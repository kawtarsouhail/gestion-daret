package com.gigd.daret.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gigd.daret.models.Daret;
import com.gigd.daret.models.DaretStart;
import com.gigd.daret.models.Participant;
import com.gigd.daret.models.ParticipantAccepte;
import com.gigd.daret.models.User;
import com.gigd.daret.repository.DaretRepository;
import com.gigd.daret.repository.DaretStartRepository;
import com.gigd.daret.repository.ParticipantRepository;
import com.gigd.daret.repository.UserRepository;
import com.gigd.daret.service.UserDetailService;


@EnableScheduling
@Controller
public class DaretController {
	@Autowired
	 private  DaretRepository daretRepository; 
	@Autowired
	 private  ParticipantRepository participantRepository;
	@Autowired
	 private UserRepository userRepository;
	@Autowired
	private DaretStartRepository daretStartRepository;

		
	@GetMapping("/admin")
	public String afficherDarets(Model model) {
	    List<Daret> listeDarets = daretRepository.findAllWithParticipants();
	    model.addAttribute("darets", listeDarets);
	    LocalDateTime now = LocalDateTime.now(); 

	    List<Daret> filtered = listeDarets.stream()
	            .filter(d -> d.getDate_demarrage().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().isAfter(now))
	            .collect(Collectors.toList());

	    model.addAttribute("darets", filtered);
	    return "vue/AdminInterface";
	}



		@GetMapping("/user")
		public String afficherDaretUser(Model model) {
		    User user = userDetailService.getCurrentAuthenticatedUser();
     
		    List<Daret> listeDarets = daretRepository.findAll();
		    model.addAttribute("darets", listeDarets);
		    model.addAttribute("users", user);
		    
		    return "vue/UserInterface";
		}
		@GetMapping("/creer_daret")
		public String afficherDaret(Model model) {
		    List<Daret> listeDarets = daretRepository.findAll();
		    model.addAttribute("darets", listeDarets);

		    return "vue/creer_daret";
		}
		  @GetMapping("/delete/{id}")
		    public String supprimerDaret(@PathVariable Long id) {
		        daretRepository.deleteById(id);
		        return "redirect:/creer_daret"; // Redirige vers la liste après la suppression
		    }


		@GetMapping("/ajouterdaret")
		public String ajDaret(Model model){	
			model.addAttribute("daret", new Daret());
		    return "vue/ajouter_daret";
		}
		
		@PostMapping("/ajouterdaret")
		public String processRegister(@Valid Daret daret, BindingResult result) {
		    if (result.hasErrors()) {
		        return "vue/ajouter_daret"; 
		    }
		    daretRepository.save(daret);
	        return "redirect:/ajouterdaret"; 
	    }
		
		@Autowired
	    UserDetailService userDetailService;
		@GetMapping("/participer")
		public String participation(@RequestParam Long daretId ,Model model) {
		    Participant participant = new Participant(); 
		    User user = userDetailService.getCurrentAuthenticatedUser();
		    model.addAttribute("participant", participant);
		    model.addAttribute("utilisateur", user);
		    model.addAttribute("daretId", daretId);
		    return "vue/participer";
		}
		
		@PostMapping("/participer")
		public String participer(@ModelAttribute Participant participant,  
		                         @RequestParam Long daretId,
		                         @RequestParam Long userId,
		                         BindingResult result,
		                         Model model) {
			     
			  if (result.hasErrors()) {				  
			        model.addAttribute("org.springframework.validation.BindingResult.participant", result);
			        return "vue/participer"; 
			    }
		    User user = userRepository.findById(userId).orElse(null);
		    Daret daret = daretRepository.findById(daretId).orElse(null);

		    
		    
		        participant.setUser(user);
		        participant.setDaret(daret);
		       
		        participantRepository.save(participant);
		        
		        return "redirect:/user";
		  
		}

//Daret Start 
		@Scheduled(fixedRate = 60000)
		@Transactional
		public void transferAndDeleteExpiredDarets() {
		    LocalDate now = LocalDate.now();
		    List<Daret> expiredDarets = daretRepository.findAll().stream()
		        .filter(d -> d.getDate_demarrage().before(Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant())))
		        .collect(Collectors.toList());

		    for (Daret d : expiredDarets) {
		        DaretStart dStart = new DaretStart(d.getNom(), d.getDate_demarrage(), d.getNombre_personnes(), d.getMontant(), d.getPeriodicite());
		        daretStartRepository.save(dStart);

		        
		        for (ParticipantAccepte participantAccepte : d.getParticipantsAcceptes()) {
		            participantAccepte.setDaret(null); 
		            participantAccepte.setDaretStart(dStart); 
		            participantRepository.save(participantAccepte); 
		        }

		        daretRepository.delete(d);

		        System.out.println("Daret expiré et transféré: " + d);
		    }
		}
		
		
}