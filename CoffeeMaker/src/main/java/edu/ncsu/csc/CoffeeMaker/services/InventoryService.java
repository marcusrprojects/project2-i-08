package edu.ncsu.csc.CoffeeMaker.services;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.repositories.IngredientRepository;
import edu.ncsu.csc.CoffeeMaker.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

/**
 * The InventoryService is used to handle CRUD operations on the Inventory
 * model. In addition to all functionality in `Service`, we also manage the
 * Inventory singleton.
 *
 * @author Kai Presler-Marshall
 */
@Component
@Transactional
public class InventoryService extends Service<Inventory, Long> {

    /**
     * InventoryRepository, to be autowired in by Spring and provide CRUD
     * operations on Inventory model.
     */
    @Autowired
    private InventoryRepository inventoryRepository;

    /**
     * IngredientRepository, to be autowired in by Spring and provide CRUD
     * operations on Ingredient model
     */
    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    protected JpaRepository<Inventory, Long> getRepository() {
        return inventoryRepository;
    }

    /**
     * Retrieves the singleton Inventory instance from the database, creating it
     * if it does not exist.
     *
     * @return the Inventory, either new or fetched
     */
    public synchronized Inventory getInventory() {
        final List<Inventory> inventoryList = findAll();
        if (inventoryList != null && inventoryList.size() == 1) {
            return inventoryList.get(0);
        } else {
            // initialize the inventory with 0 of everything
            final Inventory i = new Inventory();
            save(i);
            return i;
        }
    }

    /**
     * Saves an Inventory and all its ingredients to the database
     * @param i
     *            The Inventory to save into the database.
     */
    @Override
    public void save(Inventory i) {
        for (Ingredient ingredient : i.getIngredients().keySet()) {
            ingredientRepository.save(ingredient);
        }

        super.save(i);
    }

//    /**
//     * Saves all the inventories and their ingredients to the database
//     * @param objects
//     *            A List of inventories to save to the database.
//     */
//    @Override
//    public void saveAll(List<Inventory> objects) {
//        for (Inventory i : objects) {
//            ingredientRepository.saveAll(i.getIngredients().keySet());
//        }
//
//        super.saveAll(objects);
//    }
//
//    /**
//     * Deletes an inventory and all of its ingredients from the database
//     * @param i
//     *            The inventory to delete from the database.
//     */
//    @Override
//    public void delete(Inventory i) {
//        Set<Ingredient> ingredients = i.getIngredients().keySet();
//        super.delete(i);
//
//        for (Ingredient ingredient : ingredients) {
//            ingredientRepository.delete(ingredient);
//        }
//    }
//
//    /**
//     * Deletes all inventories and their ingredients from the database
//     */
//    @Override
//    public void deleteAll() {
//        List<Inventory> inventories = inventoryRepository.findAll();
//        Set<Set<Ingredient>> ingredients = new HashSet<>();
//        for (Inventory inventory : inventories) {
//            ingredients.add(inventory.getIngredients().keySet());
//        }
//        super.deleteAll();
//
//        for (Set<Ingredient> ingredientSet : ingredients) {
//            for (Ingredient i : ingredientSet) {
//                ingredientRepository.delete(i);
//            }
//        }
//    }
//
}
