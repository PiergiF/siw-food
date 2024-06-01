package it.uniroma3.siw.siwfood.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siwfood.model.Quantity;
import it.uniroma3.siw.siwfood.model.Unit;

public interface QuantityRepository extends JpaRepository<Quantity,Long>{

    //Optional<Quantity> findByAmountAndUnit(double amount, Unit unit);
    public Optional<Quantity> findByAmount(double amount);
}
