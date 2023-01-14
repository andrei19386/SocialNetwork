package ru.skillbox.service;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.skillbox.exception.UserNotFoundException;
import ru.skillbox.model.CommentLike;
import ru.skillbox.model.Post;
import ru.skillbox.model.PostComment;
import ru.skillbox.model.PostLike;
import ru.skillbox.repository.CommentLikeRepository;
import ru.skillbox.repository.PostLikeRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
public class LikeService {

    @Value("{${isTest}}")
    private String isTestString;
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostService postService;
    private final PersonService personService;
    private final PostCommentService postCommentService;
    private static final Logger logger = LogManager.getLogger(LikeService.class);


    @Autowired
    public LikeService(PostLikeRepository postLikeRepository, CommentLikeRepository commentLikeRepository,
                       PostService postService, PersonService personService,
                       PostCommentService postCommentService, MeterRegistry meterRegistry) {
        this.postLikeRepository = postLikeRepository;
        this.commentLikeRepository = commentLikeRepository;
        this.postService = postService;
        this.personService = personService;
        this.postCommentService = postCommentService;
        meterRegistry.gauge("likesCount", postLikeRepository.findAll().size()
                + commentLikeRepository.findAll().size());
    }

    public Optional<PostLike> getLikeByPostIdAndPersonId(Long postId, Long personId) {
        return postLikeRepository.findByPostIdAndPersonId(postId, personId);
    }

    public Optional<CommentLike> getLikeByCommentIdAndPersonId(Long commentId, Long personId) {
        return commentLikeRepository.findByCommentIdAndPersonId(commentId, personId);
    }

    @Timed("gettingPostLikesCount")
    public void addPostLike(String id, boolean isTest) {
        Post post = postService.getPostById(Long.parseLong(id));
        long currentUserId = personService.getCurrentUserId(isTest);
        try {
            Optional<PostLike> postLikeOptional = getLikeByPostIdAndPersonId(post.getId(),
                    personService.getPersonById(currentUserId).getId());

            PostLike postLike;
            if (postLikeOptional.isPresent()) {
                postLike = postLikeOptional.get();
                postLike.setIsDelete(!postLike.getIsDelete());
            } else {
                postLike = new PostLike();
                postLike.setTime(LocalDateTime.now()
                        .toEpochSecond(ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now())));
                postLike.setPost(post);
                postLike.setPerson(personService.getPersonById(currentUserId));
                postLike.setIsDelete(false);
                postLikeRepository.saveAndFlush(postLike);
            }
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        logger.info("saving postLike");
    }

    @Timed("gettingCommentLikesCount")
    public void addCommentLike(String id, String commentId, boolean isTest) {
        Post post = postService.getPostById(Long.parseLong(id));
        PostComment postComment = postCommentService.getPostCommentById(Long.parseLong(commentId));
        long currentUserId = personService.getCurrentUserId(isTest);
        try {
            Optional<CommentLike> commentLikeOptional = getLikeByCommentIdAndPersonId(postComment.getId(),
                    personService.getPersonById(currentUserId).getId());

            CommentLike commentLike;
            if (commentLikeOptional.isPresent()) {
                commentLike = commentLikeOptional.get();
                commentLike.setIsDelete(!commentLike.getIsDelete());
                commentLikeRepository.saveAndFlush(commentLike);
            } else {
                commentLike = new CommentLike();
                commentLike.setComment(postComment);
                commentLike.setPerson(personService.getPersonById(personService.getCurrentUserId(isTest)));
                commentLike.setTime(LocalDateTime.now()
                        .toEpochSecond(ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now())));
                commentLike.setIsDelete(false);
                commentLikeRepository.saveAndFlush(commentLike);
                postService.savePost(post);
            }
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        logger.info("saving commentLike");
    }


    public void deletePostLike(String id, boolean isTest) {
        Post post = postService.getPostById(Long.parseLong(id));
        long currentUserId = personService.getCurrentUserId(isTest);
        try {
            Optional<PostLike> postLikeOptional = getLikeByPostIdAndPersonId(post.getId(),
                    personService.getPersonById(currentUserId).getId());

            if (postLikeOptional.isPresent()) {
                PostLike postLike = postLikeOptional.get();
                postLike.setIsDelete(!postLike.getIsDelete());
                postLikeRepository.saveAndFlush(postLike);
                logger.info("deleting postLike");
            }
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteCommentLike(String id, String commentId, boolean isTest) {
        PostComment comment = postCommentService.getPostCommentById(Long.parseLong(commentId));
        long currentUserId = personService.getCurrentUserId(isTest);
        try {
            Optional<CommentLike> commentLikeOptional = getLikeByCommentIdAndPersonId(comment.getId(),
                    personService.getPersonById(currentUserId).getId());
            if (commentLikeOptional.isPresent()) {
                CommentLike commentLike = commentLikeOptional.get();
                commentLike.setIsDelete(!commentLike.getIsDelete());
                commentLikeRepository.saveAndFlush(commentLike);
                logger.info("deleting commentLike");
            }
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}