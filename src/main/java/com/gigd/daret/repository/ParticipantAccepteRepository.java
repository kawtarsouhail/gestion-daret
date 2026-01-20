package com.gigd.daret.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gigd.daret.models.Daret;
import com.gigd.daret.models.DaretStart;
import com.gigd.daret.models.ParticipantAccepte;

public interface ParticipantAccepteRepository extends JpaRepository<ParticipantAccepte, Long>{
	
	long countByDaretId(Long daretId);
	List<ParticipantAccepte> findByDaret(Daret dart);
	
	List<ParticipantAccepte> findByUserEmail(String userEmail);
	
	List<ParticipantAccepte> findByDaretIdOrderByToursDeRole(Long daretId);
	
	List<ParticipantAccepte> findByDaretStart(DaretStart daret);
	
	List<ParticipantAccepte> findByDaretStartId(Long daretStartId);

}