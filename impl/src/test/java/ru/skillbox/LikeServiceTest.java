package ru.skillbox;

import junit.framework.TestCase;
import lombok.NoArgsConstructor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.skillbox.enums.Type;
import ru.skillbox.model.*;
import ru.skillbox.repository.*;
import ru.skillbox.service.LikeService;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integration.properties")
@NoArgsConstructor
public class LikeServiceTest extends TestCase {

    @Value("{${isTest}}")
    private String isTestString;
    @Autowired
    private LikeService likeService;
    @Autowired
    private PostLikeRepository postLikeRepository;
    @Autowired
    private CommentLikeRepository commentLikeRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PostCommentRepository postCommentRepository;

    @Before
    @Override
    public void setUp() {

        Person person = new Person();

        person.setFirstName("T");
        person.setLastName("L");

        personRepository.saveAndFlush(person);

        Post post = new Post();
        post.setTime((new Date()).getTime());
        post.setPerson(person);
        post.setIsBlocked(true);
        post.setIsDelete(false);
        post.setType(Type.POSTED);
        post.setPostText("SomeText");
        post.setPostCommentList(null);
        post.setTitle("SomeTitle");
        post.setIsBlocked(true);
        post.setIsDelete(false);
        postRepository.saveAndFlush(post);

        PostLike postLike = new PostLike();
        postLike.setIsDelete(false);
        postLike.setPost(post);
        postLike.setPerson(person);
        postLike.setTime(new Date().getTime());
        postLikeRepository.saveAndFlush(postLike);


        PostComment postComment = new PostComment();
        postComment.setParentId(0L);
        postComment.setPerson(person);
        postComment.setCommentType("POST");
        postComment.setCommentText("Text of comment");
        postComment.setPost(post);
        postComment.setIsBlocked(false);
        postComment.setIsDelete(false);
        postCommentRepository.saveAndFlush(postComment);

        CommentLike commentLike = new CommentLike();
        commentLike.setIsDelete(false);
        commentLike.setComment(postComment);
        commentLike.setPerson(person);
        commentLike.setTime(new Date().getTime());
        commentLikeRepository.saveAndFlush(commentLike);
    }

    @After
    @Override
    public void tearDown() {
        commentLikeRepository.deleteAll();
        commentLikeRepository.flush();
        postLikeRepository.deleteAll();
        postLikeRepository.flush();
        postCommentRepository.deleteAll();
        postCommentRepository.flush();
        postRepository.deleteAll();
        postRepository.flush();
        personRepository.deleteAll();
        personRepository.flush();
    }

    @Test
    public void testAddPostLike() {
        Post post = postRepository.findAll().get(0);
        postLikeRepository.deleteAll();
        int beforeSize = postLikeRepository.findAll().size();
        likeService.addPostLike(String.valueOf(post.getId()), isTestString.equals("{true}"));
        int afterSize = postLikeRepository.findAll().size();
        assertNotSame(beforeSize, afterSize);
    }

    @Test
    public void testAddCommentLike() {
        likeService.addCommentLike(String.valueOf(postRepository.findAll().get(0).getId()),
                String.valueOf(postCommentRepository.findAll().get(0).getId()), isTestString.equals("{true}"));
    }

    @Test
    public void testDeletePostLike() {
        likeService.deletePostLike(String.valueOf(postRepository
                .findAll().get(0).getId()), isTestString.equals("{true}"));
        assertTrue(postLikeRepository.findAll().get(0).getIsDelete());
    }

    @Test
    public void testDeleteCommentLike() {
        likeService.deleteCommentLike(String.valueOf(postRepository.findAll().get(0).getId()),
                String.valueOf(postCommentRepository.findAll().get(0).getId()), isTestString.equals("{true}"));
        assertTrue(commentLikeRepository.findAll().get(0).getIsDelete());
    }
}
