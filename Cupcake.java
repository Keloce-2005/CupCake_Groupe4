package org.example;

import java.util.ArrayList;
import java.util.List;

public class Cupcake {
    private Ingredient base;
    private Ingredient creme;
    private List<Ingredient> toppings;
    private boolean estCupcakeDuJour;
    private int stockCupcakeDuJour;

     Cupcake(Ingredient base, Ingredient creme) {
        this.base = base;
        this.creme = creme;
        this.toppings = new ArrayList<>();
        this.estCupcakeDuJour = false;
    }

     Cupcake(Ingredient base, Ingredient creme, boolean estCupcakeDuJour, int stock) {
        this(base, creme);
        this.estCupcakeDuJour = estCupcakeDuJour;
        this.stockCupcakeDuJour = stock;
    }

     boolean ajouterTopping(Ingredient topping) {
        if (toppings.size() >= 2) {
            return false;
        }
        toppings.add(topping);
        return true;
    }

     double calculerPrix() {
        double prixTotal = base.getPrix() + creme.getPrix();

        if (!toppings.isEmpty()) {

            Ingredient toppingMoinsCher = toppings.get(0);
            for (Ingredient topping : toppings) {
                if (topping.getPrix() < toppingMoinsCher.getPrix()) {
                    toppingMoinsCher = topping;
                }
            }


            for (Ingredient topping : toppings) {
                if (topping != toppingMoinsCher || toppings.size() == 1) {
                    prixTotal += topping.getPrix();
                }
            }
        }

        if (estCupcakeDuJour) {
            prixTotal = prixTotal * 0.6;
        }

        return prixTotal;
    }

     boolean estDisponible() {
        if (estCupcakeDuJour) {
            return stockCupcakeDuJour > 0 && base.estDisponible() && creme.estDisponible() &&
                    toppings.stream().allMatch(Ingredient::estDisponible);
        } else {
            return base.estDisponible() && creme.estDisponible() &&
                    toppings.stream().allMatch(Ingredient::estDisponible);
        }
    }

    void preparerCupcake() {
        if (estCupcakeDuJour) {
            stockCupcakeDuJour--;
        } else {
            base.diminuerStock();
            creme.diminuerStock();
            for (Ingredient topping : toppings) {
                topping.diminuerStock();
            }
        }
    }

     boolean estCupcakeDuJour() {
        return estCupcakeDuJour;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cupcake:").append(base.getNom()).append(" avec creme ").append(creme.getNom());

        if (!toppings.isEmpty()) {
            sb.append(", topings: ");
            for (int i = 0; i < toppings.size(); i++) {
                sb.append(toppings.get(i).getNom());
                if (i < toppings.size() - 1) {
                    sb.append(", ");
                }
            }
        }

        if (estCupcakeDuJour) {
            sb.append("(Cupcake du jour)");
        }

        return sb.toString();
    }

    Ingredient getBase() {
        return base;
    }

     Ingredient getCreme() {
        return creme;
    }

     List<Ingredient> getToppings() {
        return toppings;
    }

     int getStockCupcakeDuJour() {
        return stockCupcakeDuJour;
    }
}
