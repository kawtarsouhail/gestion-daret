package com.gigd.daret.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gigd.daret.models.Daret;
import com.gigd.daret.models.DaretStart;
import com.gigd.daret.models.Notification;
import com.gigd.daret.models.Participant;
import com.gigd.daret.models.ParticipantAccepte;
import com.gigd.daret.models.User;
import com.gigd.daret.models.UserStatusChangeEvent;
import com.gigd.daret.repository.DaretRepository;
import com.gigd.daret.repository.DaretStartRepository;
import com.gigd.daret.repository.NotificationRepository;
import com.gigd.daret.repository.ParticipantAccepteRepository;
import com.gigd.daret.repository.ParticipantRepository;
import com.gigd.daret.repository.UserRepository;
import com.gigd.daret.service.DaretStartService;
import com.gigd.daret.service.NotificationService;
import com.gigd.daret.service.ParticipantAccepteService;
import com.gigd.daret.service.UserDetailService;


@Controller
public class ParticipantController {
    
    @Autowired
    private ParticipantRepository participantRepository;
    
    @Autowired
	 private  DaretRepository daretRepository; 

    @Autowired
    private ParticipantAccepteRepository participantAccepteRepository;
    @Autowired 
    private DaretStartRepository daretStartRepository;

    
    @Autowired
    private ParticipantAccepteService participantAccepterService;
    
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    @Autowired
    private NotificationService notificationservice;
    
    @Autowired
    private DaretStartService daretStartService;
    @Autowired
    private UserDetailService userDetailService;
    
   //liste des demande de paticipation qui affiche a l'admin 
    @GetMapping("/demandeParticipation")
    public String participantsByDaret(Long daretId, Model model) {
       Daret daret = daretRepository.findById(daretId).orElse(null);

     
            List<Participant> participants = participantRepository.findByDaret(daret);
            List<User> users = new ArrayList<>();
            for (Participant participant : participants) {
                if (participant.getUser() != null) {
                    users.add(participant.getUser());
                }
            }

            model.addAttribute("users", users);

            model.addAttribute("participants", participants);

            return "vue/demandeParticipation";

    }


	//les details de Mes daret
	@GetMapping("/detaillesDaretsActuelle")
	public String detaillesDaretsActuelle(Long daretId, Model model) {
	    DaretStart daret = daretStartRepository.findById(daretId).orElse(null);
	        List<ParticipantAccepte> participants = participantAccepteRepository.findByDaretStart(daret);
	        model.addAttribute("participants", participants);
	        model.addAttribute("darets", daret);
	        return "vue/DetaillesDaretActuelle"; 
	    
	        
	} 
	@GetMapping("/daretAct")
	public String daretActuel(Model model){
		 List<DaretStart> listeDarets = daretStartRepository.findAll();
		    model.addAttribute("darets", listeDarets);
		return "vue/daretAct";
	}
	
	
	
	@GetMapping("/accepter")
	public String accepter(){
		return "vue/demandeParticipation";
	}
	
	@GetMapping("/accepter/{daretId}/{participantId}")    
	public String accepterParticipant(@PathVariable Long participantId, 
	                                  @PathVariable Long daretId, 
	                                  Authentication authentication) {
	    if (participantId != null) {
	        Daret daret = daretRepository.findById(daretId)
	                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Daret non trouvé avec id " + daretId));

	        long count = participantAccepteRepository.countByDaretId(daretId);
	        if (count < daret.getNombre_personnes()) {
	            participantRepository.findById(participantId).ifPresent(participant -> {
	                ParticipantAccepte accepte = new ParticipantAccepte();
	                String daretNom = participant.getDaret().getNom();
	                BeanUtils.copyProperties(participant, accepte);
	                accepte.genererToursDeRoleUnique();
	                participantAccepteRepository.save(accepte);  

	                String requestingUserEmail = participant.getUser().getEmail();
	                eventPublisher.publishEvent(new UserStatusChangeEvent(this, requestingUserEmail,
	                        "Votre demande de participation a été acceptée.", daretNom));

	                Notification notification = new Notification();
	                notification.setMessage("Votre demande de participation a " + daretNom + " a été acceptée.");
	                notification.setDaretNom(daretNom);
	                notification.setUserEmail(requestingUserEmail);
	                notificationRepository.save(notification);

	                participantRepository.delete(participant);
	            });

	            return "redirect:/admin";
	        }
	    }
	    
	    return "redirect:/admin";
	}

	@GetMapping("/refuser/{participantId}")
	public String refuserParticipant(@PathVariable Long participantId, 
	                                 RedirectAttributes redirectAttributes, 
	                                 Authentication authentication) {
	    Optional<Participant> participantOptional = participantRepository.findById(participantId);
	    if (participantOptional.isPresent()) {
	        Participant participant = participantOptional.get();
	        String requestingUserEmail = participant.getUser().getEmail();
            String daretNom = participant.getDaret().getNom();
                 
	        eventPublisher.publishEvent(new UserStatusChangeEvent(this, requestingUserEmail,
	                "Votre demande de participation a été acceptée.", daretNom));

	        Notification notification = new Notification();
	        notification.setMessage("Votre demande de participation a "+daretNom + " a été refusée.");
            notification.setUserEmail(requestingUserEmail);
	        notification.setDaretNom(daretNom);
	        notificationRepository.save(notification);

	        participantRepository.delete(participant);
	    }

	    return "redirect:/admin";
	}


	
	@GetMapping("/notification")
	public String getNotifications(Model model) {
		   User authenticatedUser = userDetailService.getCurrentAuthenticatedUser();


	    System.out.println("Current authenticated user email: " + authenticatedUser);

	    if (authenticatedUser != null) {
	        List<Notification> notifications = notificationservice.getUserNotifications(authenticatedUser.getEmail());
	        System.out.println("Number of notifications for user email " + authenticatedUser + ": " + notifications.size());
	        model.addAttribute("notifications", notifications);
	    }

	    return "vue/notification"; 
	}

	
	//Conner User connecter
/*	private String getCurrentAuthenticatedUserEmail() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        return userDetails.getUsername();
	    }

	    return null;
	}
*/
   
 

//Mes Darets 
   @Autowired
   private ParticipantAccepteService participantAccepteService;

   @GetMapping("/MesDarets")
   public String mesDarets(Model model, Principal principal) {
       String userEmail = principal.getName();
       Long participantId = participantAccepteService.getParticipantIdByEmail(userEmail);
       List<DaretStart> darets = participantAccepteService.getDaretsForConnectedUser(userEmail);
       model.addAttribute("darets", darets);
       model.addAttribute("participantId", participantId);
       return "/vue/MesDarets";
   }
   
   
   
   
 //Detailles Mes darets 
   @GetMapping("/detaillesMesDarets")
   public String detaillesMesDarets(@RequestParam("daretId") Long daretId,
                                     Model model,
                                     @RequestParam("participantId") Long participantId) {
       // Vérifier si l'ID du daret est non nul
       if (daretId != null) {
           Optional<DaretStart> optionalDaret = daretStartRepository.findById(daretId);

           if (optionalDaret.isPresent()) {
               DaretStart daret = optionalDaret.get();

               model.addAttribute("daret", daret);

               Optional<ParticipantAccepte> optionalParticipantAccepte = participantAccepteRepository.findById(participantId);

               if (optionalParticipantAccepte.isPresent()) {
                   ParticipantAccepte participantAccepte = optionalParticipantAccepte.get();

                   model.addAttribute("participantAccepte", participantAccepte);
               } else {
                   model.addAttribute("errorMessage", "ParticipantAccepte non trouvé avec l'ID spécifié.");
               }

           } else {
               model.addAttribute("errorMessage", "Daret non trouvé avec l'ID spécifié.");
           }
       } else {
           model.addAttribute("errorMessage", "L'ID du daret est nul.");
       }

       return "vue/DetaillesMesDarets";
   }

   @PostMapping("/payer/{participantId}/{daretId}")
   public String payer(@PathVariable Long participantId, @PathVariable Long daretId) {
       participantAccepterService.updatePaymentStatus(participantId);
       return "redirect:/detaillesMesDarets?daretId=" + daretId + "&participantId=" + participantId;
   }

   
   
   

   
   //EnvoyerMontant

   @PostMapping("/envoyerMontant")
   public String envoyerMontant(@RequestParam("daretId") Long daretId, RedirectAttributes redirectAttributes) {
       try {
           boolean success = daretStartService.envoyerMontantEtVerifierPaiement(daretId);

           if (success) {
               redirectAttributes.addFlashAttribute("successMessage", "Le montant a été envoyé avec succès.");
           } else {
               redirectAttributes.addFlashAttribute("errorMessage", "Impossible d'envoyer le montant. Le montant a déjà été envoyé.");
           }
       } catch (RuntimeException e) {
           redirectAttributes.addFlashAttribute("errorMessage", "Une erreur interne s'est produite : " + e.getMessage());
       }

       return "redirect:/detaillesDaretsActuelle?daretId=" + daretId;
   }

       
   }

   
   
   
 

   
   
