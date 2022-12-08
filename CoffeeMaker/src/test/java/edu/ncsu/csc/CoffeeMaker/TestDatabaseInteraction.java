package edu.ncsu.csc.CoffeeMaker;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ExtendWith (SpringExtension.class)
@EnableAutoConfiguration
@SpringBootTest (classes = TestConfig.class)
public class TestDatabaseInteraction {
    @Autowired
    private RecipeService recipeService;

    @Test
    @Transactional
    public void testRecipes(){
        recipeService.deleteAll();

        Recipe r = new Recipe();

        r.setPrice(350);
        r.setName("Mocha");
        r.addIngredient(new Ingredient("Coffee"), 2);
        r.addIngredient(new Ingredient("Sugar"), 1);
        r.addIngredient(new Ingredient("Milk"), 1);
        r.addIngredient(new Ingredient("Chocolate"), 1);

        recipeService.save(r);

        List<Recipe> dbRecipes = recipeService.findAll();

        Assertions.assertEquals(1, dbRecipes.size());

        Recipe dbRecipe = dbRecipes.get(0);

        Assertions.assertEquals(r.getName(), dbRecipe.getName());

        Assertions.assertEquals("Mocha", r.getName());
        Assertions.assertEquals(350, r.getPrice());

        Recipe updateRecipe = new Recipe();
        updateRecipe.addIngredient(new Ingredient("Coffee"), 2);
        updateRecipe.addIngredient(new Ingredient("Sugar"), 12);
        updateRecipe.addIngredient(new Ingredient("Milk"), 1);
        updateRecipe.addIngredient(new Ingredient("Chocolate"), 1);

        dbRecipe.updateRecipe(updateRecipe);
        dbRecipe.setPrice(15);
        recipeService.save(dbRecipe);


        Assertions.assertEquals(1, recipeService.count());

        Assertions.assertEquals(15, (int) recipeService.findAll().get(0).getPrice());
        Assertions.assertEquals(12, (int) recipeService.findAll().get(0).getIngredient("Sugar").getValue());
        
        Recipe r2 = new Recipe();

        r2.setPrice(300);
        r2.setName("Hot Chocolate");
        r2.addIngredient(new Ingredient("Sugar"), 2);
        r2.addIngredient(new Ingredient("Milk"), 2);
        r2.addIngredient(new Ingredient("Chocolate"), 4);

        recipeService.save(r2);
        
        Recipe r3 = new Recipe();

        r3.setPrice(200);
        r3.setName("Black Coffee");
        r3.addIngredient(new Ingredient("Coffee"), 2);

        recipeService.save(r3);
        
        dbRecipes = recipeService.findAll();

        Assertions.assertEquals(3, dbRecipes.size());

        Recipe dbRecipe2 = dbRecipes.get(1);
        
        recipeService.delete(dbRecipe2);
        
        dbRecipes = recipeService.findAll();
        Assertions.assertEquals(2, dbRecipes.size());
        
        Assertions.assertEquals(200, (int) recipeService.findAll().get(1).getPrice());
        Assertions.assertEquals("Mocha", recipeService.findAll().get(0).getName());
        
    	recipeService.delete(dbRecipe2);
    	// Should not fail, but should not do anything.
    	
        dbRecipes = recipeService.findAll();
        Assertions.assertEquals(2, dbRecipes.size());
        
        Assertions.assertEquals(200, (int) recipeService.findAll().get(1).getPrice());
        Assertions.assertEquals("Mocha", recipeService.findAll().get(0).getName());
        
        recipeService.deleteAll();
        
        Assertions.assertEquals(0, (int) recipeService.count());
        
    }
}
