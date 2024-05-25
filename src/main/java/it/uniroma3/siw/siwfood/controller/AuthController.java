package it.uniroma3.siw.siwfood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.uniroma3.siw.siwfood.model.Credentials;
import it.uniroma3.siw.siwfood.model.User;
import it.uniroma3.siw.siwfood.service.CredentialsService;
import it.uniroma3.siw.siwfood.service.UserService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class AuthController {

    @Autowired
	private UserService userService;

    @Autowired
	private CredentialsService credentialsService;

    /*
    @GetMapping(value = "/")
    public String getHomePage(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {	
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            model.addAttribute("accountRole",credentials.getRole());
		}
        return "homePage.html";
    }
    */

    @GetMapping("/loginPage")
    public String getLoginPage() {
        return "loginPage.html";
    }

    @GetMapping(value = "/registrationPage")
    public String getRegistrationPage(Model model) {
        model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
        return "registrationPage.html";
    }

    @PostMapping("/registrationData")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult userBindingResult,
                                @Valid @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult,
                                Model model) {
        // se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB
        if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
            userService.saveUser(user);
            credentials.setUser(user);
            credentialsService.saveCredentials(credentials);
            model.addAttribute("RegistrationCompleted", true); //non funziona perché c'è il redirect di mezzo
            return "redirect:/loginPage";
        }
        return "registrationPage.html";
    }
    
}
