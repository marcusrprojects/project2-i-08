package edu.ncsu.csc.CoffeeMaker.controllers;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * The APIIngredientController is responsible for handling ingredients when a user submits
 * a request to do so.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Darien Gillespie
 *
 */
@RestController
public class APIIngredientController extends APIController {
    /**
     * IngredientService object, to be autowired in by Spring to allow for
     * manipulating the Ingredient model
     */
    @Autowired
    private IngredientService ingredientService;

    /**
     * InventoryService object, to be autowired in by Spring to allow for
     * manipulating the Inventory model
     */
    @Autowired
    private InventoryService inventoryService;
    
    /**
     * REST API method to provide GET access to all ingredients in the system
     *
     * @param unique whether or not the returned list should only include unique ingredients by name
     * @return JSON representation of all ingredients
     */
    @GetMapping(BASE_PATH + "ingredients")
    public List<Ingredient> getIngredients(@RequestParam(required = false) boolean unique) {
        if (unique) {
            return new ArrayList<>(inventoryService.getInventory().getIngredients().keySet());
        } else {
            return ingredientService.findAll();
        }
    }

    /**
     * REST API method to provide GET access to a single ingredient in the system
     * @param name name of the ingredient to GET
     * @return JSON representation of the ingredient
     */
    @GetMapping(BASE_PATH + "ingredients/{name}")
    public ResponseEntity getIngredient(@PathVariable final String name) {

        final Ingredient ingr = ingredientService.findByName(name);

        if (ingr == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(ingr, HttpStatus.OK);
    }
    
    /**
     * REST API method to provide PUT access to the Ingredient model. This is used
     * to update an Ingredient by automatically converting the JSON RequestBody
     * provided to an Ingredient object. Invalid JSON will fail.
     *
     * @param name the name of the Ingredient to update
     * @param ingredient The valid Ingredient to be updated with
     * @return ResponseEntity indicating success if the Ingredient could be saved to
     * the inventory, or an error if it could not be
     */
    @PutMapping(BASE_PATH + "/ingredients/{name}")
    public ResponseEntity updateIngredient(@PathVariable final String name, @RequestBody final Ingredient ingredient) {
        Ingredient oldIngredient = ingredientService.findByName(name);

        if (oldIngredient == null) {
            return new ResponseEntity(errorResponse("No ingredient found with name " + name), HttpStatus.NOT_FOUND);
        }

        oldIngredient.setName(ingredient.getName());

        ingredientService.save(oldIngredient);
        return new ResponseEntity(successResponse(oldIngredient + " successfully updated"), HttpStatus.OK);
    }

    /**
     * REST API method to provide POST access to the Ingredient model. This is used
     * to create a new Ingredient by automatically converting the JSON RequestBody
     * provided to an Ingredient object. Invalid JSON will fail.
     *
     * @param ingredient The valid Ingredient to be saved.
     * @return ResponseEntity indicating success if the Ingredient could be saved to
     * the inventory, or an error if it could not be
     */
    @PostMapping(BASE_PATH + "/ingredients")
    public ResponseEntity createIngredient(@RequestBody final Ingredient ingredient) {
    	if ( null != ingredientService.findByName( ingredient.getName() ) ) {
            return new ResponseEntity( errorResponse( "Ingredient with the name " + ingredient.getName() + " already exists" ),
                    HttpStatus.CONFLICT );
        }

        ingredientService.save(ingredient);
        return new ResponseEntity(successResponse(ingredient.toString() + " successfully created"), HttpStatus.OK);
    }

    /**
     * REST API method to allow deleting an Ingredient from the CoffeeMaker's
     * Inventory, by making a DELETE request to the API endpoint and indicating
     * the ingredient to delete (as a path variable)
     *
     * @param name The name of the Ingredient to delete
     * @return Success if the ingredient could be deleted; an error if the ingredient
     * does not exist
     */
    @DeleteMapping(BASE_PATH + "/ingredients/{name}")
    public ResponseEntity deleteIngredient(@PathVariable final String name) {
        final Ingredient ingredient = ingredientService.findByName(name);
        if (null == ingredient) {
            return new ResponseEntity(errorResponse("No ingredient found for name " + name), HttpStatus.NOT_FOUND);
        }
        ingredientService.delete(ingredient);

        return new ResponseEntity(successResponse(name + " was deleted successfully"), HttpStatus.OK);
    }

}
