package com.gigd.daret.models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="notification")
public class Notification {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String message;
    @Column(name = "is_read") 
    private boolean read;
    /*@ManyToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;*/
    @Column(name = "user_email")
    private String userEmail;
    private String daretNom;


    @ManyToOne
    @JoinColumn(name = "user_id") 
    private ParticipantAccepte participantAccepte;
    
    public ParticipantAccepte getParticipantAccepte() {
		return participantAccepte;
	}

	public void setParticipantAccepte(ParticipantAccepte participantAccepte) {
		this.participantAccepte = participantAccepte;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getDaretNom() {
		return daretNom;
	}

	public void setDaretNom(String daretNom) {
		this.daretNom = daretNom;
	}



}
