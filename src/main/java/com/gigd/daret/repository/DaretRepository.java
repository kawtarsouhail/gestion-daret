package com.gigd.daret.repository;
import com.gigd.daret.models.Daret;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DaretRepository extends JpaRepository<Daret, Long> {
	 Optional<Daret> findById(Long id);
	 Optional<Daret> findByNom(String nom);
	 @Query("SELECT DISTINCT d from Daret d LEFT JOIN FETCH d.participants")
	    List<Daret> findAllWithParticipants();
}
