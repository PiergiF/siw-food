package it.uniroma3.siw.siwfood.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siwfood.model.Quantity;
import it.uniroma3.siw.siwfood.repository.QuantityRepository;

@Service
public class QuantityService {

    @Autowired
    private QuantityRepository quantityRepository;

    public List<Quantity> findAll() {
        return quantityRepository.findAll();
    }

    public Quantity findById(Long id) {
        return quantityRepository.findById(id).orElse(null);
    }
    public Quantity findByAmount(double amount) {
        return quantityRepository.findByAmount(amount).orElse(null);
    }

    public Quantity save(Quantity quantity) {
        return quantityRepository.save(quantity);
    }
}
