package edu.ncsu.csc.CoffeeMaker.api;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class APITest {

	/**
	 * MockMvc uses Spring's testing framework to handle requests to the REST API
	 */
	private MockMvc mvc;

	@Autowired
	private WebApplicationContext context;

	/**
	 * Sets up the tests.
	 */
	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	@Transactional
	public void testAPI() throws Exception {
		String recipe = mvc.perform(get("/api/v1/recipes")).andDo(print()).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();

		if (!recipe.contains("Mocha")) {
			final Recipe r = new Recipe();
			r.addIngredient(new Ingredient("Chocolate"), 5);
			r.addIngredient(new Ingredient("Coffee"), 3);
			r.addIngredient(new Ingredient("Milk"), 4);
			r.addIngredient(new Ingredient("Sugar"), 8);
			r.setPrice(10);
			r.setName("Mocha");

			mvc.perform(
					post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r)))
					.andExpect(status().isOk());
		}

		recipe = mvc.perform(get("/api/v1/recipes")).andDo(print()).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();

		Assertions.assertTrue(recipe.contains("Mocha"));

		Map<Ingredient, Integer> initIngredients = new HashMap<>();
		initIngredients.put(new Ingredient("Coffee"), 50);
		initIngredients.put(new Ingredient("Milk"), 50);
		initIngredients.put(new Ingredient("Sugar"), 50);
		initIngredients.put(new Ingredient("Chocolate"), 50);

		Inventory inventory = new Inventory(initIngredients);

		mvc.perform(put("/api/v1/inventory").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(inventory))).andExpect(status().isOk());

		mvc.perform(post("/api/v1/makecoffee/mocha").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(350))).andExpect(status().isOk());

		mvc.perform(get("/api/v1/inventory").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(inventory))).andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	public void testIngredient() throws Exception {
		
		Ingredient ingredient1 = new Ingredient("Vanilla");
		
		Ingredient ingredient2 = new Ingredient("Cheese");
		
		Ingredient ingredient3 = new Ingredient("Chocolate");

		Ingredient ingredient4 = new Ingredient("Oatmeal");
		
		mvc.perform(post("/api/v1/ingredients").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(ingredient1))).andExpect(status().isOk());
		
		mvc.perform(post("/api/v1/ingredients").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(ingredient2))).andExpect(status().isOk());
		
		mvc.perform(get("/api/v1/ingredients").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		
		mvc.perform(get("/api/v1/ingredients/Vanilla").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(ingredient1))).andExpect(status().isOk());
				
		mvc.perform(delete("/api/v1/ingredients/Vanilla").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		
		mvc.perform(put("/api/v1/ingredients/Cheese").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(ingredient4))).andExpect(status().isOk());

		mvc.perform(get("/api/v1/ingredients/Oatmeal").contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk());
		
		mvc.perform(post("/api/v1/ingredients").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(ingredient3))).andExpect(status().isOk());
	}

	@Test
	@Transactional
	public void testDeleteRecipe() throws Exception {
		String recipe = mvc.perform(get("/api/v1/recipes")).andDo(print()).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();

		if (!recipe.contains("Mocha")) {
			final Recipe r = new Recipe();
			r.addIngredient(new Ingredient("Chocolate"), 5);
			r.addIngredient(new Ingredient("Coffee"), 3);
			r.addIngredient(new Ingredient("Milk"), 4);
			r.addIngredient(new Ingredient("Sugar"), 8);
			r.setPrice(10);
			r.setName("Mocha");

			mvc.perform(
					post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r)))
					.andExpect(status().isOk());
		}

		recipe = mvc.perform(get("/api/v1/recipes")).andDo(print()).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();

		Assertions.assertTrue(recipe.contains("Mocha"));

		mvc.perform(delete("/api/v1/recipes/Mocha").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString("Mocha"))).andDo(print()).andExpect(status().isOk());

		recipe = mvc.perform(get("/api/v1/recipes")).andDo(print()).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();

		Assertions.assertFalse(recipe.contains("Mocha"));

		mvc.perform(delete("/api/v1/recipes/Mocha").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString("Mocha"))).andDo(print()).andExpect(status().isNotFound());

		recipe = mvc.perform(get("/api/v1/recipes/Mocha")).andDo(print()).andExpect(status().isNotFound()).andReturn()
				.getResponse().getContentAsString();

		Assertions.assertTrue(recipe.contains("failed"));
	}
	
}
