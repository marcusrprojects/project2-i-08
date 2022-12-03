package edu.ncsu.csc.CoffeeMaker.services;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.repositories.IngredientRepository;
import edu.ncsu.csc.CoffeeMaker.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The RecipeService is used to handle CRUD operations on the Recipe model. In
 * addition to all functionality from `Service`, we also have functionality for
 * retrieving a single Recipe by name.
 *
 * @author Kai Presler-Marshall
 */
@Component
@Transactional
public class RecipeService extends Service<Recipe, Long> {

    /**
     * RecipeRepository, to be autowired in by Spring and provide CRUD
     * operations on Recipe model.
     */
    @Autowired
    private RecipeRepository recipeRepository;

    /**
     * IngredientRepository, to be autowired in by Spring and provide CRUD
     * operations on Ingredient model
     */
    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    protected JpaRepository getRepository() {
        return recipeRepository;
    }

    /**
     * Find a recipe with the provided name
     *
     * @param name Name of the recipe to find
     * @return found recipe, null if none
     */
    public Recipe findByName(final String name) {
        return recipeRepository.findByName(name);
    }

    /**
     * Saves a Recipe and all its ingredients to the database
     *
     * @param r The Recipe to save into the database.
     */
    @Override
    public void save(Recipe r) {
        for (Ingredient ingredient : r.getIngredients().keySet()) {
            ingredientRepository.save(ingredient);
        }

        super.save(r);
    }

    /**
     * Saves all the recipes and their ingredients to the database
     *
     * @param objects A List of recipes to save to the database.
     */
    @Override
    public void saveAll(List<Recipe> objects) {
        for (Recipe r : objects) {
            ingredientRepository.saveAll(r.getIngredients().keySet());
        }

        super.saveAll(objects);
    }

    /**
     * Deletes a recipe and all of its ingredients from the database
     *
     * @param r The recipe to delete from the database.
     */
    @Override
    public void delete(Recipe r) {
        Set<Ingredient> ingredients = r.getIngredients().keySet();
        super.delete(r);

        for (Ingredient i : ingredients) {
            ingredientRepository.delete(i);
        }
    }

    /**
     * Deletes all recipes and their ingredients from the database
     */
    @Override
    public void deleteAll() {
        List<Recipe> recipes = recipeRepository.findAll();
        Set<Set<Ingredient>> ingredients = new HashSet<>();
        for (Recipe recipe : recipes) {
            ingredients.add(recipe.getIngredients().keySet());
        }
        super.deleteAll();

        for (Set<Ingredient> ingredientSet : ingredients) {
            for (Ingredient i : ingredientSet) {
                ingredientRepository.delete(i);
            }
        }
    }
}
