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
import ru.skillbox.enums.LikeType;
import ru.skillbox.enums.Type;
import ru.skillbox.model.Person;
import ru.skillbox.model.Post;
import ru.skillbox.model.PostComment;
import ru.skillbox.repository.PersonRepository;
import ru.skillbox.repository.PostCommentRepository;
import ru.skillbox.repository.PostRepository;
import ru.skillbox.request.CommentAddRequest;
import ru.skillbox.service.PostCommentService;

import javax.transaction.Transactional;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integration.properties")
@NoArgsConstructor
public class PostCommentServiceTest extends TestCase {

    @Value("{${isTest}}")
    private String isTestString;
    @Autowired
    private PostCommentService postCommentService;
    @Autowired
    private PostCommentRepository postCommentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PersonRepository personRepository;


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

        PostComment postComment = new PostComment();
        postComment.setPerson(person);
        postComment.setCommentType("POST");
        postComment.setCommentText("Text of comment");
        postComment.setPost(post);
        postComment.setIsBlocked(false);
        postComment.setIsDelete(false);
        postCommentRepository.saveAndFlush(postComment);
    }

    @After
    @Override
    public void tearDown() {
        postCommentRepository.deleteAll();
        postCommentRepository.flush();
        postRepository.deleteAll();
        postRepository.flush();
        personRepository.deleteAll();
        personRepository.flush();
    }

    @Transactional
    @Test
    public void testDeleteComment() {
        PostComment postComment = postCommentRepository.findAll().get(0);
        Long postId = postComment.getPost().getId();
        postCommentService.deleteComment(String.valueOf(postId), String.valueOf(postComment.getId()));
        assertTrue(postComment.getIsDelete());
    }

    @Test
    public void addComment() {
        CommentAddRequest request = new CommentAddRequest();
        request.setCommentText("Text of comment");
        request.setCommentType(LikeType.POST);
        request.setIsBlocked(false);
        request.setIsDelete(false);
        request.setAuthorId(personRepository.findAll().get(0).getId());
        Long id = postRepository.findAll().get(0).getId();
        request.setPostId(id);
        int beforeSize = postCommentRepository.findAll().size();
        postCommentService.addComment(String.valueOf(id), request, isTestString.equals("{true}"));
        int afterSize = postCommentRepository.findAll().size();
        assertNotSame(beforeSize, afterSize);
    }
}
