package org.example;

public class Ingredient {
    private String nom;
    private double prix;
    private int stock;
    private IngredientType type;

    public enum IngredientType {
        BASE, CREME, TOPPING
    }

    public Ingredient(String nom, double prix, int stock, IngredientType type) {
        this.nom = nom;
        this.prix = prix;
        this.stock = stock;
        this.type = type;
    }

    public String getNom() {
        return nom;
    }

    public double getPrix() {
        return prix;
    }

    public int getStock() {
        return stock;
    }

    public void diminuerStock() {
        if (stock > 0) {
            stock--;
        }
    }

    public boolean estDisponible() {
        return stock > 0;
    }

    public IngredientType getType() {
        return type;
    }
}
