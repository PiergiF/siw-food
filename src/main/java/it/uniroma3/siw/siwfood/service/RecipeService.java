package it.uniroma3.siw.siwfood.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siwfood.model.Chef;
import it.uniroma3.siw.siwfood.model.Courses;
import it.uniroma3.siw.siwfood.model.Ingredient;
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

    @Autowired
    private IngredientService ingredientService;

    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    public Recipe findById(Long id) {
        return recipeRepository.findById(id).orElse(null);
    }

    /*
    public List<Recipe> getRandomRecipes(int numElements){
        List<Recipe> all = this.findAll();
        List<Recipe> randomRecipes = new ArrayList<>();
        if(all.size() > numElements){
            for(int i=0; i<numElements; i++){
                int randomIndex = (int) (Math.random() * all.size());
                randomRecipes.add(all.get(randomIndex));
            }
            return randomRecipes;
        }else{
            return all;
        }
    }
    */

    public List<Recipe> getRandomRecipes(int numElements){
        List<Recipe> all = this.findAll();
        List<Recipe> randomRecipes = new ArrayList<>();
        int randomIndex;
        Set<Integer> s = new HashSet<>();
        if(all.size() > numElements){
            for(int i=0; i<numElements; i++){
                while (true) {
                    randomIndex = (int) (Math.random() * all.size());
                    if(!s.contains(randomIndex)){
                        s.add(randomIndex);
                        randomRecipes.add(all.get(randomIndex));
                        break;
                    }
                }
            }
            return randomRecipes;
        }else{
            return all;
        }
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

    public List<Recipe> findAllByCourse(Courses course){
        return recipeRepository.findAllByCourse(course);
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

    public List<Recipe> searchRecipes(String query) {
        //ricette che contengono la query nel nome
            List<Recipe> recipesByName = recipeRepository.findByNameContainingIgnoreCase(query);

        //ricette con gli ingredienti che contengono la query nel nome
            List<Ingredient> ingredientByQuery = ingredientService.findIngredientContainsQuery(query); //cerca gli ingredienti che contengono la ricerca
            //per ogni ingrediente trovato cerca le ricette che lo contengono
            Set<Recipe> recipesContainsIngredient = new HashSet<>(); //dove metterò tutte le ricette
            for (Ingredient ingredient : ingredientByQuery) {
                List<Recipe> r = recipeIngredientRepository.findAllRecipesContainsIngredient(ingredient.getId()); //tutte le ricette con quell'ingrediente
                //aggiungi ogni ricetta che contiene quell'ingrediente all'insieme di tutte ricette di tutti gli ingredienti che contengono la query nel nome
                for (Recipe re : r) {
                    recipesContainsIngredient.add(re);
                }
            }

        //List<Recipe> recipesByIngredient = recipeRepository.findByIngredientName(query);


        Set<Recipe> allRecipes = new HashSet<>(recipesByName);
        allRecipes.addAll(recipesContainsIngredient);
        return new ArrayList<>(allRecipes);
    }
}
