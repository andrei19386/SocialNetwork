package ru.skillbox.service;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.enums.NotificationType;
import ru.skillbox.enums.StatusCode;
import ru.skillbox.enums.Type;
import ru.skillbox.model.Person;
import ru.skillbox.model.Post;
import ru.skillbox.model.PostFile;
import ru.skillbox.model.Tag;
import ru.skillbox.repository.PostRepository;
import ru.skillbox.request.PostAddRequest;
import ru.skillbox.request.settings.NotificationInputDto;
import ru.skillbox.response.post.PostResponse;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final PostFileService postFileService;
    private final PersonService personService;
    private static final Logger logger = LogManager.getLogger(PostService.class);
    private final NotificationsService notificationsService;
    private final FriendsService friendsService;

    @Autowired
    public PostService(PostRepository postRepository, PostFileService postFileService,
                       PersonService personService, MeterRegistry meterRegistry, NotificationsService notificationsService, FriendsService friendsService) {
        this.postRepository = postRepository;
        this.postFileService = postFileService;
        this.personService = personService;
        this.notificationsService = notificationsService;
        this.friendsService = friendsService;
        meterRegistry.gauge("postsCount", postRepository.findAll().size());
    }

    public Post getPostById(long id) {
        return postRepository.findById(id).get();
    }

    public void savePost(Post post) {
        postRepository.save(post);
    }

    public List<Tag> convertStringToTag(List<String> tagsFromRequest) {
        List<Tag> tagList = new ArrayList<>();
        for (String tagString : tagsFromRequest) {
            Tag tag = new Tag();
            tag.setTag(tagString);
            tagList.add(tag);
        }
        return tagList;
    }

    public List<String> convertTagToString(List<Tag> tagList) {
        List<String> tags = new ArrayList<>();
        for (Tag tag : tagList) {
            tags.add(tag.getTag());
        }
        return tags;
    }

    public void deletePost(Post post) {
        post.setIsDelete(true);
        postRepository.saveAndFlush(post);
        logger.info("deleting post № " + post.getId());
    }

    @Timed("gettingNewPostsCount")
    public void addPost(PostAddRequest request, HttpServletRequest httpServletRequest) {
        Person person = personService.getCurrentPerson();
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setPostText(request.getPostText());
        post.setTags(convertStringToTag(request.getTags()));
        post.setIsBlocked(request.getIsBlocked());
        post.setIsDelete(false);
        post.setPerson(person);


        String publishDateString = httpServletRequest.getParameter("publishDate");

        if (publishDateString != null) {
            post.setType(Type.QUEUED);
        } else {
            post.setType(Type.POSTED);
            sendNotification(person.getId());
        }
        switch (post.getType()) {
            case POSTED: {
                post.setTime(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
                break;
            }
            case QUEUED: {
                assert publishDateString != null;
                post.setTime(Long.parseLong(publishDateString));
                break;
            }
        }
        Optional<PostFile> postFile = postFileService.getPostFileByPath(request.getImagePath());
        postFile.ifPresent(file -> post.setPostFiles(List.of(file)));
        postRepository.save(post);

        logger.info("saving post № " + post.getId());
    }

    public PostResponse addPostResponse(String id) {
        Post post = getPostById(Long.parseLong(id));
        PostResponse response = new PostResponse();
        response.setPostText(post.getPostText());
        response.setId(post.getId());
        response.setTime(post.getTime());
        response.setTitle(post.getTitle());
        response.setIsBlocked(post.getIsBlocked());
        response.setAuthorId(post.getPerson().getId());
        response.setType(post.getType());
        response.setCommentsCount(post.getPostCommentList().size());
        response.setTags(convertTagToString(post.getTags()));
        return response;
    }

    private void sendNotification(Long currentUser) {
        List<Long> listFriendsId = friendsService.getByCodePersonIdsForCurrentUser(StatusCode.FRIEND);
        for (Long id : listFriendsId) {
            NotificationInputDto notificationInputDto = new NotificationInputDto();
            notificationInputDto.setAuthorId(currentUser);
            notificationInputDto.setUserId(id);
            notificationInputDto.setNotificationType(NotificationType.POST);
            notificationInputDto.setContent("Пост");
            notificationsService.createAndSaveNotification(notificationInputDto);
        }
    }

    public void updatePost(PostAddRequest request, HttpServletRequest httpServletRequest,
                           String id) {
        Post post = getPostById(Long.parseLong(id));
        post.setTitle(request.getTitle());
        if (request.getTime() != null) {
            post.setTime(request.getTime());
        }
        post.setTimeChanged(LocalDateTime.now()
                .toEpochSecond(ZoneOffset.UTC));
        post.setPostText(request.getPostText());
        post.setPerson(personService.getCurrentPerson());
        if (convertStringToTag(request.getTags()) != null) {
            post.setTags(convertStringToTag(request.getTags()));
        }
        Optional<PostFile> postFile = postFileService.getPostFileByPath(request.getImagePath());
        postFile.ifPresent(file -> post.setPostFiles(new ArrayList<>(List.of(file))));
        String publishDateString = httpServletRequest.getParameter("publishDate");
        if (publishDateString != null) {
            post.setType(Type.QUEUED);
        } else {
            post.setType(Type.POSTED);
        }
        switch (post.getType()) {
            case POSTED:
                post.setTime(LocalDateTime.now()
                        .toEpochSecond(ZoneOffset.UTC));
                break;
            case QUEUED:
                assert publishDateString != null;
                post.setTime(Long.parseLong(publishDateString));
        }
        postRepository.save(post);
        logger.info("updating post № " + post.getId());
    }
}
