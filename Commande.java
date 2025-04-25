package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Commande {
    private List<Cupcake> cupcakes;

    public Commande() {
        this.cupcakes = new ArrayList<>();
    }

    public boolean ajouterCupcake(Cupcake cupcake) {
        if (!cupcake.estDisponible()) {
            return false;
        }

        cupcakes.add(cupcake);
        cupcake.preparerCupcake();
        return true;
    }

    public double calculerTotal() {
        if (cupcakes.isEmpty()) {
            return 0;
        }

        List<Cupcake> cupcakesNormaux = cupcakes.stream()
                .filter(c -> !c.estCupcakeDuJour())
                .collect(Collectors.toList());

        int nombreCupcakesGratuits = cupcakesNormaux.size() / 6;

        double total = 0;


        for (Cupcake cupcake : cupcakes) {
            if (cupcake.estCupcakeDuJour()) {
                total += cupcake.calculerPrix();
            }
        }


        if (!cupcakesNormaux.isEmpty() && nombreCupcakesGratuits > 0) {

            cupcakesNormaux.sort(Comparator.comparing(Cupcake::calculerPrix));


            for (int i = 0; i < nombreCupcakesGratuits; i++) {
                cupcakesNormaux.remove(0); // Supprime le moins cher
            }
        }


        for (Cupcake cupcake : cupcakesNormaux) {
            total += cupcake.calculerPrix();
        }

        return total;
    }

    public List<Cupcake> getCupcakes() {
        return cupcakes;
    }
}
