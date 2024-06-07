package it.uniroma3.siw.siwfood.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siwfood.model.Credentials;
import it.uniroma3.siw.siwfood.repository.CredentialsRepository;
import jakarta.transaction.Transactional;

@Service
public class CredentialsService {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected CredentialsRepository credentialsRepository;

    @Transactional
    public Credentials getCredentials(Long id) {
        Optional<Credentials> result = this.credentialsRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public Credentials getCredentials(String username) {
        Optional<Credentials> result = this.credentialsRepository.findByUsername(username);
        return result.orElse(null);
    }

    @Transactional
    public Credentials saveCredentials(Credentials credentials, String role) {
        if(role.equals("CUSTOMER")){
            credentials.setRole(Credentials.CUSTOMER_ROLE);
        }
        else if(role.equals("CHEF")){
            credentials.setRole(Credentials.CHEF_ROLE);
        }else if(role.equals("ADMINISTRATOR")){
            credentials.setRole(Credentials.ADMINISTRATOR_ROLE);
        }
        credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        return this.credentialsRepository.save(credentials);
    }

    public void deleteByChefId(Long chefId){
        this.credentialsRepository.deleteByChefId(chefId);
    }
}

