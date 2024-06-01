package it.uniroma3.siw.siwfood.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siwfood.model.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient,Long>{
    public Optional<Ingredient> findByName(String name);
    public boolean existsByName(String name);
}
