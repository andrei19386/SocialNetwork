package ru.skillbox.model;

import org.springframework.web.bind.annotation.*;
import ru.skillbox.request.CommentAddRequest;

@RequestMapping("/api/v1/post/{id}/comment")
public interface PostCommentController {
    @PostMapping
    void addCommentByIdPost(
            @PathVariable String id, @RequestBody CommentAddRequest request);

    @DeleteMapping("/{commentId}")
    void deleteCommentByIdPost(@PathVariable String id, @PathVariable String commentId);

    @PutMapping("/{commentId}")
    void putCommentByIdPost(@RequestBody CommentAddRequest request,
                            @PathVariable String id, @PathVariable String commentId);

}
