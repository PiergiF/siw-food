package it.uniroma3.siw.siwfood.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siwfood.model.Credentials;
import jakarta.transaction.Transactional;

import java.util.List;


public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

    public Optional<Credentials>findByUsername(String username);

    @Modifying
    @Transactional
    @Query("DELETE FROM Credentials c WHERE c.chef.id = :chefId")
    public void deleteByChefId(Long chefId);
}