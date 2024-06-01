package it.uniroma3.siw.siwfood.model;

import java.io.Serializable;
import java.util.Objects;

public class RecipeIngredientId implements Serializable {
    private Long recipe;
    private Long ingredient;

    
    public RecipeIngredientId() {
    }


    public RecipeIngredientId(Long recipe, Long ingredient) {
        this.recipe = recipe;
        this.ingredient = ingredient;
    }


    public Long getrecipe() {
        return recipe;
    }


    public void setrecipe(Long recipe) {
        this.recipe = recipe;
    }


    public Long getIngredient() {
        return ingredient;
    }


    public void setIngredient(Long ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredientId that = (RecipeIngredientId) o;
        return Objects.equals(recipe, that.recipe) && Objects.equals(ingredient, that.ingredient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipe, ingredient);
    }
}


