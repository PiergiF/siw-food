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
import it.uniroma3.siw.siwfood.model.Customer;
import it.uniroma3.siw.siwfood.repository.CredentialsRepository;
import it.uniroma3.siw.siwfood.service.CredentialsService;

@ControllerAdvice
public class GlobalController {
    @Autowired
    private CredentialsService credentialsService;

    @ModelAttribute("userDetails")

    static public UserDetails getUserDetails() {
        UserDetails userDetails = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return userDetails;
    }

    @ModelAttribute("accountRole")

    public String getUserRole(){
        Credentials credentials = null;
        UserDetails userDetails = getUserDetails();
        if(userDetails!=null){
            //UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //UserDetails userDetails = ud;
            credentials = credentialsService.getCredentials(userDetails.getUsername());
            return credentials.getRole();
        }
        return "ROLE_ANONYMOUS";
    }

    @ModelAttribute("loggedId")

    public Long getLoggedId(){
        Credentials credentials = null;
        UserDetails ud=getUserDetails();
        if(ud!=null){
            credentials = credentialsService.getCredentials(ud.getUsername());
            if(getUserRole().equals("CUSTOMER")){
                return credentials.getCustomer().getId();
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

