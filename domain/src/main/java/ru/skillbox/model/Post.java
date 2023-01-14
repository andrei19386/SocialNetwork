package ru.skillbox.model;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.enums.Type;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long time;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "post2tag",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private List<Tag> tags;
    private String title;
    @Column(name = "is_delete")
    private Boolean isDelete;

    private Long timeChanged;
    @Column(name = "post_text")
    private String postText;
    @Column(name = "is_blocked")
    private Boolean isBlocked;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private List<PostLike> postLikes;

    @Column(name = "post_type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Person person;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private List<PostFile> postFiles;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private List<PostComment> postCommentList;

    public Post(Long id, Long time, List<Tag> tags, String title, String postText, Boolean isBlocked,
                List<PostLike> postLikes) {
        this.id = id;
        this.time = time;
        this.tags = tags;
        this.title = title;
        this.postText = postText;
        this.isBlocked = isBlocked;
        this.postLikes = postLikes;
    }

    public Post() {

    }
}
