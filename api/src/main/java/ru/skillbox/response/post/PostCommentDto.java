package ru.skillbox.response.post;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.enums.CommentType;

@Getter
@Setter
public class PostCommentDto {
    private Long id;
    private String time;
    private CommentType commentType;
    private String timeChanged;
    private Long authorId;
    private Long parentId;
    private String commentText;
    private Integer postId;
    private Boolean isBlocked;
    private Boolean isDelete;
    private Integer likeAmount;
    private Boolean myLike;
    private Integer commentsCount;//?
    private String imagePath;
}
