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
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Recipe extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long    id;

    /** Recipe name */
    private String  name;

    /** Recipe price */
    @Min ( 0 )
    private Integer price;

    /**
     * Map of Recipe Ingredients to amounts
     */
    @ElementCollection
    @JoinColumn(name = "ingredient_name")
    private Map<Ingredient, @Min(0) Integer> ingredients;

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.name = "";
        this.price = 0;
        this.ingredients = new HashMap<>();
    }

    /**
     * Adds a single Ingredient to the Recipe
     * @param ingredient to be added
     * @param amount amount to be added
     */
    public void addIngredient(Ingredient ingredient, Integer amount) {
        this.ingredients.put(ingredient, amount);
    }

    /**
     * Sets the ingredient map to an entirely new map.
     * @param ingredients the map to replace the ingredients with
     */
    public void setIngredients(Map<Ingredient, Integer> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Gets all Ingredients in the Recipe
     * @return map of Ingredients in the Recipe
     */
    public Map<Ingredient, Integer> getIngredients() {
        return this.ingredients;
    }

    /**
     * Gets an Ingredient in the Recipe
     * @param ingredientType the type of the Ingredient desired
     * @return desired Ingredient in the Recipe, or null if it's not in the recipe
     */
    public Map.Entry<Ingredient, Integer> getIngredient(String ingredientType) {

        return this.ingredients.entrySet().stream().filter(
                (ingredient) -> ingredient.getKey().getName().equals(ingredientType)).findAny().orElse(null);
    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns name of the recipe.
     *
     * @return Returns the name.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the recipe name.
     *
     * @param name
     *            The name to set.
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the price of the recipe.
     *
     * @return Returns the price.
     */
    public Integer getPrice () {
        return price;
    }

    /**
     * Sets the recipe price.
     *
     * @param price
     *            The price to set.
     */
    public void setPrice ( final Integer price ) {
        this.price = price;
    }

    /**
     * Updates the fields to be equal to the passed Recipe
     *
     * @param r
     *            with updated fields
     */
    public void updateRecipe(final Recipe r) {
        setPrice(r.getPrice());
        Map<Ingredient, Integer> ingredientIntegerMap = new HashMap<>();

        for (Map.Entry<Ingredient, Integer> i : r.getIngredients().entrySet()) {
            ingredientIntegerMap.put(new Ingredient(i.getKey().getName()), i.getValue());
        }

        setIngredients(ingredientIntegerMap);
    }

    /**
     * Returns the name of the recipe.
     *
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(this.name).append(", Ingredients: {\n");

        List<Ingredient> ingredientList = new ArrayList<>(this.ingredients.keySet());
        Collections.sort(ingredientList);

        for (Ingredient ingredient : ingredientList) {
            builder.append(ingredient.getName()).append(": ").append(this.ingredients.get(ingredient)).append("\n");
        }

        builder.append("}");

        return builder.toString();
    }

    @Override
    public int hashCode () {
        final int prime = 31;
        Integer result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Recipe other = (Recipe) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        }
        else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

}
