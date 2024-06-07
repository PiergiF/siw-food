package it.uniroma3.siw.siwfood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties.Credential;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.uniroma3.siw.siwfood.model.Credentials;
import it.uniroma3.siw.siwfood.model.User;
import it.uniroma3.siw.siwfood.repository.CredentialsRepository;
import it.uniroma3.siw.siwfood.service.CredentialsService;

@ControllerAdvice
public class GlobalController {
    @Autowired
    private CredentialsService credentialsService;

    @ModelAttribute("userDetails")

    public UserDetails getUser() {
        UserDetails user = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return user;
    }

    @ModelAttribute("userRole")

    public String getUserRole(){
        Credentials credentials = null;
        UserDetails ud;
        ud=getUser();
        if(ud!=null){
            UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            credentials = credentialsService.getCredentials(userDetails.getUsername());
            return credentials.getRole();
        }
        return "ANONYMOUS";
    }

    @ModelAttribute("loggedId")

    public Long getLoggedId(){
        Credentials credentials = null;
        UserDetails ud=getUser();
        if(ud!=null){
            credentials = credentialsService.getCredentials(ud.getUsername());
            if(getUserRole().equals("CUSTOMER")){
                return credentials.getUser().getId();
            }else if(getUserRole().equals("CHEF")){
                return credentials.getChef().getId();
            }else if(getUserRole().equals("ADMINISTRATOR")){
                return credentials.getAdministrator().getId();
            }
            return null;
        }
        return null;
    }
}

