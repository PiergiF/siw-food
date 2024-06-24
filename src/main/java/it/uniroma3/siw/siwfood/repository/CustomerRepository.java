package it.uniroma3.siw.siwfood.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siwfood.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long>{

}
