package ru.skillbox.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import ru.skillbox.response.post.PostCommentDto;

import java.util.List;

@Setter
@Getter
public class CommentResponse implements Responsable {
    private long totalElements;
    private long totalPages;
    private long number;
    private long size;
    private List<PostCommentDto> content;

    private Sort sort;
    boolean first;
    boolean last;
    Integer numberOfElements;
    org.springframework.data.domain.Pageable pageable;
    boolean empty;

    @Override
    public Responsable getResponse(String s) {
        CommentResponse commentResponse = new CommentResponse();
        return commentResponse;
    }
}
