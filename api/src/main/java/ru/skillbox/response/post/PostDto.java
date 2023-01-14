package ru.skillbox.response.post;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.enums.Type;

@Getter
@Setter
public class PostDto {
    private Long id;
    private String time;
    private String timeChanged;
    private Long authorId;
    private String title;
    private Type type;
    private String postText;
    private Boolean isDelete;
    private Boolean isBlocked;
    private Integer commentsCount;
    private String[] tags;
    private Integer likeAmount;
    private Boolean myLike;
    private String imagePath;
    private String publishDate;
}
