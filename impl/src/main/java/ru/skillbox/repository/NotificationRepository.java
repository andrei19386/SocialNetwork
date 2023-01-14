package ru.skillbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.model.Notification;

import java.util.List;


public interface NotificationRepository extends JpaRepository<Notification, Long> {


    List<Notification> findByPersonId(Long personId);
}
