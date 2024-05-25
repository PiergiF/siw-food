package it.uniroma3.siw.siwfood.service;

import org.springframework.beans.factory.annotation.Autowired;

import it.uniroma3.siw.siwfood.repository.RecipeRepository;

public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;
}
