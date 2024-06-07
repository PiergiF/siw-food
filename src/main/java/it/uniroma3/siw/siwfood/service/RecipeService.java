package it.uniroma3.siw.siwfood.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siwfood.model.Chef;
import it.uniroma3.siw.siwfood.model.Recipe;
import it.uniroma3.siw.siwfood.model.RecipeIngredient;
import it.uniroma3.siw.siwfood.repository.RecipeIngredientRepository;
import it.uniroma3.siw.siwfood.repository.RecipeRepository;
import jakarta.transaction.Transactional;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;

    @Autowired
    private ChefService chefService;

    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    public Recipe findById(Long id) {
        return recipeRepository.findById(id).orElse(null);
    }

    public Recipe save(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public void saveRecipeIngredient(RecipeIngredient recipeIngredient) {
        recipeIngredientRepository.save(recipeIngredient);
    }

    public List<RecipeIngredient> findRecipeIngredientsByRecipeId(Long recipeId) {
        return recipeIngredientRepository.findAllByRecipeId(recipeId);
    }

    @Transactional
    public void deleteRecipeIngredientsByRecipeId(Long recipeId) {
        recipeIngredientRepository.deleteByRecipeId(recipeId);
    }

    /*
    public boolean existByRecipeId(Long recipeId) {
        return recipeIngredientRepository.existsByRecipeId(recipeId);
    }
    */

    public List<Recipe> findAllByChefId(Long chefId){
        Chef chef = chefService.findById(chefId);
        return recipeRepository.findAllByChef(chef);
    }

    public void deleteById(Long id){
        recipeRepository.deleteById(id);
    }

    public void deleteRecipesByChefId(Long chefId){
        List<Recipe> recipes = findAllByChefId(chefId);
        for(Recipe recipe : recipes){
            this.deleteRecipeIngredientsByRecipeId(recipe.getId());
            this.deleteById(recipe.getId());
        }
    }

    @Transactional
    public void deleteRecipeIngredientsByIngredientId(Long ingredientId){
        recipeIngredientRepository.deleteByIngredientId(ingredientId);
    }
}
