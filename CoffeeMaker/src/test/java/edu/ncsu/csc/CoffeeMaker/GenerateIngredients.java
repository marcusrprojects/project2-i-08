package edu.ncsu.csc.CoffeeMaker;

import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;

import javax.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@SpringBootTest(classes = TestConfig.class)
public class GenerateIngredients {

    @Autowired
    private IngredientService ingredientService;

    @Test
    @Transactional
    public void testCreateIngredients() {
        ingredientService.deleteAll();

        final Ingredient i1 = new Ingredient("Coffee");

        ingredientService.save(i1);

        final Ingredient i2 = new Ingredient("Milk");

        ingredientService.save(i2);

        Assertions.assertEquals(2, ingredientService.count());
    }
}