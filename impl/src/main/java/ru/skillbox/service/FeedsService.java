package ru.skillbox.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.enums.StatusCode;
import ru.skillbox.enums.Type;
import ru.skillbox.mapper.PostCommentMapper;
import ru.skillbox.mapper.PostMapper;
import ru.skillbox.model.Friendship;
import ru.skillbox.model.Post;
import ru.skillbox.model.PostComment;
import ru.skillbox.model.Tag;
import ru.skillbox.repository.FriendsRepository;
import ru.skillbox.repository.PostCommentRepository;
import ru.skillbox.repository.PostRepository;
import ru.skillbox.repository.TagRepository;
import ru.skillbox.request.FeedsRequest;
import ru.skillbox.response.CommentResponse;
import ru.skillbox.response.FeedsResponse;
import ru.skillbox.response.Responsable;
import ru.skillbox.response.post.PostCommentDto;
import ru.skillbox.response.post.PostDto;
import ru.skillbox.specification.PostCommentSpecification;
import ru.skillbox.specification.PostSpecification;
import ru.skillbox.specification.TagSpecification;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;


@Log4j2
@Service
public class FeedsService {
    private final PostRepository postRepository;

    private final PostCommentRepository postCommentRepository;

    private final TagRepository tagRepository;

    private final PersonService personService;

    private final FriendsRepository friendsRepository;

    @Autowired
    public FeedsService(PostRepository postRepository, PostCommentRepository postCommentRepository,
                        PersonService personService, TagRepository tagRepository,
                        FriendsRepository friendsRepository) {
        this.postRepository = postRepository;
        this.personService = personService;
        this.postCommentRepository = postCommentRepository;
        this.tagRepository = tagRepository;
        this.friendsRepository = friendsRepository;
    }

    public void updatePostType() {
        List<Post> posts = postRepository.findAll();
        for (Post post : posts) {
            if (post.getType().equals(Type.QUEUED) && post.getTime() <= LocalDateTime.now()
                    .toEpochSecond(ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now()))) {
                post.setType(Type.POSTED);
                postRepository.saveAndFlush(post);
            }
        }
    }

    @Transactional
    public ResponseEntity<Responsable> getObjectResponseEntity(FeedsRequest feedsRequest, boolean isTest)
            throws JsonProcessingException {

        long currentUserId = getCurrentUserId(isTest);
        updatePostType();
        List<PostDto> postDtoList = new ArrayList<>();
        Specification<Post> postSpec = generatePostSpecification(feedsRequest);
        Page<Post> pagedPosts = postRepository.findAll(postSpec, feedsRequest.getPageable());
        List<Post> posts = pagedPosts.getContent();
        log.debug("Find all posts from repository with given Criteria");

        if (posts != null && posts.size() != 0) {
            fillPostDtoList(currentUserId, posts, postDtoList, isTest);
        }
        FeedsResponse feedsResponse = getFeedsResponseOK(feedsRequest.getPageable(), pagedPosts, postDtoList);
        return ResponseEntity.ok(feedsResponse);
    }

    private Specification<Post> generatePostSpecification(FeedsRequest feedsRequest) {
        PostSpecification spec = new PostSpecification();
        return getPostSpecificationByDate(feedsRequest, spec)
                .and(spec.getPostsByIsDelete(feedsRequest.getIsDelete()))
                .and(getPostSpecificationByAccountId(feedsRequest, spec))
                .and(spec.getPostsNotIsDeletePerson())
                .and(getPostSpecificationByFriends(feedsRequest, spec))
                .and(getPostSpecificationByText(feedsRequest, spec))
                .and(getPostSpecificationByTags(feedsRequest, spec))
                .and(getPostSpecificationByAuthor(feedsRequest, spec));
    }

    private Specification<Post> getPostSpecificationByAccountId(FeedsRequest feedsRequest, PostSpecification spec) {
        Specification<Post> accountSpec = spec;
        if (feedsRequest.getAccountId() != null) {
            accountSpec = accountSpec.and(spec.getPostsByPersonId(feedsRequest.getAccountId()))
                   .and(getPostedForNotCurrentPerson(feedsRequest, spec));
        } else {
            accountSpec = accountSpec.and(spec.getPostsByType(Type.POSTED));
        }
        return accountSpec;
    }

    private Specification<Post> getPostedForNotCurrentPerson(FeedsRequest feedsRequest, PostSpecification spec) {
        Specification<Post> accountSpec = new PostSpecification();
        if (feedsRequest.getAccountId() != personService.getCurrentPerson().getId()) {
            accountSpec = accountSpec.and(spec.getPostsByType(Type.POSTED));
        }
        return accountSpec;
    }


    private Specification<Post> getPostSpecificationByFriends(FeedsRequest feedsRequest, PostSpecification spec) {
        Specification<Post> result = spec;
        if (feedsRequest.getWithFriends() != null && feedsRequest.getWithFriends().equals(true)) {
            List<Long> friendOrSubscriptionIds = getFriendOrSubscriptionIds(personService);
            result = result.and(spec.getPostsByPersonIds(friendOrSubscriptionIds));
        }
        return result;
    }

    private List<Long> getFriendOrSubscriptionIds(PersonService personService) {
        long id = personService.getCurrentPerson().getId();
        Set<Long> ids = new HashSet<>();
        ids.add(id);
        addFriendsOrSubscriptions(id, StatusCode.FRIEND, ids);
        addFriendsOrSubscriptions(id, StatusCode.REQUEST_TO, ids);
        addFriendsOrSubscriptions(id, StatusCode.SUBSCRIBED, ids);
        return new ArrayList<>(ids);
    }

    private void addFriendsOrSubscriptions(long id, StatusCode code, Set<Long> ids) {
        Optional<List<Friendship>> setOptional = friendsRepository.findAllByStatusCodeAndSrcPersonId(code, id);
        setOptional.ifPresent(friendships -> friendships.forEach(f -> ids.add(f.getDstPerson().getId())));
    }

    private static Specification<Post> getPostSpecificationByAuthor(FeedsRequest feedsRequest, PostSpecification spec) {
        Specification<Post> authSpec = spec;
        if (feedsRequest.getAuthor() != null) {
            String[] names = feedsRequest.getAuthor().split(" ");
            if (names.length > 1) {
                authSpec = authSpec.or(spec.getPostsByTwoNames(names[0], names[1])).or(spec.getPostsByTwoNames(names[1], names[0]));

            } else {
                authSpec = authSpec.or(spec.getPostsByFirstName(names[0])).or(spec.getPostsByLastName(names[0]));
            }
        }
        return authSpec;
    }

    private Specification<Post> getPostSpecificationByTags(FeedsRequest feedsRequest, PostSpecification spec) {
        Specification<Post> tagsSpec = spec;
        if (feedsRequest.getTags() != null) {
            List<Tag> tags = getTags(feedsRequest);
            if (!tags.isEmpty()) {
                for (Tag tag : tags) {
                    tagsSpec = tagsSpec.or(spec.getPostsByTag(tag));
                }
            } else if(!feedsRequest.getTags().isEmpty() && !feedsRequest.getTags().get(0).equals("")){
                tagsSpec = tagsSpec.and(spec.getFalse());
            }
        }
        return tagsSpec;
    }

    private List<Tag> getTags(FeedsRequest feedsRequest) {
        List<Tag> tags = new ArrayList<>();
        for (String tagString : feedsRequest.getTags()) {
            TagSpecification tagSpec = new TagSpecification();
            Specification<Tag> tagSpecification = tagSpec.getTagsByName(tagString);
            tags.addAll(tagRepository.findAll(tagSpecification));
        }
        return tags;
    }

    private static Specification<Post> getPostSpecificationByText(FeedsRequest feedsRequest, PostSpecification spec) {
        Specification<Post> wordSpec = spec;
        if (feedsRequest.getWords() != null) {
            for (String word : feedsRequest.getWords()) {
                wordSpec = wordSpec.or(spec.getPostsByTitleOrText(word));
            }

        }
        return wordSpec;
    }

    private static Specification<Post> getPostSpecificationByDate(FeedsRequest feedsRequest, PostSpecification spec) {
        Specification<Post> result = spec;
        if (feedsRequest.getDateTo() != null) {
            result = result.and(spec.getPostsByDateTo(feedsRequest.getDateTo()));
            log.debug("DateTo={}", feedsRequest.getDateTo());
        }
        if (feedsRequest.getDateFrom() != null) {
            result = result.and(spec.getPostsByDateFrom(feedsRequest.getDateFrom()));
            log.debug("DateFrom={}", feedsRequest.getDateFrom());
        }
        return result;
    }

    private void fillPostDtoList(long currentUserId, List<Post> posts, List<PostDto> postDtoList, boolean isTest) {
        for (Post post : posts) {
            log.debug("Mapping post number " + post.getId());
            PostDto postDto = PostMapper.INSTANCE.postToPostDto(post, currentUserId, isTest);
            postDtoList.add(postDto);
        }
    }

    private static FeedsResponse getFeedsResponseOK(Pageable pageable, Page<Post> pagedPosts, List<PostDto> postDtoList) throws JsonProcessingException {
        FeedsResponse feedsResponse = new FeedsResponse();
        log.debug("Generating feeds response!");
        feedsResponse.setContent(postDtoList);
        feedsResponse.setSize(pageable.getPageSize());
        feedsResponse.setTotalElements(pagedPosts.getTotalElements());
        feedsResponse.setTotalPages(pagedPosts.getTotalPages());
        feedsResponse.setEmpty(pagedPosts.isEmpty());
        feedsResponse.setSort(pagedPosts.getSort());
        feedsResponse.setFirst(pagedPosts.isFirst());
        feedsResponse.setLast(pagedPosts.isLast());
        feedsResponse.setNumber(pagedPosts.getNumber());
        feedsResponse.setNumberOfElements(pagedPosts.getNumberOfElements());
        feedsResponse.setPageable(pageable);
        ObjectMapper mapper = new ObjectMapper();
        log.debug(mapper.writeValueAsString(feedsResponse));
        return feedsResponse;
    }


    @Transactional
    public ResponseEntity<Responsable> getComments(long id, FeedsRequest feedsRequest, boolean isTest) throws JsonProcessingException {
        Post post = postRepository.findById(id).get();
        Specification<PostComment> specification = getPostCommentSpecification(post, 0L);
        Page<PostComment> postCommentPage = postCommentRepository.findAll(specification, feedsRequest.getPageable());


        CommentResponse commentResponse = getCommentResponse(feedsRequest.getPageable(), postCommentPage, isTest);

        return ResponseEntity.ok(commentResponse);
    }

    private Specification<PostComment> getPostCommentSpecification(Post post, Long parentId) {
        Specification<PostComment> specification = new PostCommentSpecification();
        PostCommentSpecification commentSpecification = new PostCommentSpecification();
        specification = specification.and(commentSpecification.getCommentsByPostId(post.getId())).and(commentSpecification.getCommentsByAuthorIsNotDelete()).and(commentSpecification.getCommentsByIsDelete()).and(commentSpecification.getCommentsByParentId(parentId));
        return specification;
    }

    private CommentResponse getCommentResponse(Pageable pageable, Page<PostComment> pages, boolean isTest) throws JsonProcessingException {
        log.debug("Generating comment response!");

        List<PostComment> comments = pages.getContent();

        List<PostCommentDto> postCommentDtoList = new ArrayList<>();
        long currentUserId = getCurrentUserId(isTest);
        for (PostComment comment : comments) {
            log.debug("Mapping comment number " + comment.getId());
            PostCommentDto postCommentDtoDto = PostCommentMapper.INSTANCE.postCommentToPostCommentDto(comment, currentUserId, postCommentRepository, isTest);
            postCommentDtoList.add(postCommentDtoDto);
        }
        CommentResponse commentResponse = fillCommentResponse(pageable, pages, postCommentDtoList);
        ObjectMapper mapper = new ObjectMapper();
        log.debug(mapper.writeValueAsString(commentResponse));
        return commentResponse;
    }

    private long getCurrentUserId(boolean isTest) {
        long currentUserId;
        if (isTest) {
            currentUserId = 1;
            log.debug("Test mode!");
        } else {
            currentUserId = personService.getCurrentPerson().getId();
        }
        return currentUserId;
    }

    private static CommentResponse fillCommentResponse(Pageable pageable, Page<PostComment> pages, List<PostCommentDto> postCommentDtoList) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setSize(pageable.getPageSize());
        commentResponse.setEmpty(pages.isEmpty());
        commentResponse.setSort(pages.getSort());
        commentResponse.setFirst(pages.isFirst());
        commentResponse.setLast(pages.isLast());
        commentResponse.setNumber(pages.getNumber());
        commentResponse.setPageable(pageable);
        commentResponse.setTotalElements(pages.getTotalElements());
        commentResponse.setNumberOfElements(pages.getNumberOfElements());
        commentResponse.setTotalPages(pages.getTotalPages());
        commentResponse.setContent(postCommentDtoList);
        return commentResponse;
    }

    @Transactional
    public ResponseEntity<Responsable> getSubComments(long id, long commentId, FeedsRequest feedsRequest, boolean isTest) throws JsonProcessingException {
        Pageable subCommentsPageable = PageRequest.of(0, postCommentRepository.findAll().size(), feedsRequest.getPageable().getSort());
        Post post = postRepository.findById(id).get();
        Specification<PostComment> specification = getPostCommentSpecification(post, commentId);
        Page<PostComment> postCommentPage = postCommentRepository.findAll(specification, subCommentsPageable);

        CommentResponse commentResponse = getCommentResponse(subCommentsPageable, postCommentPage, isTest);
        return ResponseEntity.ok(commentResponse);
    }
}
