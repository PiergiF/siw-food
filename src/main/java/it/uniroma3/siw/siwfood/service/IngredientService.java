package it.uniroma3.siw.siwfood.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siwfood.model.Ingredient;
import it.uniroma3.siw.siwfood.model.Quantity;
import it.uniroma3.siw.siwfood.model.Recipe;
import it.uniroma3.siw.siwfood.model.Unit;
import it.uniroma3.siw.siwfood.repository.IngredientRepository;
import it.uniroma3.siw.siwfood.repository.QuantityRepository;
import it.uniroma3.siw.siwfood.repository.UnitRepository;

@Service
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private QuantityRepository quantityRepository;

    @Autowired
    private UnitRepository unitRepository;

    public List<Ingredient> findAll() {
        return ingredientRepository.findAll(); // solo con JpaRepository, crud restituisce un Iterable
    }

    public Ingredient findById(Long id) {
        return ingredientRepository.findById(id).orElse(null);
    }

    public Optional<Ingredient> findByName(String name) {
        return ingredientRepository.findByName(name);//.orElse(null);
    }

    public Ingredient save(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    /*
    public Ingredient addIngredientToRecipe(Recipe recipe, String ingredientName, double amount, String unitUnit) {
        Unit unit = unitRepository.findByUnit(unitUnit)
                .orElseThrow(() -> new IllegalArgumentException("Unit not found"));

        // Find existing quantity or create new one if it doesn't exist
        Quantity quantity = quantityRepository.findByAmountAndUnit(amount, unit)
                .orElseGet(() -> {
                    Quantity newQuantity = new Quantity();
                    newQuantity.setAmount(amount);
                    newQuantity.setUnit(unit);
                    return quantityRepository.save(newQuantity);
                });

        Ingredient ingredient = new Ingredient();
        ingredient.setName(ingredientName);
        ingredient.setQuantity(quantity);
        ingredient.setRecipe(recipe);

        return ingredientRepository.save(ingredient);
    }
    */
}
