package it.uniroma3.siw.siwfood.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siwfood.model.Administrator;
import it.uniroma3.siw.siwfood.model.Recipe;
import it.uniroma3.siw.siwfood.repository.AdministratorRepository;

@Service
public class AdministratorService {

    @Autowired
    private AdministratorRepository administratorRepository;

    public Administrator saveAdministrator(Administrator administrator) {
        return this.administratorRepository.save(administrator);
    }

    public Administrator findById(Long id){
        return administratorRepository.findById(id).orElse(null);
    }

    public void saveRecipeForAdministrator(Administrator administrator, Recipe recipe) {
        administrator.getSavedRecipes().add(recipe);
        administratorRepository.save(administrator);
    }

    public List<Recipe> getSavedRecipes(Administrator administrator) {
        return administrator.getSavedRecipes();
    }

    public void removeRecipeForAdministrator(Administrator administrator , Recipe recipe) {
        administrator.getSavedRecipes().remove(recipe);
        administratorRepository.save(administrator);
    }
}
