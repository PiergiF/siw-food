package it.uniroma3.siw.siwfood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siwfood.model.Administrator;
import it.uniroma3.siw.siwfood.repository.AdministratorRepository;

@Service
public class AdministratorService {

    @Autowired
    private AdministratorRepository administratorRepository;

    public Administrator saveAdministrator(Administrator administrator) {
        return this.administratorRepository.save(administrator);
    }
}
