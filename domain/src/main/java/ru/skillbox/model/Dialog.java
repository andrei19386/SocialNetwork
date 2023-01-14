package ru.skillbox.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Entity(name = "dialogs")
public class Dialog {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "companion_1", referencedColumnName = "id")
    private Person companion1;

    @ManyToOne()
    @JoinColumn(name = "companion_2", referencedColumnName = "id")
    private Person companion2;

    @OneToMany(mappedBy = "dialogId")
    List<Message> messages;

    @Transient
    private Long unreadCount;

    @Transient
    private Message lastMessage;

    @Transient
    private Person conversationPartner;

}
