package it.uniroma3.siw.siwfood.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.uniroma3.siw.siwfood.model.Chef;

public interface ChefRepository extends JpaRepository<Chef,Long>{

    public boolean existsByNameAndSurname(String name, String surname);
}
