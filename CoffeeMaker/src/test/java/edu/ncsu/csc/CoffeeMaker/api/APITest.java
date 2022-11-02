package edu.ncsu.csc.CoffeeMaker.api;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
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
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
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
        String recipe = mvc.perform(get("/api/v1/recipes")).andDo(print()).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        if (!recipe.contains("Mocha")) {
            final Recipe r = new Recipe();
            r.setChocolate(5);
            r.setCoffee(3);
            r.setMilk(4);
            r.setSugar(8);
            r.setPrice(10);
            r.setName("Mocha");

            mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.asJsonString(r))).andExpect(status().isOk());
        }

        recipe = mvc.perform(get("/api/v1/recipes")).andDo(print()).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertTrue(recipe.contains("Mocha"));

        Inventory inventory = new Inventory(50, 50, 50, 50);

        mvc.perform(put("/api/v1/inventory").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(inventory))).andExpect(status().isOk());

        mvc.perform(post("/api/v1/makecoffee/mocha").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(350))).andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testDeleteRecipe() throws Exception {
        String recipe = mvc.perform(get("/api/v1/recipes")).andDo(print()).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        if (!recipe.contains("Mocha")) {
            final Recipe r = new Recipe();
            r.setChocolate(5);
            r.setCoffee(3);
            r.setMilk(4);
            r.setSugar(8);
            r.setPrice(10);
            r.setName("Mocha");

            mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.asJsonString(r))).andExpect(status().isOk());
        }

        recipe = mvc.perform(get("/api/v1/recipes")).andDo(print()).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertTrue(recipe.contains("Mocha"));

        mvc.perform(delete("/api/v1/recipes/Mocha").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString("Mocha"))).andDo(print()).andExpect(status().isOk());

        recipe = mvc.perform(get("/api/v1/recipes")).andDo(print()).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertFalse(recipe.contains("Mocha"));

        mvc.perform(delete("/api/v1/recipes/Mocha").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString("Mocha"))).andDo(print()).andExpect(status().isNotFound());

        recipe = mvc.perform(get("/api/v1/recipes/Mocha")).andDo(print()).andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertTrue(recipe.contains("failed"));
    }
}
