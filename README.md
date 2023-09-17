# Coffee Maker Application

## Table of Contents

- [Introduction](#introduction)
- [File/Folder Structure](#filefolder-structure)
- [Application Components](#application-components)
- [How to Run](#how-to-run)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

## Introduction

The Coffee Maker Application is a Java-based software system designed to simulate a coffee maker. It allows users to update inventory, add a recipe, edit recipes, delete recipes, make coffee, add ingredients, and to do so through a web-based interface.

## File/Folder Structure

The project is organized into the following files and folders:

- `.github`: GitHub-specific configuration files.
- `CoffeeMaker`: The main project folder.
  - `.mvn/wrapper`: Maven wrapper files for project setup.
  - `src/main`: The main source code directory.
    - `java/edu/ncsu/csc/CoffeeMaker/controllers`: Controllers for handling web requests.
      - `APICoffeeController.java`: Controller for coffee-related API operations.
      - `APIController.java`: General API controller.
      - `APIIngredientController.java`: Controller for ingredient-related API operations.
      - `APIInventoryController.java`: Controller for inventory-related API operations.
      - `APIRecipeController.java`: Controller for recipe-related API operations.
      - `MappingController.java`: Mapping controller.
    - `models`: Model classes representing domain objects.
      - `DomainObject.java`: Base class for domain objects.
      - `Ingredient.java`: Represents an ingredient.
      - `Inventory.java`: Represents the inventory of ingredients.
      - `Recipe.java`: Represents a coffee recipe.
    - `repositories`: Data repositories for interacting with the database.
      - `IngredientRepository.java`: Repository for ingredients.
      - `InventoryRepository.java`: Repository for inventory.
      - `RecipeRepository.java`: Repository for recipes.
    - `services`: Service classes for business logic.
      - `IngredientService.java`: Service for ingredient-related operations.
      - `InventoryService.java`: Service for inventory-related operations.
      - `RecipeService.java`: Service for recipe-related operations.
      - `Service.java`: Base service interface.
    - `CoffeeMakerApplication.java`: Main Spring Boot application class.
  - `resources`: Configuration and static resource files.
    - `static/css`: CSS stylesheets.
    - `templates`: HTML templates.
    - `application.yml.template`: Application configuration template.
  - `test/java/edu/ncsu/csc/CoffeeMaker`: Unit and integration tests.
    - `api`: API tests.
    - `common`: Common test utilities.
    - `unit`: Unit tests for various components.
- `.gitignore`: Git ignore file.
- `pom.xml`: Maven project configuration file.
- `images`: Images related to the project (e.g., diagrams).
- `README.md`: This README file.

## Application Components

The Coffee Maker Application consists of several components, including:

- **Controllers**: Handle incoming HTTP requests and manage routing.
- **Models**: Represent the core data structures of the application.
- **Repositories**: Provide data access methods to interact with the database.
- **Services**: Handles CRUD operations performed on models.
- **HTML Templates**: Define the structure of web pages.
- **CSS Stylesheets**: Define the application's styling.
- **Unit and Integration Tests**: Ensure the application functions correctly.

## How to Run

To run the Coffee Maker Application:

1. Clone the repository to your local machine.
2. Open the project in your preferred IDE.
3. Build the project and resolve any dependencies.
4. Run the `CoffeeMakerApplication.java` class to start the application.
5. Access the application in a web browser at `http://localhost:8080`.

## Testing

The project includes unit and integration tests located in the `test` directory. You can run the tests to ensure the correctness of the application's components and functionality.
