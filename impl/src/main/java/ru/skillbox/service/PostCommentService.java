package ru.skillbox.service;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.skillbox.enums.NotificationType;
import ru.skillbox.exception.UserNotFoundException;
import ru.skillbox.model.Post;
import ru.skillbox.model.PostComment;
import ru.skillbox.repository.PostCommentRepository;
import ru.skillbox.request.CommentAddRequest;
import ru.skillbox.request.settings.NotificationInputDto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class PostCommentService {
    @Value("{${isTest}}")
    private String isTestString;
    private final PostCommentRepository postCommentRepository;
    private final PostService postService;
    private final PersonService personService;
    private final NotificationsService notificationsService;
    private static final Logger logger = LogManager.getLogger(PostCommentService.class);

    @Autowired
    public PostCommentService(PostCommentRepository postCommentRepository, PostService postService,
                              PersonService personService, MeterRegistry meterRegistry, NotificationsService notificationsService) {
        this.postCommentRepository = postCommentRepository;
        this.postService = postService;
        this.personService = personService;
        this.notificationsService = notificationsService;
        meterRegistry.gauge("postCommentsCount", postCommentRepository.findAll().size());
    }

    public PostComment getPostCommentById(long id) {
        return postCommentRepository.findById(id).get();
    }

    @Timed("gettingPostCommentsCount")
    public void addComment(String id, CommentAddRequest request, boolean isTest) {
        Post post = postService.getPostById(Long.parseLong(id));
        PostComment postComment = new PostComment();
        postComment.setCommentText(request.getCommentText());
        logger.info(isTestString);
        long currentUserId = personService.getCurrentUserId(isTest);
        try {
            postComment.setPerson(personService.getPersonById(currentUserId));
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (request.getParentId() != null) {
            postComment.setParentId(request.getParentId());
        } else {
            postComment.setParentId(0L);
        }
        postComment.setPost(post);
        postComment.setTime(LocalDateTime.now()
                .toEpochSecond(ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now())));
        postComment.setIsDelete(false);
        postComment.setIsBlocked(false);
        sendNotification(currentUserId, post.getPerson().getId());
        logger.info("saving comment № " + postComment.getId());
        postCommentRepository.save(postComment);
    }

    private void sendNotification(Long currentUser, Long otherPersonId) {
        NotificationInputDto notificationInputDto = new NotificationInputDto();
        notificationInputDto.setAuthorId(currentUser);
        notificationInputDto.setUserId(otherPersonId);
        notificationInputDto.setNotificationType(NotificationType.POST_COMMENT);
        notificationInputDto.setContent("Получен коментаррий");
        notificationsService.createAndSaveNotification(notificationInputDto);
    }

    public void deleteComment(String id, String commentId) {
        PostComment postComment = getPostCommentById(Long.parseLong(commentId));
        postComment.setIsDelete(true);
        postCommentRepository.saveAndFlush(postComment);
        logger.info("deleting comment № " + postComment.getId());
    }

    public void updateComment(CommentAddRequest request, String id, String commentId) {
        Post post = postService.getPostById(Long.parseLong(id));
        PostComment postComment = getPostCommentById(Long.parseLong(commentId));
        if (!post.getPostCommentList().isEmpty()) {
            postComment.setCommentText(request.getCommentText());
            postComment.setPerson(personService.getCurrentPerson());
            postComment.setPost(post);
            if (request.getTime() != null) {
                postComment.setTime(request.getTime());
            }
            if (request.getIsBlocked() != null) {
                postComment.setIsBlocked(request.getIsBlocked());
            }
            postComment.setIsDelete(false);
            postCommentRepository.save(postComment);
            logger.info("updating comment № " + postComment.getId());
        }
    }
}
