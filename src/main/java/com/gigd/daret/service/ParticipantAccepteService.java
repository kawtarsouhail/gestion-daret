package com.gigd.daret.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gigd.daret.models.DaretStart;
import com.gigd.daret.models.ParticipantAccepte;
import com.gigd.daret.repository.ParticipantAccepteRepository;

@Service
public class ParticipantAccepteService {

	 @Autowired
	    private ParticipantAccepteRepository participantAccepteRepository;
	
	 public List<DaretStart> getDaretsForConnectedUser(String userEmail) {
	        List<ParticipantAccepte> participantAcceptes = participantAccepteRepository.findByUserEmail(userEmail);

	        return participantAcceptes.stream()
	                .map(ParticipantAccepte::getDaretStart)
	                .collect(Collectors.toList());
	    }
	 
	
	 public Long getParticipantIdByEmail(String email) {
	        List<ParticipantAccepte> participantAcceptes = participantAccepteRepository.findByUserEmail(email);
	        return (participantAcceptes != null && !participantAcceptes.isEmpty()) ? participantAcceptes.get(0).getId() : null;
	    }
	
	 
	 /*
	 @Autowired
	    private DaretRepository daretRepository;
	    @Scheduled(cron = "0 0 0 * * *") // Exécute tous les jours à minuit
	    public void passerAuParticipantSuivant() {
	        List<Daret> darets = daretRepository.findAll();

	        darets.forEach(daret -> {
	            // Obtenez la liste des participants triés par le tour de rôle
	            List<ParticipantAccepte> participants = participantAccepteRepository.findByDaretIdOrderByToursDeRole(daret.getId());

	            // Vérifiez s'il y a des participants avant de procéder
	            if (participants != null && !participants.isEmpty()) {
	                // Recherchez l'ID du participant actuel
	                Long participantActuelId = daret.getParticipantActuel();

	                // Trouvez le participant actuel
	                ParticipantAccepte participantActuel = findParticipantById(participants, participantActuelId);

	                if (participantActuel != null) {
	                    // Passez au participant suivant en fonction de la périodicité
	                    int indexActuel = participants.indexOf(participantActuel);
	                    int indexSuivant;
	                    switch (daret.getPeriodicite()) {
	                        case "15j":
	                            // Passez au participant suivant après 15 jours
	                            indexSuivant = (indexActuel + 1) % participants.size();
	                            break;
	                        case "par semaine":
	                            // Passez au participant suivant après une semaine (7 jours)
	                            indexSuivant = (indexActuel + 1) % participants.size();
	                            break;
	                        case "mensuel":
	                            // Passez au participant suivant après un mois (30 jours)
	                            indexSuivant = (indexActuel + 1) % participants.size();
	                            break;
	                        // Ajoutez d'autres cas en fonction des périodicités possibles
	                        default:
	                            throw new IllegalArgumentException("Périodicité non prise en charge : " + daret.getPeriodicite());
	                    }

	                    // Mettez à jour l'ID du participant actuel
	                    daret.setParticipantActuel(participants.get(indexSuivant).getId());
	                    daretRepository.save(daret);
	                } else {
	                    // Aucun participant actuel trouvé, vous pouvez gérer cela comme vous le souhaitez
	                    System.out.println("Aucun participant actuel trouvé pour le Daret : " + daret.getId());
	                }
	            } else {
	                // Aucun participant, vous pouvez gérer cela comme vous le souhaitez
	                System.out.println("Aucun participant disponible pour la rotation des rôles pour le Daret : " + daret.getId());
	            }
	        });
	    }

	    private ParticipantAccepte findParticipantById(List<ParticipantAccepte> participants, Long participantId) {
	        return participants.stream()
	                .filter(participant -> participant.getId().equals(participantId))
	                .findFirst()
	                .orElse(null);
	    }
	 
	
	
	*/    
	    
	    
	    //Payement
	    @Transactional
	    public void updatePaymentStatus(Long participantId) {
	        Optional<ParticipantAccepte> participantOptional = participantAccepteRepository.findById(participantId);
	        
	        participantOptional.ifPresent(participant -> {
	            participant.setPayement(true);
	            participantAccepteRepository.save(participant);
	        });
	    }
	    
	    
	
}
