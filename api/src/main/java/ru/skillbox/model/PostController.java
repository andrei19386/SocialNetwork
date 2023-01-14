package ru.skillbox.model;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.request.PostAddRequest;
import ru.skillbox.response.post.PostResponse;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/v1/post")
public interface PostController {
    @PostMapping
    void addNewPost(@RequestBody PostAddRequest request,
                    HttpServletRequest httpServletRequest);

    @DeleteMapping("/{id}")
    ResponseEntity<String> deletePostById(@PathVariable String id);


    @GetMapping("/{id}")
    ResponseEntity<PostResponse> getPostById(@PathVariable String id);


    @PutMapping("/{id}")
    public ResponseEntity<String> putPostById(@RequestBody PostAddRequest request,
                                              HttpServletRequest httpServletRequest,
                                              @PathVariable String id);
}
