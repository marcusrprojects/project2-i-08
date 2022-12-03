package edu.ncsu.csc.CoffeeMaker.models;


import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {

    /**
     * id for inventory entry
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Map of Ingredients in inventory to their amounts
     */
    @ElementCollection
    @JoinColumn(name = "ingredient_name")
    private Map<Ingredient, @Min(0) Integer> ingredients;

    /**
     * Empty constructor for Hibernate
     */
    public Inventory() {
        this.ingredients = new HashMap<>();
        // Intentionally empty so that Hibernate can instantiate
        // Inventory object.
    }

    /**
     * Use this to create inventory with specified amts.
     *
     * @param ingredients the initial ingredients in the inventory
     */
    public Inventory(Map<Ingredient, Integer> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id the ID
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Returns the current number of units of the requested ingredient in the inventory
     *
     * @param ingredientType the type of ingredient to fetch
     * @return amount of the requested ingredient
     */
    public Integer getIngredient(String ingredientType) {
        for (Ingredient ingredient : this.ingredients.keySet()) {
            if (ingredient.getName().equals(ingredientType)) {
                return this.ingredients.get(ingredient);
            }
        }

        return 0;
    }

    /**
     * Returns the entire map of Ingredients in the inventory
     *
     * @return the map of Ingredients
     */
    public Map<Ingredient, Integer> getIngredients() {
        return this.ingredients;
    }

    /**
     * Sets the number of units of the requested ingredient in the inventory to the specified amount.
     *
     * @param ingredientType the type of the ingredient to set
     * @param amt            amount of ingredient to set
     */
    public void setIngredient(String ingredientType, Integer amt) {
        for (Ingredient ingredient : this.ingredients.keySet()) {
            if (ingredient.getName().equals(ingredientType)) {
                this.ingredients.put(ingredient, amt);
                return;
            }
        }

        this.ingredients.put(new Ingredient(ingredientType), amt);
    }

    /**
     * Sets an entire new map of Ingredients in the inventory, replacing the existing one.
     *
     * @param ingredients the map to replace it with
     */
    public void setIngredients(Map<Ingredient, Integer> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Checks that the number of units of the to add is a valid, non-negative amount
     * of units of that ingredient.
     *
     * @param ingredientAmt amount to add
     * @return checked amount of ingredient
     * @throws IllegalArgumentException if the parameter isn't a positive integer or the Ingredient
     *                                  isn't in the inventory
     */
    public Integer checkIngredient(final String ingredientAmt) {
        Integer amtIngredient = 0;
        try {
            amtIngredient = Integer.parseInt(ingredientAmt);
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException("Units of ingredient must be a positive integer");
        }
        if (amtIngredient < 0) {
            throw new IllegalArgumentException("Units of ingredient must be a positive integer");
        }

        return amtIngredient;
    }

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients(final Recipe r) {

        for (Map.Entry<Ingredient, Integer> ingredient : r.getIngredients().entrySet()) {
            if (getIngredient(ingredient.getKey().getName()) < ingredient.getValue()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param r recipe to make
     * @return true if recipe is made.
     */
    public boolean useIngredients(final Recipe r) {
        if (enoughIngredients(r)) {
            for (Map.Entry<Ingredient, Integer> ingredient : r.getIngredients().entrySet()) {
                if (getIngredient(ingredient.getKey().getName()) != null) {
                    setIngredient(ingredient.getKey().getName(),
                            getIngredient(ingredient.getKey().getName()) - ingredient.getValue());
                } else {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds ingredients to the inventory
     *
     * @param ingredients map of ingredients to add to amount to add
     * @return true if successful, false if not
     */
    public boolean addIngredients(Map<Ingredient, Integer> ingredients) {
        for (Integer amount : ingredients.values()) {
            if (amount < 0) {
                throw new IllegalArgumentException("Amount cannot be negative");
            }
        }

        for (Map.Entry<Ingredient, Integer> ingredient : ingredients.entrySet()) {
            setIngredient(ingredient.getKey().getName(),
                    getIngredient(ingredient.getKey().getName()) + ingredient.getValue());
        }

        return true;
    }

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString() {
        final StringBuffer buf = new StringBuffer();

        List<Ingredient> ingredientList = new ArrayList<>(this.ingredients.keySet());
        Collections.sort(ingredientList);

        for (Ingredient ingredient : ingredientList) {
            buf.append(ingredient.getName()).append(": ").append(this.ingredients.get(ingredient)).append("\n");
        }

        return buf.toString();
    }

}
