package it.uniroma3.siw.siwfood.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siwfood.model.Chef;
import it.uniroma3.siw.siwfood.model.Recipe;
import it.uniroma3.siw.siwfood.repository.ChefRepository;

@Service
public class ChefService {
    
    @Autowired
    private ChefRepository chefRepository;

    public boolean existsByNameAndSurname(String name, String surname){
        return chefRepository.existsByNameAndSurname(name, surname);
    }

    public Chef saveChef(Chef chef) {
        return this.chefRepository.save(chef);
    }

    public List<Chef> findAll() {
        return chefRepository.findAll();
    }

    public Chef findById(Long id){
        return chefRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id){
        chefRepository.deleteById(id);
    }

    public void saveRecipeForChef(Chef chef, Recipe recipe) {
        chef.getSavedRecipes().add(recipe);
        chefRepository.save(chef);
    }

    public List<Recipe> getSavedRecipes(Chef chef) {
        return chef.getSavedRecipes();
    }

    public void removeRecipeForChef(Chef chef, Recipe recipe) {
        chef.getSavedRecipes().remove(recipe);
        chefRepository.save(chef);
    }

    public List<Chef> searchChefs(String query) {
        return chefRepository.findByNameContainingIgnoreCaseOrSurnameContainingIgnoreCase(query, query);
    }
    
}
