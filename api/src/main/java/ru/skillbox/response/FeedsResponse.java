package ru.skillbox.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import ru.skillbox.response.post.PostDto;

import java.util.List;

@Setter
@Getter
public class FeedsResponse implements Responsable {
    private long totalElements;
    private long totalPages;
    private long number;
    private long size;
    private List<PostDto> content;

    private Sort sort;
    boolean first;
    boolean last;
    Integer numberOfElements;
    org.springframework.data.domain.Pageable pageable;
    boolean empty;

    @Override
    public Responsable getResponse(String s) {
        FeedsResponse feedsResponse = new FeedsResponse();
        return feedsResponse;
    }
}
