package it.uniroma3.siw.siwfood.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.siwfood.model.Chef;
import it.uniroma3.siw.siwfood.model.Quantity;
import it.uniroma3.siw.siwfood.model.Recipe;
import it.uniroma3.siw.siwfood.model.Customer;
//import it.uniroma3.siwfood.model.Image;
import it.uniroma3.siw.siwfood.repository.ChefRepository;
//import it.uniroma3.siwfood.repository.ImageRepository;

@Service
public class ChefService {
    
    //sito
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

    /*
    //tutta l'immagine
    @Autowired
    private ImageRepository imageRepository;
    
    public void saveChefWithImage(Chef chef, MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(StringUtils.cleanPath(file.getOriginalFilename()));
        image.setData(file.getBytes());
        imageRepository.save(image);

        chef.setImage(image);
        this.chefRepository.save(chef);
    }
    */
    
    
}
