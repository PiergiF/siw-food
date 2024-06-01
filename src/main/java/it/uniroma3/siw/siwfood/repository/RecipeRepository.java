package it.uniroma3.siw.siwfood.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siwfood.model.Chef;
import it.uniroma3.siw.siwfood.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe,Long>{

    public List<Recipe> findAllByChef(Chef chef);
}
