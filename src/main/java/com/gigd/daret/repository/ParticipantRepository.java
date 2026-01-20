package com.gigd.daret.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gigd.daret.models.Daret;
import com.gigd.daret.models.Participant;
import com.gigd.daret.models.ParticipantAccepte;


public interface ParticipantRepository extends JpaRepository<Participant, Long> {
	 Optional<Participant> findById(Long id); 
	 List<Participant> findByDaret(Daret dart);
	 
	 
	 
	 //daretStart
	 void save(ParticipantAccepte participantAccepte);
	    Optional<Participant> findByUserEmail(String userEmail);
	}
