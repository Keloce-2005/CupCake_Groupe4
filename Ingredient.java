package org.example;

public class Ingredient {
    private String nom;
    private double prix;
    private int stock;
    private IngredientType type;

     enum IngredientType {
        BASE, CREME, TOPPING
    }

     Ingredient(String nom, double prix, int stock, IngredientType type) {
        this.nom = nom;
        this.prix = prix;
        this.stock = stock;
        this.type = type;
    }

     String getNom() {
        return nom;
    }

     double getPrix() {
        return prix;
    }

     int getStock() {
        return stock;
    }

     void diminuerStock() {
        if (stock > 0) {
            stock--;
        }
    }

     boolean estDisponible() {
        return stock > 0;
    }

    IngredientType getType() {
        return type;
    }
}
