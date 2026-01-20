package com.gigd.daret.models;

import org.springframework.context.ApplicationEvent;

public class UserStatusChangeEvent extends ApplicationEvent {
    private String message;
    private String userEmail;
    private String daretNom;

    public UserStatusChangeEvent(Object source, String userEmail, String message, String daretNom) {
        super(source);
        this.userEmail = userEmail;
        this.message = message;
        this.daretNom = daretNom;
    }

    public String getMessage() {
        return message;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getDaretNom() {
        return daretNom;
    }
}
