package it.uniroma3.siw.siwfood.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.siwfood.model.Recipe;
import it.uniroma3.siw.siwfood.model.RecipeIngredient;
import it.uniroma3.siw.siwfood.model.RecipeIngredientId;
import jakarta.transaction.Transactional;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, RecipeIngredientId>{

    @Query("SELECT ri FROM RecipeIngredient ri WHERE ri.recipe.id = :recipeId")
    public List<RecipeIngredient> findAllByRecipeId(@Param("recipeId") Long recipeId);

    @Query("SELECT ri.recipe FROM RecipeIngredient ri WHERE ri.ingredient.id =:ingredientId")
    public List<Recipe> findAllRecipesContainsIngredient(Long ingredientId);

    //public boolean existsByRecipeId(Long recipeId);

    @Modifying
    @Transactional
    @Query("DELETE FROM RecipeIngredient ri WHERE ri.recipe.id = :recipeId")
    public void deleteByRecipeId(@Param("recipeId") Long recipeId);

    @Modifying
    @Transactional
    @Query("DELETE FROM RecipeIngredient ri WHERE ri.ingredient.id = :ingredientId")
    public void deleteByIngredientId(Long ingredientId);
}
