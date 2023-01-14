package ru.skillbox.model;

import lombok.*;
import ru.skillbox.enums.MessagePermission;

import javax.persistence.*;
import java.util.List;

@Entity(name = "people")
@Table
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "reg_date")
    private Long regDate;

    @Column(name = "birth_date")
    private Long birthDate;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "photo")
    private String photo;

    @Column(name = "about")
    private String about;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id",referencedColumnName = "id", insertable = true,updatable = true)
    private City city;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id",referencedColumnName = "id", insertable = true,updatable = true)
    private Country country;

    @Column(name = "confirmation_code")
    private String confirmationCode;

    @Column(name = "is_approved")
    private Boolean isApproved;

    @Column(name = "messages_permission")
    @Enumerated(EnumType.STRING)
    private MessagePermission messagePermission;

    @Column(name = "last_online_time")
    private Long lastOnlineTime;

    @Column(name="is_blocked")
    private Boolean isBlocked;

    @Column(name="is_online")
    private Boolean isOnline;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private List<Post> postList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private List<PostLike> postLikeList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private List<PostComment> postCommentList;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Settings settings;
}
