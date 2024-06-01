package it.uniroma3.siw.siwfood.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.siwfood.model.RecipeIngredient;
import it.uniroma3.siw.siwfood.model.RecipeIngredientId;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, RecipeIngredientId>{
    @Query("SELECT ri FROM RecipeIngredient ri WHERE ri.recipe.id = :recipeId")
    List<RecipeIngredient> findAllByRecipeId(@Param("recipeId") Long recipeId);
}
