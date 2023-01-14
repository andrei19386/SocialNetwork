package ru.skillbox.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "post_comment")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment_type")
    private String commentType;

    private Long timeChanged;

    @Column(name = "is_delete")
    private Boolean isDelete;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    @Column(name = "parent_id")
    private Long parentId;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Person person;

    @Column(name = "comment_text")
    private String commentText;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

    private Long time;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", referencedColumnName = "id")
    private List<CommentLike> commentLikes;
}
