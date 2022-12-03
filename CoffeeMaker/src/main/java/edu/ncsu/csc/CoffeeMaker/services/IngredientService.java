package edu.ncsu.csc.CoffeeMaker.services;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.repositories.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * The IngredientService is used to handle CRUD operations on the Ingredient
 * model.
 *
 * @author Darien Gillespie
 */
@Component
@Transactional
public class IngredientService extends Service<Ingredient, Long> {

    /**
     * IngredientRepository, to be autowired in by Spring and provide CRUD
     * operations on Ingredient model.
     */
    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    protected JpaRepository getRepository() {
        return this.ingredientRepository;
    }

    /**
     * Find an ingredient with the provided name
     *
     * @param name Name of the ingredient to find
     * @return found ingredient, null if none
     */
    public Ingredient findByName(final String name) {
        return ingredientRepository.findByName(name);
    }
}
