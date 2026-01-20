package com.gigd.daret.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity(name ="participant")
public class Participant implements Serializable {
    private static final long serialVersionUID = 4738820128726469948L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@NotBlank(message = "Le rib est obligatoire !!!!!")
    @Pattern(regexp = "\\d{14}", message = "Le RIB doit contenir exactement 14 chiffres")  
    private String rib;
    private Integer nbrpar;
    private Integer role;// est ce que va participer avec deux role ou une seul role 
    //private String userEmail;
    
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRib() {
		return rib;
	}

	public void setRib(String rib) {
		this.rib = rib;
	}

	public Integer getNbrpar() {
		return nbrpar;
	}

	public void setNbrpar(Integer nbrpar) {
		this.nbrpar = nbrpar;
	}


	public Daret getDaret() {
		return daret;
	}

	public void setDaret(Daret daret) {
		this.daret = daret;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}


	@ManyToOne
    @JoinColumn(name = "daret_id")
    private Daret daret;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    

}
