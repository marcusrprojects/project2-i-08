package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class APIRecipeTest {

    @Autowired
    private RecipeService service;

    @Autowired
    private MockMvc mvc;

    @Test
    @Transactional
    public void ensureRecipe() throws Exception {
        service.deleteAll();

        final Recipe r = new Recipe();
        r.addIngredient(new Ingredient("Chocolate"), 5);
        r.addIngredient(new Ingredient("Coffee"), 3);
        r.addIngredient(new Ingredient("Milk"), 4);
        r.addIngredient(new Ingredient("Sugar"), 8);
        r.setPrice(10);
        r.setName("Mocha");

        mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(r))).andExpect(status().isOk());

    }
    
    @Test
    @Transactional
    public void editRecipe() throws Exception {
        service.deleteAll();

        final Recipe r = new Recipe();
        r.setName("Mocha");
        r.addIngredient(new Ingredient("Chocolate"), 10);
        r.addIngredient(new Ingredient("Milk"), 20);
        r.addIngredient(new Ingredient("Sugar"), 5);
        r.addIngredient(new Ingredient("Coffee"), 1);

        r.setPrice(5);

        
        final Recipe r2 = new Recipe();
        r2.addIngredient(new Ingredient("Coffee"), 2);
        r2.addIngredient(new Ingredient("Milk"), 2);
        r2.addIngredient(new Ingredient("Sugar"), 2);
        r2.setPrice(8);
        r2.setName("Latte");
        
        mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(r))).andExpect(status().isOk());
        
        mvc.perform(post("/api/v1/recipe").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(r2)));
        
        r.setPrice(15);
        
        mvc.perform(put("/api/v1/editrecipe/Mocha").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(r))).andExpect(status().isOk());
        
        r.setPrice(-1);
        
        mvc.perform(put("/api/v1/editrecipe/Mocha").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(r))).andExpect(status().isConflict());
        
        r.setPrice(15);
        
        Map<Ingredient, Integer> map = new HashMap<>();
        
        r.setIngredients(map);
        
        mvc.perform(put("/api/v1/editrecipe/Mocha").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(r))).andExpect(status().isConflict());
        
        r.addIngredient(new Ingredient("Coffee"), 2);
        r.addIngredient(new Ingredient("Milk"), 2);
        r.addIngredient(new Ingredient("Sugar"), 2);
        
        mvc.perform(put("/api/v1/editrecipe/null").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(r))).andExpect(status().isNotFound());
        
        
//        mvc.perform(get("/api/v1/editrecipe/Mocha").contentType(MediaType.APPLICATION_JSON))
//		.andExpect(status().isOk());
        
        
        
        List<Recipe> recList = new ArrayList<>();
        recList.add(r);
        recList.add(r2);
        
        mvc.perform(get("/api/v1/editrecipe").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(recList))).andExpect(status().isOk());
        
        mvc.perform(put("/api/v1/editrecipe/Mocha").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(r))).andExpect(status().isOk());
        
        mvc.perform(get("/api/v1/editrecipe/Mocha").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(r))).andExpect(status().isOk());
        
        
        
        
        
        
        
        
        
//        Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
//
//
//        Assertions.assertEquals(3, service.count(),
//                "Creating three recipes should result in three recipes in the database");
//
//        final Recipe r4 = createRecipe("Hot Chocolate", 75, 0, 2, 1, 2);
//
//        mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON)
//                .content(TestUtils.asJsonString(r4))).andExpect(status().isInsufficientStorage());
//
//        Assertions.assertEquals(3, service.count(), "Creating a fourth recipe should not get saved");

    }


    @Test
    @Transactional
    public void testRecipeAPI() throws Exception {

        service.deleteAll();

        final Recipe recipe = new Recipe();
        recipe.setName("Delicious Not-Coffee");
        recipe.addIngredient(new Ingredient("Chocolate"), 10);
        recipe.addIngredient(new Ingredient("Milk"), 20);
        recipe.addIngredient(new Ingredient("Sugar"), 5);
        recipe.addIngredient(new Ingredient("Coffee"), 1);

        recipe.setPrice(5);

        mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(recipe)));

        Assertions.assertEquals(1, (int) service.count());

    }

    @Test
    @Transactional
    public void testAddRecipe2() throws Exception {
        service.deleteAll();

        /* Tests a recipe with a duplicate name to make sure it's rejected */

        Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
        final String name = "Coffee";
        final Recipe r1 = createRecipe(name, 50, 3, 1, 1, 0);

        service.save(r1);

        final Recipe r2 = createRecipe(name, 50, 3, 1, 1, 0);
        mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(r2))).andExpect(status().is4xxClientError());

        Assertions.assertEquals(1, service.findAll().size(), "There should only one recipe in the CoffeeMaker");
    }

    @Test
    @Transactional
    public void testAddRecipe15() throws Exception {
        service.deleteAll();

        /* Tests to make sure that our cap of 3 recipes is enforced */

        Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");

        final Recipe r1 = createRecipe("Coffee", 50, 3, 1, 1, 0);
        service.save(r1);
        final Recipe r2 = createRecipe("Mocha", 50, 3, 1, 1, 2);
        service.save(r2);
        final Recipe r3 = createRecipe("Latte", 60, 3, 2, 2, 0);
        service.save(r3);

        Assertions.assertEquals(3, service.count(),
                "Creating three recipes should result in three recipes in the database");

        final Recipe r4 = createRecipe("Hot Chocolate", 75, 0, 2, 1, 2);

        mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(r4))).andExpect(status().isInsufficientStorage());

        Assertions.assertEquals(3, service.count(), "Creating a fourth recipe should not get saved");
    }

    private Recipe createRecipe(final String name, final Integer price, final Integer coffee, final Integer milk,
                                final Integer sugar, final Integer chocolate) {
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
