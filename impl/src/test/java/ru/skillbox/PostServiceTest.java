package ru.skillbox;

import junit.framework.TestCase;
import lombok.NoArgsConstructor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.skillbox.enums.Type;
import ru.skillbox.model.Person;
import ru.skillbox.model.Post;
import ru.skillbox.repository.PersonRepository;
import ru.skillbox.repository.PostRepository;
import ru.skillbox.service.PostService;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integration.properties")
@NoArgsConstructor
public class PostServiceTest extends TestCase {


    @Autowired
    private PostService postService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PostRepository postRepository;


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
    }

    @After
    @Override
    public void tearDown() {
        postRepository.deleteAll();
        postRepository.flush();
        personRepository.deleteAll();
        personRepository.flush();
    }

    @Test
    public void testDeletePost() {
        Post post = postRepository.findAll().get(0);
        postService.deletePost(post);
        assertTrue(post.getIsDelete());
    }
}
