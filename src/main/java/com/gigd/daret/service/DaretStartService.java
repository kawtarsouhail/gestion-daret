package com.gigd.daret.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gigd.daret.models.DaretStart;
import com.gigd.daret.models.Notification;
import com.gigd.daret.models.ParticipantAccepte;
import com.gigd.daret.repository.DaretStartRepository;
import com.gigd.daret.repository.NotificationRepository;
import com.gigd.daret.repository.ParticipantAccepteRepository;

@Service
public class DaretStartService {

    @Autowired
    private ParticipantAccepteRepository participantRepository;

    @Autowired
    private DaretStartRepository daretRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    //Envoyer montant
    
    public boolean verifierPaiementPourDaret(Long daretId) {
        List<ParticipantAccepte> participants = participantRepository.findByDaretStartId(daretId);

        for (ParticipantAccepte participant : participants) {
            if (!participant.isPayement()) {
                return false; 
            }
        }

        return true; 
    }

    public double calculerMontantTotalPourDaret(Long daretId) {
        DaretStart daret = daretRepository.findById(daretId).orElse(null);

        if (daret != null) {
            return daret.getMontant() * daret.getNombre_personnes();
        } else {
            throw new NoSuchElementException("Le daret avec l'ID " + daretId + " n'a pas été trouvé.");
        }
    }
    
    public boolean envoyerMontantEtVerifierPaiement(Long daretId) {
        List<ParticipantAccepte> participants = participantRepository.findByDaretStartId(daretId);

        Optional<ParticipantAccepte> participantAvecTour1 = participants.stream()
                .filter(participant -> participant.getToursDeRole() != null && participant.getToursDeRole() == 1)
                .findFirst();

        if (participantAvecTour1.isPresent()) {
            boolean tousPayes = verifierPaiementPourDaret(daretId);

            if (tousPayes) {
                double montantTotal = calculerMontantTotalPourDaret(daretId);

                // vérifier si la notification a déjà été envoyée
                if (!participantAvecTour1.get().isNotifEnvoyee()) {
                    //envoi du montant ici

                    
                    participants.forEach(participant -> {
                        participant.setPayement(true);
                        participant.setNotifEnvoyee(true); // Marquez la notification comme envoyée
                    });

                    participantRepository.saveAll(participants);

                    // Créer une notification
                    Notification notification = new Notification();
                    notification.setMessage("Vous avez reçu le montant de " + montantTotal + " DH avec succès pour le daret" + daretId);
                    notification.setUserEmail(participantAvecTour1.get().getUser().getEmail());
                    notification.setDaretNom(participantAvecTour1.get().getDaretStart().getNom());

                    // Sauvegarder la notification
                    notificationRepository.save(notification);

                    return true; 
                } else {
                    throw new RuntimeException("Le montant a déjà été envoyé , attendez pour prochain tour.");
                }
            } else {
                throw new RuntimeException("Impossible d'envoyer le montant. Certains participants n'ont pas payé.");
            }
        }

        return false;
    }



}
