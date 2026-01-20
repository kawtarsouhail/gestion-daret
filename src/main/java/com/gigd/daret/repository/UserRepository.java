package com.gigd.daret.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gigd.daret.models.Daret;
import com.gigd.daret.models.User;


public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findById(Long id);
	public User findByEmail(String email); 

}