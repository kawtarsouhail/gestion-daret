/*package com.gigd.daret.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gigd.daret.repository.DaretRepository;

@Service
public class DaretService {

	 private  DaretRepository daretRepository;

	    @Autowired
	    public DaretService(DaretRepository daretRepository) {
	        this.daretRepository = daretRepository;
	    }

	    // Méthode pour supprimer un daret par ID
	    public void deleteDaretById(Long id) {
	        // Vérifiez si le daret existe dans la base de données
	        if (daretRepository.existsById(id)) {
	            // Si oui, supprimez-le
	            daretRepository.deleteById(id);
	        }
	        // Sinon, vous pouvez gérer cette situation selon votre logique métier
	        // Par exemple, en lançant une exception ou en enregistrant un message de log
	    }
	    
	    
	    
}*/
