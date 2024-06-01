package it.uniroma3.siw.siwfood.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.uniroma3.siw.siwfood.model.Chef;
import it.uniroma3.siw.siwfood.model.Recipe;

public interface ChefRepository extends JpaRepository<Chef,Long>{

    public boolean existsByNameAndSurname(String name, String surname);
    @Query("SELECT r FROM Recipe r WHERE r.chef.id = :chefId")
    public List<Recipe> findAllByChefId(Long chefId);
}
