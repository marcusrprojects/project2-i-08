package edu.ncsu.csc.CoffeeMaker.models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Ingredient for the coffee maker. Ingredient is tied to the database using Hibernate
 * libraries. See IngredientRepository and IngredientService for the other two pieces
 * used for database support.
 *
 * @author Darien Gillespie
 */
@Entity
public class Ingredient extends DomainObject implements Comparable<Ingredient> {

    /**
     * Ingredient id
     */
    @Id
    @GeneratedValue
    private long id;

    /**
     * Ingredient name
     */
    private String name;

    /**
     * Creates a default Ingredient for the CoffeeMaker
     */
    public Ingredient() {
        this.name = "";
    }

    /**
     * Creates a specific Ingredient for the CoffeeMaker
     *
     * @param name type of the ingredient to create
     */
    public Ingredient(String name) {
        this.name = name;
    }

    /**
     * Gets the type of the Ingredient
     *
     * @return the ingredient type
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the type of the Ingredient
     *
     * @param name type to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Serializable getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Ingredient i) {
        return this.name.compareTo(i.getName());
    }
}
