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
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@SpringBootTest(classes = TestConfig.class)
public class IngredientTest {

    @Autowired
    private IngredientService service;

    @BeforeEach
    public void setup() {
        service.deleteAll();
    }

    @Test
    @Transactional
    public void testAddIngredient() {

        final Ingredient i1 = new Ingredient();
        i1.setName("Skim Milk");
        service.save(i1);

        final Ingredient i2 = new Ingredient();
        i1.setName("Almond Milk");
        service.save(i2);

        final List<Ingredient> ingredients = service.findAll();
        Assertions.assertEquals(2, ingredients.size(),
                "Creating two ingredients should result in two ingredients in the database");

        Assertions.assertEquals(i1, ingredients.get(0), "The retrieved ingredient should match the created one");
    }


    @Test
    @Transactional
    public void testAddIngredient1() {

        Assertions.assertEquals(0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker");
        final Ingredient i1 = new Ingredient();
        String name = "Whole Milk";
        i1.setName(name);
        service.save(i1);

        Assertions.assertEquals(1, service.findAll().size(), "There should only one ingredient in the CoffeeMaker");
        Assertions.assertNotNull(service.findByName(name));
        
        Ingredient i1_retrieved = service.findByName(name);
        Assertions.assertEquals(i1, i1_retrieved,
        		"Finding the skim milk ingredient by name should have retrieved the ingredient from the service, but did not.");

    }

    @Test
    @Transactional
    public void testDeleteIngredient() {
        Assertions.assertEquals(0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker");

        final Ingredient i1 = new Ingredient();
        i1.setName("2% Milk");
        service.save(i1);

        Assertions.assertEquals(1, service.count(), "There should be one ingredient in the database");

        service.delete(i1);
        Assertions.assertEquals(0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker");
    }

}
