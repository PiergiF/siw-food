package it.uniroma3.siw.siwfood.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siwfood.model.Unit;

public interface UnitRepository extends JpaRepository<Unit,Long>{

    //Optional<Unit> findByUnit(String name);
    public Optional<Unit> findByName(String name);
}
