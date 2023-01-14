package ru.skillbox.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "setting")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Settings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "friend_request")
    private boolean friendRequest;

    @Column(name = "friend_birthday")
    private boolean friendBirthday;

    @Column(name = "post_comment")
    private boolean postComment;

    @Column(name = "comment_comment")
    private boolean commentComment;

    private boolean post;
    private boolean message;

    @Column(name = "send_phone_massage")
    private boolean sendPhoneMessage;

    @Column(name = "send_email_massage")
    private boolean sendEmailMessage;
}