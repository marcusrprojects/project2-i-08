package edu.ncsu.csc.CoffeeMaker.unit;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import org.junit.jupiter.api.Assertions;
import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@SpringBootTest(classes = TestConfig.class)
public class InventoryTest {

    @Autowired
    private InventoryService inventoryService;

    @BeforeEach
    public void setup() {
        final Inventory ivt = inventoryService.getInventory();

        ivt.setIngredient("Chocolate", 500);
        ivt.setIngredient("Coffee", 500);
        ivt.setIngredient("Milk", 500);
        ivt.setIngredient("Sugar", 500);

        inventoryService.save(ivt);
    }

    @Test
    @Transactional
    public void testConsumeInventory() {
        final Inventory i = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName("Delicious Not-Coffee");
        recipe.addIngredient(new Ingredient("Chocolate"), 10);
        recipe.addIngredient(new Ingredient("Milk"), 20);
        recipe.addIngredient(new Ingredient("Sugar"), 5);
        recipe.addIngredient(new Ingredient("Coffee"), 1);

        recipe.setPrice(5);

        i.useIngredients(recipe);

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        Assertions.assertEquals(490, (int) i.getIngredient("Chocolate"));
        Assertions.assertEquals(480, (int) i.getIngredient("Milk"));
        Assertions.assertEquals(495, (int) i.getIngredient("Sugar"));
        Assertions.assertEquals(499, (int) i.getIngredient("Coffee"));
    }

    @Test
    @Transactional
    public void testAddInventory1() {
        Inventory ivt = inventoryService.getInventory();

        Map<Ingredient, Integer> addedIngredients = new HashMap<>();
        addedIngredients.put(new Ingredient("Coffee"), 5);
        addedIngredients.put(new Ingredient("Milk"), 3);
        addedIngredients.put(new Ingredient("Sugar"), 7);
        addedIngredients.put(new Ingredient("Chocolate"), 2);
        ivt.addIngredients(addedIngredients);

        /* Save and retrieve again to update with DB */
        inventoryService.save(ivt);

        ivt = inventoryService.getInventory();

        Assertions.assertEquals(505, (int) ivt.getIngredient("Coffee"),
                "Adding to the inventory should result in correctly-updated values for coffee");
        Assertions.assertEquals(503, (int) ivt.getIngredient("Milk"),
                "Adding to the inventory should result in correctly-updated values for milk");
        Assertions.assertEquals(507, (int) ivt.getIngredient("Sugar"),
                "Adding to the inventory should result in correctly-updated values sugar");
        Assertions.assertEquals(502, (int) ivt.getIngredient("Chocolate"),
                "Adding to the inventory should result in correctly-updated values chocolate");

    }

    @Test
    @Transactional
    public void testAddInventory2() {
        final Inventory ivt = inventoryService.getInventory();

        try {
            Map<Ingredient, Integer> addedIngredients = new HashMap<>();
            addedIngredients.put(new Ingredient("Coffee"), -5);
            addedIngredients.put(new Ingredient("Milk"), 3);
            addedIngredients.put(new Ingredient("Sugar"), 7);
            addedIngredients.put(new Ingredient("Chocolate"), 2);
            ivt.addIngredients(addedIngredients);
            Assertions.fail("Trying to update Inventory with an invalid value for coffee should throw an iae, but did not.");
        } catch (final IllegalArgumentException iae) {
            Assertions.assertEquals(500, (int) ivt.getIngredient("Coffee"),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee");
            Assertions.assertEquals(500, (int) ivt.getIngredient("Milk"),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk");
            Assertions.assertEquals(500, (int) ivt.getIngredient("Sugar"),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar");
            Assertions.assertEquals(500, (int) ivt.getIngredient("Chocolate"),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate");
        }
    }

    @Test
    @Transactional
    public void testAddInventory3() {
        final Inventory ivt = inventoryService.getInventory();

        try {
            Map<Ingredient, Integer> addedIngredients = new HashMap<>();
            addedIngredients.put(new Ingredient("Coffee"), 5);
            addedIngredients.put(new Ingredient("Milk"), -3);
            addedIngredients.put(new Ingredient("Sugar"), 7);
            addedIngredients.put(new Ingredient("Chocolate"), 2);
            ivt.addIngredients(addedIngredients);
            Assertions.fail("Trying to update Inventory with an invalid value for milk should throw an iae, but did not.");
        } catch (final IllegalArgumentException iae) {
            Assertions.assertEquals(500, (int) ivt.getIngredient("Coffee"),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- coffee");
            Assertions.assertEquals(500, (int) ivt.getIngredient("Milk"),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- milk");
            Assertions.assertEquals(500, (int) ivt.getIngredient("Sugar"),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- sugar");
            Assertions.assertEquals(500, (int) ivt.getIngredient("Chocolate"),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- chocolate");

        }

    }

    @Test
    @Transactional
    public void testAddInventory4() {
        final Inventory ivt = inventoryService.getInventory();

        try {
            Map<Ingredient, Integer> addedIngredients = new HashMap<>();
            addedIngredients.put(new Ingredient("Coffee"), 5);
            addedIngredients.put(new Ingredient("Milk"), 3);
            addedIngredients.put(new Ingredient("Sugar"), -7);
            addedIngredients.put(new Ingredient("Chocolate"), 2);
            ivt.addIngredients(addedIngredients);
            Assertions.fail("Trying to update Inventory with an invalid value for sugar should throw an iae, but did not.");
        } catch (final IllegalArgumentException iae) {
            Assertions.assertEquals(500, (int) ivt.getIngredient("Coffee"),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- coffee");
            Assertions.assertEquals(500, (int) ivt.getIngredient("Milk"),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- milk");
            Assertions.assertEquals(500, (int) ivt.getIngredient("Sugar"),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- sugar");
            Assertions.assertEquals(500, (int) ivt.getIngredient("Chocolate"),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- chocolate");

        }

    }

    @Test
    @Transactional
    public void testChecks() {

        final Inventory ivt = inventoryService.getInventory();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ivt.checkIngredient("-1");
        }, "Cannot add negative amount of an ingredient");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ivt.checkIngredient("negative one");
        }, "Cannot add a string that does not parse into an integer");
    }

    @Test
    @Transactional
    public void testAddInventory5() {
        final Inventory ivt = inventoryService.getInventory();

        try {
            Map<Ingredient, Integer> addedIngredients = new HashMap<>();
            addedIngredients.put(new Ingredient("Coffee"), 5);
            addedIngredients.put(new Ingredient("Milk"), 3);
            addedIngredients.put(new Ingredient("Sugar"), 7);
            addedIngredients.put(new Ingredient("Chocolate"), -2);
            ivt.addIngredients(addedIngredients);
            Assertions.fail("Trying to update Inventory with an invalid value for chocolate should throw an iae, but did not.");
        } catch (final IllegalArgumentException iae) {
            Assertions.assertEquals(500, (int) ivt.getIngredient("Coffee"),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- coffee");
            Assertions.assertEquals(500, (int) ivt.getIngredient("Milk"),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- milk");
            Assertions.assertEquals(500, (int) ivt.getIngredient("Sugar"),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- sugar");
            Assertions.assertEquals(500, (int) ivt.getIngredient("Chocolate"),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- chocolate");

        }

    }

    @Test
    @Transactional
    public void testCheckInventory() {
        final Inventory ivt = inventoryService.getInventory();
        Map<Ingredient, Integer> addedIngredients = new HashMap<>();
        addedIngredients.put(new Ingredient("Coffee"), 5);
        addedIngredients.put(new Ingredient("Milk"), 3);
        addedIngredients.put(new Ingredient("Sugar"), 7);
        addedIngredients.put(new Ingredient("Chocolate"), 2);
        ivt.addIngredients(addedIngredients);
        ivt.setId((long) (2));
        Assertions.assertEquals((long) 2, (long) ivt.getId());
        // Chocolate
        try {
            ivt.setIngredient("Chocolate", ivt.checkIngredient("-5"));
            Assertions.fail("should fail as cannot set to a negative int");
        } catch (IllegalArgumentException e) {
            ivt.setIngredient("Chocolate", ivt.checkIngredient("5"));
        }
        try {
            ivt.setIngredient("Chocolate", ivt.checkIngredient("five"));
            Assertions.fail("should fail as cannot set to a string");
        } catch (IllegalArgumentException e) {
            ivt.setIngredient("Chocolate", ivt.checkIngredient("5"));
        }
        Assertions.assertEquals(5, (int) ivt.getIngredient("Chocolate"));
        // Coffee
        try {
            ivt.setIngredient("Coffee", ivt.checkIngredient("-5"));
            Assertions.fail("should fail as cannot set to a negative int");
        } catch (IllegalArgumentException e) {
            ivt.setIngredient("Coffee", ivt.checkIngredient("5"));
        }
        try {
            ivt.setIngredient("Coffee", ivt.checkIngredient("five"));
            Assertions.fail("should fail as cannot set to a string");
        } catch (IllegalArgumentException e) {
            ivt.setIngredient("Coffee", ivt.checkIngredient("5"));
        }
        Assertions.assertEquals(5, (int) ivt.getIngredient("Coffee"));
        // Milk
        try {
            ivt.setIngredient("Milk", ivt.checkIngredient("-5"));
            Assertions.fail("should fail as cannot set to a negative int");
        } catch (IllegalArgumentException e) {
            ivt.setIngredient("Milk", ivt.checkIngredient("5"));
        }
        try {
            ivt.setIngredient("Milk", ivt.checkIngredient("five"));
            Assertions.fail("should fail as cannot set to a string");
        } catch (IllegalArgumentException e) {
            ivt.setIngredient("Milk", ivt.checkIngredient("5"));
        }
        Assertions.assertEquals(5, (int) ivt.getIngredient("Milk"));
        // Sugar
        try {
            ivt.setIngredient("Sugar", ivt.checkIngredient("-5"));
            Assertions.fail("should fail as cannot set to a negative int");
        } catch (IllegalArgumentException e) {
            ivt.setIngredient("Sugar", ivt.checkIngredient("5"));
        }
        try {
            ivt.setIngredient("Sugar", ivt.checkIngredient("five"));
            Assertions.fail("should fail as cannot set to a string");
        } catch (IllegalArgumentException e) {
            ivt.setIngredient("Sugar", ivt.checkIngredient("5"));
        }
        Assertions.assertEquals(5, (int) ivt.getIngredient("Sugar"));
    }

    @Test
    @Transactional
    public void testToString() {
        final Inventory ivt = inventoryService.getInventory();
        Map<Ingredient, Integer> addedIngredients = new HashMap<>();
        addedIngredients.put(new Ingredient("Coffee"), 5);
        addedIngredients.put(new Ingredient("Milk"), 3);
        addedIngredients.put(new Ingredient("Sugar"), 7);
        addedIngredients.put(new Ingredient("Chocolate"), 2);
        ivt.addIngredients(addedIngredients);
        Assertions.assertEquals("Chocolate: 502\nCoffee: 505\nMilk: 503\nSugar: 507\n", ivt.toString());

    }

    @Test
    @Transactional
    public void testEnoughIngredients() {
        final Inventory ivt = inventoryService.getInventory();
        Map<Ingredient, Integer> addedIngredients = new HashMap<>();
        addedIngredients.put(new Ingredient("Coffee"), 5);
        addedIngredients.put(new Ingredient("Milk"), 3);
        addedIngredients.put(new Ingredient("Sugar"), 7);
        addedIngredients.put(new Ingredient("Chocolate"), 2);
        ivt.addIngredients(addedIngredients);
        final Recipe r1 = new Recipe();
        r1.setName("test");
        r1.setPrice(1000);
        r1.addIngredient(new Ingredient("Coffee"), 1000);
        r1.addIngredient(new Ingredient("Milk"), 1000);
        r1.addIngredient(new Ingredient("Sugar"), 1000);
        r1.addIngredient(new Ingredient("Chocolate"), 1000);
        Assertions.assertFalse(ivt.enoughIngredients(r1));
    }

}
