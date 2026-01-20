package com.gigd.daret.models;

import java.util.Date;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;



@Entity
@Table(name="daret")
public class Daret {
	 @Id  	
	    @GeneratedValue(strategy=GenerationType.IDENTITY)
		private Long id;
	    @Column(unique=true)
	    @NotBlank(message = "Le nom est obligatoire !!!!!")
	    private String nom;
	    @NotNull(message = "Le nombre des personnes est obligatoire !!!!!")
	    private Long nombre_personnes ;
	    @NotNull(message = "La date de démarrage est obligatoire !!!!!")
	    @Future(message = "La date de démarrage doit être dans le futur")
	    @DateTimeFormat(pattern = "dd/MM/yyyy") // Spécifiez ici le format de date attendu
	    private Date date_demarrage;
	    @NotBlank(message = "La periodicite est obligatoire !!!!!")
	    @Pattern(regexp = "15j|par semaine|mensuel", message = "Veuillez sélectionner une periodicite valide")
	    private  String periodicite ;
	    @NotNull(message = "Le montant est obligatoire !!!!!")
	    private Double montant;
	    @Column(name = "is_deja_deplace")
	    private boolean isDejaDeplace;

	  
	    
       
		public Daret() {
			super();
		}

		public Daret(String nom2, String dateDemarrage, int nombrePersonnes, double montant2) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public String toString() {
			return "daret [id=" + id + ", nom=" + nom + ", nombre_personnes=" + nombre_personnes + ", date_demarrage="
					+ date_demarrage + ", periodicite=" + periodicite + ", montant=" + montant + "]";
		}

		public Long getId() {
		    return this.id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getNom() {
			return nom;
		}

		public void setNom(String nom) {
			this.nom = nom;
		}

		public Long getNombre_personnes() {
			return nombre_personnes;
		}

		public void setNombre_personnes(Long nombre_personnes) {
			this.nombre_personnes = nombre_personnes;
		}

		public Date getDate_demarrage() {
			return date_demarrage;
		}

		public void setDate_demarrage(Date date_demarrage) {
			this.date_demarrage = date_demarrage;
		}

		public String getPeriodicite() {
			return periodicite;
		}

		public void setPeriodicite(String periodicite) {
			this.periodicite = periodicite;
		}

		public Double getMontant() {
			return montant;
		}

		public void setMontant(Double montant) {
			this.montant = montant;
		}
	
		
		@OneToMany(mappedBy = "daret")
	    private Set<Participant> participants;



		public void setActive(boolean b) {
			// TODO Auto-generated method stub
			
		}
	    @OneToMany(mappedBy = "daret")
	    private Set<ParticipantAccepte> participantsAcceptes;


		public boolean isDejaDeplace() {
			return isDejaDeplace;
		}

		public void setDejaDeplace(boolean isDejaDeplace) {
			this.isDejaDeplace = isDejaDeplace;
		}

	

		public Set<ParticipantAccepte> getParticipantsAcceptes() {
			return participantsAcceptes;
		}

		public void setParticipantsAcceptes(Set<ParticipantAccepte> participantsAcceptes) {
			this.participantsAcceptes = participantsAcceptes;
		}

		
	    
}
