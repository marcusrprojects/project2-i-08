package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

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

    /** Amount coffee */
    @Min ( 0 )
    private Integer coffee;

    /** Amount milk */
    @Min ( 0 )
    private Integer milk;

    /** Amount sugar */
    @Min ( 0 )
    private Integer sugar;

    /** Amount chocolate */
    @Min ( 0 )
    private Integer chocolate;

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.name = "";
    }

    /**
     * Check if all ingredient fields in the recipe are 0
     *
     * @return true if all ingredient fields are 0, otherwise return false
     */
    public boolean checkRecipe () {
        return coffee == 0 && milk == 0 && sugar == 0 && chocolate == 0;
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
     * Returns amount of chocolate in the recipe.
     *
     * @return Returns the amtChocolate.
     */
    public Integer getChocolate () {
        return chocolate;
    }

    /**
     * Sets the amount of chocolate in the recipe.
     *
     * @param chocolate
     *            The amtChocolate to set.
     */
    public void setChocolate ( final Integer chocolate ) {
        this.chocolate = chocolate;
    }

    /**
     * Returns amount of coffee in the recipe.
     *
     * @return Returns the amtCoffee.
     */
    public Integer getCoffee () {
        return coffee;
    }

    /**
     * Sets the amount of coffee in the recipe.
     *
     * @param coffee
     *            The amtCoffee to set.
     */
    public void setCoffee ( final Integer coffee ) {
        this.coffee = coffee;
    }

    /**
     * Returns amount of milk in the recipe.
     *
     * @return Returns the amtMilk.
     */
    public Integer getMilk () {
        return milk;
    }

    /**
     * Sets the amount of milk in the recipe.
     *
     * @param milk
     *            The amtMilk to set.
     */
    public void setMilk ( final Integer milk ) {
        this.milk = milk;
    }

    /**
     * Returns amount of sugar in the recipe.
     *
     * @return Returns the amtSugar.
     */
    public Integer getSugar () {
        return sugar;
    }

    /**
     * Sets the amount of sugar in the recipe.
     *
     * @param sugar
     *            The amtSugar to set.
     */
    public void setSugar ( final Integer sugar ) {
        this.sugar = sugar;
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
    public void updateRecipe ( final Recipe r ) {
        setChocolate( r.getChocolate() );
        setCoffee( r.getCoffee() );
        setMilk( r.getMilk() );
        setSugar( r.getSugar() );
        setPrice( r.getPrice() );
    }

    /**
     * Returns the name of the recipe.
     *
     * @return String
     */
    @Override
    public String toString () {
        return name;
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
