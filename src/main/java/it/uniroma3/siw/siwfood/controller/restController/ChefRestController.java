package it.uniroma3.siw.siwfood.controller.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.siwfood.model.Chef;
import it.uniroma3.siw.siwfood.service.ChefService;

@RestController
public class ChefRestController {

    @Autowired
    public ChefService chefService;

    @GetMapping("/rest/allChefs")
    public List<Chef> getAllChefs() {
        return (List<Chef>) this.chefService.findAll();
    }

    @GetMapping("/rest/chef/{id}")
    public Chef getChef(@PathVariable("id") Long id) {
        return this.chefService.findById(id);
    }

}
