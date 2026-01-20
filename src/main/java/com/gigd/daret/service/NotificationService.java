package com.gigd.daret.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gigd.daret.models.Notification;
import com.gigd.daret.models.Participant;
import com.gigd.daret.repository.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void notifyUser(Participant participant, String message, String daretNom) {
        String userEmail = participant.getUser().getEmail();

        System.out.println("Notifying user with email: " + userEmail + " - Message: " + message);

        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setRead(false);
        notification.setUserEmail(userEmail);
        notification.setDaretNom(daretNom);

       // notificationRepository.save(notification);
    }

    public List<Notification> getUserNotifications(String userEmail) {
        return notificationRepository.findByUserEmailAndReadFalse(userEmail);
    }
}
