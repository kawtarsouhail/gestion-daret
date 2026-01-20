package com.gigd.daret.models;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "participant_accepte")
public class ParticipantAccepte implements Serializable{
	 private static final long serialVersionUID = 1L;
	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	 @NotBlank(message = "Le rib est obligatoire !!!!!")
	    private String rib;

	    private Integer nbrpar;
	    private Integer role;

		//@Column(unique=true)
		 private Integer toursDeRole;
		    
		    @Transient
		    private List<Integer> toursDeRoleGeneres = new ArrayList<>();
		
		@ManyToOne
	    @JoinColumn(name = "daret_id")
	    private Daret daret;

	    @ManyToOne
	    @JoinColumn(name = "user_id")
	    private User user;
	    
	    
	    @Column(name = "payement")
	    private boolean payement;
	    
	    
	    
	    @ManyToOne
	    @JoinColumn(name = "daret_start_id")
	    private DaretStart daretStart;
	    
	    
	    @OneToMany(mappedBy = "participantAccepte")
	    private List<Notification> notifications;
	    
	    @Column(name = "is_notif_envoyee")
	    private boolean isNotifEnvoyee;
	    
	    
	    
		public boolean isNotifEnvoyee() {
			return isNotifEnvoyee;
		}
		public void setNotifEnvoyee(boolean isNotifEnvoyee) {
			this.isNotifEnvoyee = isNotifEnvoyee;
		}
		public boolean isPayement() {
			return payement;
		}
		public void setPayement(boolean payement) {
			this.payement = payement;
		}
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
	public Integer getRole() {
		return role;
	}
	public void setRole(Integer role) {
		this.role = role;
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
	public Integer getToursDeRole() {
		return toursDeRole;
	}
	public void setToursDeRole(Integer toursDeRole) {
		this.toursDeRole = toursDeRole;
	}
	public List<Integer> getToursDeRoleGeneres() {
		return toursDeRoleGeneres;
	}
	public void setToursDeRoleGeneres(List<Integer> toursDeRoleGeneres) {
		this.toursDeRoleGeneres = toursDeRoleGeneres;
	}

	
	
	
   public List<Notification> getNotifications() {
		return notifications;
	}
	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}
public DaretStart getDaretStart() {
		return daretStart;
	}
	public void setDaretStart(DaretStart daretStart) {
		this.daretStart = daretStart;
	}
    private static Map<Long, Set<Integer>> daretRoleNumbers = new ConcurrentHashMap<>();

	//Donner Tours de role
	public Integer genererToursDeRoleUnique() {
	    if (daret != null && daret.getNombre_personnes() != null && daret.getNombre_personnes() > 0) {
	        if (!toursDeRoleGeneres.isEmpty()) {
	            if (toursDeRoleGeneres.contains(daret.getId())) {
	                return null; 
	            }
	        }

	        Set<Integer> generatedRolesForThisDaret = daretRoleNumbers.computeIfAbsent(daret.getId(), k -> ConcurrentHashMap.newKeySet());

	        List<Integer> uniqueNumbers = IntStream.rangeClosed(1, daret.getNombre_personnes().intValue())
	                .boxed()
	                .collect(Collectors.toList());

	        Collections.shuffle(uniqueNumbers);

	        for (Integer uniqueNumber : uniqueNumbers) {
	            if (!generatedRolesForThisDaret.contains(uniqueNumber)) {
	                toursDeRole = uniqueNumber;
	                generatedRolesForThisDaret.add(toursDeRole);	              
	                toursDeRoleGeneres.add(toursDeRole);
	                
	           
	                return toursDeRole;
	            }
	        }
	    }
	    return null; 
	}
	  
	  
	}

