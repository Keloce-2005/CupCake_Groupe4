package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Menu {
    private List<Ingredient> ingredients;
    private List<Cupcake> cupcakesDuJour;

    public Menu() {
        this.ingredients = new ArrayList<>();
        this.cupcakesDuJour = new ArrayList<>();
    }

    public void ajouterIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public void ajouterCupcakeDuJour(Cupcake cupcake) {
        cupcakesDuJour.add(cupcake);
    }

    public List<Ingredient> getIngredientsDisponibles() {
        return ingredients.stream()
                .filter(Ingredient::estDisponible)
                .collect(Collectors.toList());
    }

    public List<Ingredient> getBasesDisponibles() {
        return getIngredientsDisponibles().stream()
                .filter(i -> i.getType() == Ingredient.IngredientType.BASE)
                .collect(Collectors.toList());
    }

    public List<Ingredient> getCremesDisponibles() {
        return getIngredientsDisponibles().stream()
                .filter(i -> i.getType() == Ingredient.IngredientType.CREME)
                .collect(Collectors.toList());
    }

    public List<Ingredient> getToppingsDisponibles() {
        return getIngredientsDisponibles().stream()
                .filter(i -> i.getType() == Ingredient.IngredientType.TOPPING)
                .collect(Collectors.toList());
    }

    public List<Cupcake> getCupcakesDuJourDisponibles() {
        return cupcakesDuJour.stream()
                .filter(Cupcake::estDisponible)
                .collect(Collectors.toList());
    }

    public boolean peutFaireCupcakes() {
        return !getBasesDisponibles().isEmpty() && !getCremesDisponibles().isEmpty();
    }

    public void afficherMenu() {
        System.out.println("===== MENU CUPCAKE FACTORY =====");


        List<Cupcake> cupcakesDuJourDispos = getCupcakesDuJourDisponibles();
        if (!cupcakesDuJourDispos.isEmpty()) {
            System.out.println("\nCUPCAKES DU JOUR (60% du prix normal):");
            for (Cupcake cupcake : cupcakesDuJourDispos) {
                System.out.printf("- %s (%.2f€) - Stock: %d\n",
                        cupcake.toString(), cupcake.calculerPrix(), cupcake.getStockCupcakeDuJour());
            }
        }


        if (peutFaireCupcakes()) {
            System.out.println("\nINGREDIENTS DISPONIBLES:");

            System.out.println("Bases:");
            for (Ingredient base : getBasesDisponibles()) {
                System.out.printf("- %s (%.2f€) - Stock: %d\n", base.getNom(), base.getPrix(), base.getStock());
            }

            System.out.println("\nCrèmes:");
            for (Ingredient creme : getCremesDisponibles()) {
                System.out.printf("- %s (%.2f€) - Stock: %d\n", creme.getNom(), creme.getPrix(), creme.getStock());
            }

            System.out.println("\nToppings:");
            for (Ingredient topping : getToppingsDisponibles()) {
                System.out.printf("- %s (%.2f€) - Stock: %d\n", topping.getNom(), topping.getPrix(), topping.getStock());
            }
        }
    }
}