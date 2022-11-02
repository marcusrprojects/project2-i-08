package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.jupiter.api.Assertions;
import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@SpringBootTest(classes = TestConfig.class)
public class InventoryTest {

	@Autowired
	private InventoryService inventoryService;

	@BeforeEach
	public void setup() {
		final Inventory ivt = inventoryService.getInventory();

		ivt.setChocolate(500);
		ivt.setCoffee(500);
		ivt.setMilk(500);
		ivt.setSugar(500);

		inventoryService.save(ivt);
	}

	@Test
	@Transactional
	public void testConsumeInventory() {
		final Inventory i = inventoryService.getInventory();

		final Recipe recipe = new Recipe();
		recipe.setName("Delicious Not-Coffee");
		recipe.setChocolate(10);
		recipe.setMilk(20);
		recipe.setSugar(5);
		recipe.setCoffee(1);

		recipe.setPrice(5);

		i.useIngredients(recipe);

		/*
		 * Make sure that all of the inventory fields are now properly updated
		 */

		Assertions.assertEquals(490, (int) i.getChocolate());
		Assertions.assertEquals(480, (int) i.getMilk());
		Assertions.assertEquals(495, (int) i.getSugar());
		Assertions.assertEquals(499, (int) i.getCoffee());
	}

	@Test
	@Transactional
	public void testAddInventory1() {
		Inventory ivt = inventoryService.getInventory();

		ivt.addIngredients(5, 3, 7, 2);

		/* Save and retrieve again to update with DB */
		inventoryService.save(ivt);

		ivt = inventoryService.getInventory();

		Assertions.assertEquals(505, (int) ivt.getCoffee(),
				"Adding to the inventory should result in correctly-updated values for coffee");
		Assertions.assertEquals(503, (int) ivt.getMilk(),
				"Adding to the inventory should result in correctly-updated values for milk");
		Assertions.assertEquals(507, (int) ivt.getSugar(),
				"Adding to the inventory should result in correctly-updated values sugar");
		Assertions.assertEquals(502, (int) ivt.getChocolate(),
				"Adding to the inventory should result in correctly-updated values chocolate");

	}

	@Test
	@Transactional
	public void testAddInventory2() {
		final Inventory ivt = inventoryService.getInventory();

		try {
			ivt.addIngredients(-5, 3, 7, 2);
		} catch (final IllegalArgumentException iae) {
			Assertions.assertEquals(500, (int) ivt.getCoffee(),
					"Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee");
			Assertions.assertEquals(500, (int) ivt.getMilk(),
					"Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk");
			Assertions.assertEquals(500, (int) ivt.getSugar(),
					"Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar");
			Assertions.assertEquals(500, (int) ivt.getChocolate(),
					"Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate");
		}
	}

	@Test
	@Transactional
	public void testAddInventory3() {
		final Inventory ivt = inventoryService.getInventory();

		try {
			ivt.addIngredients(5, -3, 7, 2);
		} catch (final IllegalArgumentException iae) {
			Assertions.assertEquals(500, (int) ivt.getCoffee(),
					"Trying to update the Inventory with an invalid value for milk should result in no changes -- coffee");
			Assertions.assertEquals(500, (int) ivt.getMilk(),
					"Trying to update the Inventory with an invalid value for milk should result in no changes -- milk");
			Assertions.assertEquals(500, (int) ivt.getSugar(),
					"Trying to update the Inventory with an invalid value for milk should result in no changes -- sugar");
			Assertions.assertEquals(500, (int) ivt.getChocolate(),
					"Trying to update the Inventory with an invalid value for milk should result in no changes -- chocolate");

		}

	}

	@Test
	@Transactional
	public void testAddInventory4() {
		final Inventory ivt = inventoryService.getInventory();

		try {
			ivt.addIngredients(5, 3, -7, 2);
		} catch (final IllegalArgumentException iae) {
			Assertions.assertEquals(500, (int) ivt.getCoffee(),
					"Trying to update the Inventory with an invalid value for sugar should result in no changes -- coffee");
			Assertions.assertEquals(500, (int) ivt.getMilk(),
					"Trying to update the Inventory with an invalid value for sugar should result in no changes -- milk");
			Assertions.assertEquals(500, (int) ivt.getSugar(),
					"Trying to update the Inventory with an invalid value for sugar should result in no changes -- sugar");
			Assertions.assertEquals(500, (int) ivt.getChocolate(),
					"Trying to update the Inventory with an invalid value for sugar should result in no changes -- chocolate");

		}

	}
  
    @Test
    @Transactional
    public void testChecks () {
    	
    	final Inventory ivt = inventoryService.getInventory();
    	
	    Assertions.assertThrows(IllegalArgumentException.class, () -> {
			ivt.checkChocolate("-1");
		}, "Cannot add negative amount of chocolate");
	
	    Assertions.assertThrows(IllegalArgumentException.class, () -> {
			ivt.checkChocolate("negative one");
		}, "Cannot add a string that does not parse into an integer");
	
	    Assertions.assertThrows(IllegalArgumentException.class, () -> {
			ivt.checkMilk("-1");
		}, "Cannot add negative amount of milk");
	
	    Assertions.assertThrows(IllegalArgumentException.class, () -> {
			ivt.checkMilk("negative one");
		}, "Cannot add a string that does not parse into an integer");
	
	    Assertions.assertThrows(IllegalArgumentException.class, () -> {
			ivt.checkSugar("-1");
		}, "Cannot add negative amount of sugar");
	
	    Assertions.assertThrows(IllegalArgumentException.class, () -> {
			ivt.checkSugar("negative one");
		}, "Cannot add a string that does not parse into an integer");
	
	    Assertions.assertThrows(IllegalArgumentException.class, () -> {
			ivt.checkCoffee("-1");
		}, "Cannot add negative amount of chocolate");
	
	    Assertions.assertThrows(IllegalArgumentException.class, () -> {
			ivt.checkCoffee("negative one");
		}, "Cannot add a string that does not parse into an integer");
	}

	@Test
	@Transactional
	public void testAddInventory5() {
		final Inventory ivt = inventoryService.getInventory();

		try {
			ivt.addIngredients(5, 3, 7, -2);
		} catch (final IllegalArgumentException iae) {
			Assertions.assertEquals(500, (int) ivt.getCoffee(),
					"Trying to update the Inventory with an invalid value for chocolate should result in no changes -- coffee");
			Assertions.assertEquals(500, (int) ivt.getMilk(),
					"Trying to update the Inventory with an invalid value for chocolate should result in no changes -- milk");
			Assertions.assertEquals(500, (int) ivt.getSugar(),
					"Trying to update the Inventory with an invalid value for chocolate should result in no changes -- sugar");
			Assertions.assertEquals(500, (int) ivt.getChocolate(),
					"Trying to update the Inventory with an invalid value for chocolate should result in no changes -- chocolate");

		}

	}

	@Test
	@Transactional
	public void testCheckInventory() {
		final Inventory ivt = inventoryService.getInventory();
		ivt.addIngredients(5, 3, 7, 2);
		ivt.setId((long) (2));
		Assertions.assertEquals((long) (2), (long) ivt.getId());
		// Chocolate
		try {
			ivt.setChocolate(ivt.checkChocolate("-5"));
			Assertions.fail("should fail as cannot set to a negative int");
		} catch (IllegalArgumentException e) {
			ivt.setChocolate(ivt.checkChocolate("5"));
		}
		try {
			ivt.setChocolate(ivt.checkChocolate("five"));
			Assertions.fail("should fail as cannot set to a string");
		} catch (IllegalArgumentException e) {
			ivt.setChocolate(ivt.checkChocolate("5"));
		}
		Assertions.assertEquals(5, (int) ivt.getChocolate());
		// Coffee
		try {
			ivt.setChocolate(ivt.checkCoffee("-5"));
			Assertions.fail("should fail as cannot set to a negative int");
		} catch (IllegalArgumentException e) {
			ivt.setCoffee(ivt.checkCoffee("5"));
		}
		try {
			ivt.setCoffee(ivt.checkCoffee("five"));
			Assertions.fail("should fail as cannot set to a string");
		} catch (IllegalArgumentException e) {
			ivt.setCoffee(ivt.checkCoffee("5"));
		}
		Assertions.assertEquals(5, (int) ivt.getCoffee());
		// Milk
		try {
			ivt.setMilk(ivt.checkMilk("-5"));
			Assertions.fail("should fail as cannot set to a negative int");
		} catch (IllegalArgumentException e) {
			ivt.setMilk(ivt.checkMilk("5"));
		}
		try {
			ivt.setMilk(ivt.checkMilk("five"));
			Assertions.fail("should fail as cannot set to a string");
		} catch (IllegalArgumentException e) {
			ivt.setMilk(ivt.checkMilk("5"));
		}
		Assertions.assertEquals(5, (int) ivt.getMilk());
		// Sugar
		try {
			ivt.setSugar(ivt.checkSugar("-5"));
			Assertions.fail("should fail as cannot set to a negative int");
		} catch (IllegalArgumentException e) {
			ivt.setSugar(ivt.checkSugar("5"));
		}
		try {
			ivt.setSugar(ivt.checkSugar("five"));
			Assertions.fail("should fail as cannot set to a string");
		} catch (IllegalArgumentException e) {
			ivt.setSugar(ivt.checkSugar("5"));
		}
		Assertions.assertEquals(5, (int) ivt.getSugar());
	}
	
	@Test
	@Transactional
	public void testToString() {
		final Inventory ivt = inventoryService.getInventory();
		ivt.addIngredients(5, 3, 7, 2);
		Assertions.assertEquals("Coffee: 505\n"
				+ "Milk: 503\n"
				+ "Sugar: 507\n"
				+ "Chocolate: 502\n",  ivt.toString());
		
	}
	
	@Test
	@Transactional
	public void testEnoughIngredients() {
		final Inventory ivt = inventoryService.getInventory();
		ivt.addIngredients(5, 3, 7, 2);
        final Recipe r1 = new Recipe();
        r1.setName( "test" );
        r1.setPrice( 1000 );
        r1.setCoffee( 1000 );
        r1.setMilk( 1000 );
        r1.setSugar( 1000 );
        r1.setChocolate( 1000 );
        Assertions.assertEquals(false, ivt.enoughIngredients(r1));
		
	}

}
