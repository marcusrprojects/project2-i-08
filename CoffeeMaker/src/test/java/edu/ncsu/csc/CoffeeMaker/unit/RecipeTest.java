package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.List;

import javax.validation.ConstraintViolationException;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class RecipeTest {

    @Autowired
    private RecipeService service;

    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    @Test
    @Transactional
    public void testAddRecipe () {

        final Recipe r1 = new Recipe();
        r1.setName("Black Coffee");
        r1.setPrice(1);
        r1.addIngredient(new Ingredient("Coffee"), 1);
        service.save(r1);

        final Recipe r2 = new Recipe();
        r2.setName("Mocha");
        r2.setPrice(1);
        r2.addIngredient(new Ingredient("Coffee"), 1);
        r2.addIngredient(new Ingredient("Milk"), 1);
        r2.addIngredient(new Ingredient("Sugar"), 1);
        r2.addIngredient(new Ingredient("Chocolate"), 1);
        service.save(r2);

        final List<Recipe> recipes = service.findAll();
        Assertions.assertEquals( 2, recipes.size(),
                "Creating two recipes should result in two recipes in the database" );

        Assertions.assertEquals( r1, recipes.get( 0 ), "The retrieved recipe should match the created one" );
    }

    @Test
    @Transactional
    public void testNoRecipes () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName("Tasty Drink");
        r1.setPrice(12);
        r1.addIngredient(new Ingredient("Coffee"), -12);

        final Recipe r2 = new Recipe();
        r2.setName("Mocha");
        r2.setPrice(1);
        r2.addIngredient(new Ingredient("Coffee"), 1);
        r2.addIngredient(new Ingredient("Milk"), 1);
        r2.addIngredient(new Ingredient("Sugar"), 1);
        r2.addIngredient(new Ingredient("Chocolate"), 1);

        final List<Recipe> recipes = List.of( r1, r2 );

        try {
            service.saveAll( recipes );
            Assertions.assertEquals( 0, service.count(),
                    "Trying to save a collection of elements where one is invalid should result in neither getting saved" );
        }
        catch ( final Exception e ) {
            Assertions.assertTrue( e instanceof ConstraintViolationException );
        }

    }

    @Test
    @Transactional
    public void testAddRecipe1 () {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, 0 );

        service.save( r1 );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
        Assertions.assertNotNull( service.findByName( name ) );

    }

    /* Test2 is done via the API for different validation */

    @Test
    @Transactional
    public void testAddRecipe3 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, -50, 3, 1, 1, 0 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative price" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe4 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, -3, 1, 1, 2 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of coffee" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe5 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, -1, 1, 2 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of milk" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe6 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, -1, 2 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of sugar" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe7 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, -2 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of chocolate" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe13 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );

        Assertions.assertEquals( 2, service.count(),
                "Creating two recipes should result in two recipes in the database" );

    }

    @Test
    @Transactional
    public void testAddRecipe14 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

    }

    @Test
    @Transactional
    public void testDeleteRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        service.delete( r1 );
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testDeleteRecipe2 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(), "There should be three recipes in the database" );

        service.deleteAll();

        Assertions.assertEquals( 0, service.count(), "`service.deleteAll()` should remove everything" );

    }

    @Test
    @Transactional
    public void testUpdateRecipe() {
        final Recipe r1 = new Recipe();
        r1.setName("Black Coffee");
        r1.setPrice(1);
        r1.addIngredient(new Ingredient("Coffee"), 1);
        service.save(r1);

        final Recipe r2 = new Recipe();
        r2.setName("Mocha");
        r2.setPrice(1);
        r2.addIngredient(new Ingredient("Coffee"), 1);
        r2.addIngredient(new Ingredient("Milk"), 1);
        r2.addIngredient(new Ingredient("Sugar"), 1);
        r2.addIngredient(new Ingredient("Chocolate"), 1);
        service.save(r2);

        r1.updateRecipe(r2);

        for (Integer ingredientAmt : r1.getIngredients().values()) {
                Assertions.assertEquals(1, ingredientAmt);
        }

        Assertions.assertEquals("Black Coffee, Ingredients: {\nChocolate: 1\nCoffee: 1\nMilk: 1\nSugar: 1\n}", r1.toString());

        r1.setName("Mocha");

        service.save(r1);
        Assertions.assertTrue(r1.equals(r2));
        final List<Recipe> recipes = service.findAll();
        Assertions.assertEquals(2, recipes.size(),
                    "Creating two recipes should result in two recipes in the database");
        Assertions.assertEquals(r1, recipes.get(0), "The retrieved recipe should match the created one");
        }

    @Test
    @Transactional
    public void testEditRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );

        r1.setPrice( 70 );

        service.save( r1 );

        final Recipe retrieved = service.findByName("Coffee");

        Assertions.assertEquals(70, (int) retrieved.getPrice());
        Assertions.assertEquals(3, (int) retrieved.getIngredient("Coffee").getValue());
        Assertions.assertEquals(1, (int) retrieved.getIngredient("Milk").getValue());
        Assertions.assertEquals(1, (int) retrieved.getIngredient("Sugar").getValue());

        Assertions.assertEquals(1, service.count(), "Editing a recipe shouldn't duplicate it");
        
        
        final Recipe r2 = new Recipe();
        r2.setName("Mocha");
        r2.setPrice(3);
        r2.addIngredient(new Ingredient("Coffee"), 3);
        r2.addIngredient(new Ingredient("Milk"), 1);
        r2.addIngredient(new Ingredient("Sugar"), 1);
        r2.addIngredient(new Ingredient("Chocolate"), 1);
        service.save(r2);
        r1.updateRecipe(r2);

        Assertions.assertEquals(3, (int) retrieved.getPrice());
        Assertions.assertEquals(3, (int) retrieved.getIngredient("Coffee").getValue());
        Assertions.assertEquals(1, (int) retrieved.getIngredient("Milk").getValue());
        Assertions.assertEquals(1, (int) retrieved.getIngredient("Sugar").getValue());
        Assertions.assertEquals(1, (int) retrieved.getIngredient("Chocolate").getValue());

    }

    private Recipe createRecipe ( final String name, final Integer price, final Integer coffee, final Integer milk,
            final Integer sugar, final Integer chocolate ) {
        final Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setPrice(price);
        recipe.addIngredient(new Ingredient("Coffee"), coffee);
        recipe.addIngredient(new Ingredient("Milk"), milk);
        recipe.addIngredient(new Ingredient("Sugar"), sugar);
        recipe.addIngredient(new Ingredient("Chocolate"), chocolate);

        return recipe;
    }

}
