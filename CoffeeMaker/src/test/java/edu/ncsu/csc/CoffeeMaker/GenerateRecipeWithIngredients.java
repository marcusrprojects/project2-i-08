package edu.ncsu.csc.CoffeeMaker;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.models.DomainObject;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@SpringBootTest(classes = TestConfig.class)
public class GenerateRecipeWithIngredients {

    @Autowired
    private RecipeService recipeService;

    @BeforeEach
    public void setup() {
        recipeService.deleteAll();
    }


    @Test
    @Transactional
    public void createRecipe() {
        final Recipe r1 = new Recipe();
        r1.setName("Delicious Coffee");

        r1.setPrice(50);

        r1.addIngredient(new Ingredient("Coffee"), 10);
        r1.addIngredient(new Ingredient("Pumpkin Spice"), 3);
        r1.addIngredient(new Ingredient("Milk"), 2);

        recipeService.save(r1);

        Assertions.assertEquals(1, recipeService.count());
        
        Long r1_id = r1.getId();
        Assertions.assertTrue(recipeService.existsById(r1_id),
        		"A recipe by the id of r1 should exist, but it does not.");
        
        Recipe r1_found = recipeService.findById(r1_id);
        Assertions.assertEquals(r1, r1_found,
                "Finding a recipe by the id of r1 should have returned r1, but did not.");
        Recipe r2_not_found = recipeService.findById(r1_id + 1);
        Assertions.assertNull(r2_not_found,
        		"Finding a recipe by the id of one greater than that of r1 should have returned null, but did not.");

        printRecipes();
    }

    private void printRecipes() {
        for (DomainObject r : recipeService.findAll()) {
            System.out.println(r);
        }
    }

}