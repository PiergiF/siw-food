package it.uniroma3.siw.siwfood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.siwfood.model.Recipe;
import it.uniroma3.siw.siwfood.model.Customer;
import it.uniroma3.siw.siwfood.repository.CustomerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The UserService handles logic for Users.
 */
@Service
public class CustomerService {

    @Autowired
    protected CustomerRepository customerRepository;

    /**
     * This method retrieves a User from the DB based on its ID.
     * @param id the id of the User to retrieve from the DB
     * @return the retrieved User, or null if no User with the passed ID could be found in the DB
     */
    @Transactional
    public Customer getCustomer(Long id) {
        Optional<Customer> result = this.customerRepository.findById(id);
        return result.orElse(null);
    }

    /**
     * This method saves a User in the DB.
     * @param customer the customer to save into the DB
     * @return the saved Customer
     * @throws DataIntegrityViolationException if a Customer with the same username
     *                              as the passed Customer already exists in the DB
     */
    @Transactional
    public Customer saveCustomer(Customer customer) {
        return this.customerRepository.save(customer);
    }

    /**
     * This method retrieves all Users from the DB.
     * @return a List with all the retrieved Users
     */
    @Transactional
    public List<Customer> getAllCustomer() {
        List<Customer> result = new ArrayList<>();
        Iterable<Customer> iterable = this.customerRepository.findAll();
        for(Customer customer : iterable)
            result.add(customer);
        return result;
    }

    public Customer findById(Long id){
        return customerRepository.findById(id).orElse(null);
    }

    public void saveRecipeForCustomer(Customer customer, Recipe recipe) {
        customer.getSavedRecipes().add(recipe);
        customerRepository.save(customer);
    }

    public List<Recipe> getSavedRecipes(Customer customer) {
        return customer.getSavedRecipes();
    }

    public void removeRecipeForCustomer(Customer customer, Recipe recipe) {
        customer.getSavedRecipes().remove(recipe);
        customerRepository.save(customer);
    }
}

