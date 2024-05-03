package it.uniroma3.siw.siwfood.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siwfood.model.Recipe;

public interface RecipeRepository extends CrudRepository <Recipe, Long>{

}
