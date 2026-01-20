package com.gigd.daret.web;

import com.gigd.daret.models.User;
import com.gigd.daret.repository.UserRepository;
import com.gigd.daret.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepo;
    
    @Autowired 
    private UserDetailService userDetailService;
   
    @GetMapping("/monProfile")
    public String showUserProfile(Model model) {
        User authenticatedUser = userDetailService.getCurrentAuthenticatedUser();

        if (authenticatedUser != null) {
            model.addAttribute("user", authenticatedUser);
            return "vue/monProfile"; // Assurez-vous que le chemin est correct
        } else {
            // Gérer le cas où l'utilisateur n'est pas authentifié
            return "redirect:/vue/monProfile"; // Rediriger vers la page de connexion par exemple
        }
    }
    
    
    @GetMapping("/editProfile")
    public String showEditProfilePage(Model model) {
        User authenticatedUser = userDetailService.getCurrentAuthenticatedUser();

        if (authenticatedUser != null) {
            model.addAttribute("user", authenticatedUser);
            return "vue/editProfile";
        } else {
            // Handle the case where the user is not authenticated
            return "redirect:/connexion"; // Redirect to the login page, for example
        }
    }
    
    @PostMapping("/saveProfile")
    public String saveProfile(@ModelAttribute User updatedUser) {
        User authenticatedUser = userDetailService.getCurrentAuthenticatedUser();

        if (authenticatedUser != null) {
            // Mettez à jour les champs modifiables de l'utilisateur avec les nouvelles valeurs
            authenticatedUser.setNom(updatedUser.getNom());
            authenticatedUser.setPrenom(updatedUser.getPrenom());
            authenticatedUser.setEmail(updatedUser.getEmail());
            authenticatedUser.setTelephone(updatedUser.getTelephone());
            authenticatedUser.setCin(updatedUser.getCin());
            authenticatedUser.setAdresse(updatedUser.getAdresse());

            // Enregistrez l'utilisateur mis à jour dans la base de données
             updateUser(authenticatedUser);

            // Redirigez vers la page de profil après la mise à jour
            return "redirect:/monProfile";
        } else {
            // Gérer le cas où l'utilisateur n'est pas authentifié
            return "redirect:/connexion"; // Rediriger vers la page de connexion, par exemple
        }
    }
    public void updateUser(User user) {
        // Ajoutez le code pour mettre à jour l'utilisateur dans la base de données
        userRepo.save(user); // Supposons que vous utilisiez JpaRepository<User, Long> userRepository
    }
}