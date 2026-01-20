package com.gigd.daret.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gigd.daret.models.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserEmailAndReadFalse(String userEmail);

    List<Notification> findByUserEmail(String userEmail);
}
