package it.uniroma3.siw.siwfood.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siwfood.model.Chef;
import it.uniroma3.siw.siwfood.model.Courses;
import it.uniroma3.siw.siwfood.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe,Long>{

    public List<Recipe> findAllByChef(Chef chef);

    public List<Recipe> findByNameContainingIgnoreCase(String name);

    public List<Recipe> findAllByCourse(Courses course);

    /*
    @Query("SELECT r FROM Recipe r JOIN r.ingredients i WHERE i.name LIKE %?1%")
    public List<Recipe> findByIngredientName(String ingredientName);
    */
}
