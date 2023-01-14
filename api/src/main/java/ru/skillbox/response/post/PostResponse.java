package ru.skillbox.response.post;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.enums.Type;

import java.util.List;

@Getter
@Setter
public class PostResponse {

    private Long id;
    private Long time;
    private Long timeChanged;
    private Long authorId;
    private String title;
    private Type type;
    private String postText;
    private Boolean isBlocked;
    private Boolean isDelete;
    private Integer commentsCount;
    private List<String> tags;
    private Integer likeAmount;
    private Boolean myLike;
    private String imagePath;
    private Long publishDate;
}
