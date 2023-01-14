package ru.skillbox.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.skillbox.response.CommentResponse;
import ru.skillbox.response.FeedsResponse;
import ru.skillbox.response.Responsable;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface FeedsController {

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный вход (возвращаются totalElements, totalPages,number, size, content," +
                            " pageable-атрибуты)",
                    content = {
                            @Content(
                                    mediaType = "*/*",
                                    schema = @Schema(implementation = FeedsResponse.class))
                    }
            )
    })


    @GetMapping("/api/v1/post")
    public ResponseEntity<Responsable> getFeedsSearch(
            HttpServletRequest httpServletRequest

    )
            throws IOException;


    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный вход (возвращаются totalElements, totalPages,number, size, content," +
                            " pageable-атрибуты)",
                    content = {
                            @Content(
                                    mediaType = "*/*",
                                    schema = @Schema(implementation = CommentResponse.class))
                    }
            )
    })


    @GetMapping("/api/v1/post/{id}/comment")
    public ResponseEntity<Responsable> getAllCommentsToPost(@PathVariable long id,
                                                            HttpServletRequest httpServletRequest)
            throws JsonProcessingException;


    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный вход (возвращаются totalElements, totalPages,number, size, content," +
                            " pageable-атрибуты)",
                    content = {
                            @Content(
                                    mediaType = "*/*",
                                    schema = @Schema(implementation = CommentResponse.class))
                    }
            )
    })


    @GetMapping("/api/v1/post/{id}/comment/{commentId}/subcomment")
    public ResponseEntity<Responsable> getAllSubComments(@PathVariable long id, @PathVariable long commentId,
                                                         HttpServletRequest httpServletRequest)
            throws JsonProcessingException;


}
