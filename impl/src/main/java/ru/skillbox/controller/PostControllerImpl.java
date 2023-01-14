package ru.skillbox.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.model.PostController;
import ru.skillbox.request.PostAddRequest;
import ru.skillbox.response.post.PostResponse;
import ru.skillbox.service.PostService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class PostControllerImpl implements PostController {

    private final PostService postService;

    @Override
    public void addNewPost(PostAddRequest request, HttpServletRequest httpServletRequest) {
        postService.addPost(request, httpServletRequest);
    }

    @Override
    public ResponseEntity<String> deletePostById(String id) {
        postService.deletePost(postService.getPostById(Long.parseLong(id)));
        return ResponseEntity.ok("ok");
    }

    @Override
    public ResponseEntity<PostResponse> getPostById(String id) {
        PostResponse response = postService.addPostResponse(id);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<String> putPostById(PostAddRequest request,
                                              HttpServletRequest httpServletRequest, String id) {
        postService.updatePost(request, httpServletRequest, id);
        return ResponseEntity.ok("ok");
    }
}