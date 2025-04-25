package org.example;

import java.util.Scanner;
import java.util.List;

public class CupcakeFactory {
    private Menu menu;

     CupcakeFactory() {
        this.menu = new Menu();
        initialiserIngredients();
        initialiserCupcakesDuJour();
    }

    private void initialiserIngredients() {

        menu.ajouterIngredient(new Ingredient("Nature", 1.5, 15, Ingredient.IngredientType.BASE));
        menu.ajouterIngredient(new Ingredient("Chocolat", 2.0, 15, Ingredient.IngredientType.BASE));
        menu.ajouterIngredient(new Ingredient("Fourrée", 2.5, 7, Ingredient.IngredientType.BASE));


        menu.ajouterIngredient(new Ingredient("Vanille", 1.0, 7, Ingredient.IngredientType.CREME));
        menu.ajouterIngredient(new Ingredient("Chocolat", 1.2, 15, Ingredient.IngredientType.CREME));
        menu.ajouterIngredient(new Ingredient("Framboise", 1.5, 70, Ingredient.IngredientType.CREME));


        menu.ajouterIngredient(new Ingredient("Pépites de chocolat", 0.8, 30, Ingredient.IngredientType.TOPPING));
        menu.ajouterIngredient(new Ingredient("Vermicelles", 0.5, 40, Ingredient.IngredientType.TOPPING));
        menu.ajouterIngredient(new Ingredient("Marshmallows", 1.0, 25, Ingredient.IngredientType.TOPPING));
        menu.ajouterIngredient(new Ingredient("Cookie dought", 1.2, 20, Ingredient.IngredientType.TOPPING));
    }

    private void initialiserCupcakesDuJour() {

        Cupcake cupcake1 = new Cupcake(
                findIngredient("Chocolat", Ingredient.IngredientType.BASE),
                findIngredient("Vanille", Ingredient.IngredientType.CREME),
                true, 10
        );
        cupcake1.ajouterTopping(findIngredient("Pépites de chocolat", Ingredient.IngredientType.TOPPING));
        menu.ajouterCupcakeDuJour(cupcake1);


        Cupcake cupcake2 = new Cupcake(
                findIngredient("Nature", Ingredient.IngredientType.BASE),
                findIngredient("Framboise", Ingredient.IngredientType.CREME),
                true, 10
        );
        cupcake2.ajouterTopping(findIngredient("Marshmallows", Ingredient.IngredientType.TOPPING));
        menu.ajouterCupcakeDuJour(cupcake2);


        Cupcake cupcake3 = new Cupcake(
                findIngredient("Fourrée", Ingredient.IngredientType.BASE),
                findIngredient("Chocolat", Ingredient.IngredientType.CREME),
                true, 10
        );
        cupcake3.ajouterTopping(findIngredient("Cookie dought", Ingredient.IngredientType.TOPPING));
        menu.ajouterCupcakeDuJour(cupcake3);
    }

    private Ingredient findIngredient(String nom, Ingredient.IngredientType type) {
        for (Ingredient ingredient : menu.getIngredientsDisponibles()) {
            if (ingredient.getNom().equals(nom) && ingredient.getType() == type) {
                return ingredient;
            }
        }
        return null;
    }

     Menu getMenu() {
        return menu;
    }

     Commande creerNouvelleCommande() {
        return new Commande();
    }

    public static void main(String[] args) {
        CupcakeFactory cupcakeFactory = new CupcakeFactory();
        Scanner scanner = new Scanner(System.in);
        Commande commande = cupcakeFactory.creerNouvelleCommande();

        boolean continuer = true;

        System.out.println("Bienvenue à la Cupcake Factory les amis !!!");

        while (continuer) {
            cupcakeFactory.getMenu().afficherMenu();

            System.out.println("\nQue voulez-vous faire ?");
            System.out.println("1. Commander un cupcake du jour");
            System.out.println("2. Composer un cupcake");
            System.out.println("3. Voir ma commande");
            System.out.println("4. Finaliser ma commande");
            System.out.println("5. Quitter");

            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    commanderCupcakeDuJour(cupcakeFactory, scanner, commande);
                    break;
                case 2:
                    composerCupcake(cupcakeFactory, scanner, commande);
                    break;
                case 3:
                    voirCommande(commande);
                    break;
                case 4:
                    finaliserCommande(commande);
                    continuer = false;
                    break;
                case 5:
                    continuer = false;
                    break;
                default:
                    System.out.println("Choix invalide");
            }
        }

        scanner.close();
    }

    private static void commanderCupcakeDuJour(CupcakeFactory factory, Scanner scanner, Commande commande) {
        List<Cupcake> cupcakesDuJour = factory.getMenu().getCupcakesDuJourDisponibles();

        if (cupcakesDuJour.isEmpty()) {
            System.out.println("Désolé, il n'y a plus de cupcakes du jour disponibles.");
            return;
        }

        System.out.println("Choisissez un cupcake du jour (1-" + cupcakesDuJour.size() + "):");
        for (int i = 0; i < cupcakesDuJour.size(); i++) {
            System.out.println((i+1) + ". " + cupcakesDuJour.get(i));
        }

        int choix = scanner.nextInt();
        scanner.nextLine(); // Pour consommer le retour à la ligne

        if (choix < 1 || choix > cupcakesDuJour.size()) {
            System.out.println("Choix invalide");
            return;
        }

        boolean succes = commande.ajouterCupcake(cupcakesDuJour.get(choix - 1));
        if (succes) {
            System.out.println("Cupcake du jour ajouté à votre commande !");
        } else {
            System.out.println("Désolé, ce cupcake n'est plus disponible.");
        }
    }

    private static void composerCupcake(CupcakeFactory factory, Scanner scanner, Commande commande) {
        Menu menu = factory.getMenu();

        if (!menu.peutFaireCupcakes()) {
            System.out.println("Désolé, nous n'avons plus assez d'ingredients pour faire des cupcakes personnalisés.");
            return;
        }

        // Choisir une base
        List<Ingredient> bases = menu.getBasesDisponibles();
        System.out.println("Choisissez une base (1-" + bases.size() + "):");
        for (int i = 0; i < bases.size(); i++) {
            System.out.println((i+1) + ". " + bases.get(i).getNom() + " (" + bases.get(i).getPrix() + "€)");
        }

        int choixBase = scanner.nextInt();
        scanner.nextLine(); // Pour consomer le retour à la ligne

        if (choixBase < 1 || choixBase > bases.size()) {
            System.out.println("Choix invalide");
            return;
        }


        List<Ingredient> cremes = menu.getCremesDisponibles();
        System.out.println("Choisissez une crème (1-" + cremes.size() + "):");
        for (int i = 0; i < cremes.size(); i++) {
            System.out.println((i+1) + ". " + cremes.get(i).getNom() + " (" + cremes.get(i).getPrix() + "€)");
        }

        int choixCreme = scanner.nextInt();
        scanner.nextLine();

        if (choixCreme < 1 || choixCreme > cremes.size()) {
            System.out.println("Choix invalide");
            return;
        }

        Cupcake cupcake = new Cupcake(bases.get(choixBase - 1), cremes.get(choixCreme - 1));


        List<Ingredient> toppings = menu.getToppingsDisponibles();
        boolean continuerToppings = true;

        while (continuerToppings && cupcake.getToppings().size() < 2) {
            System.out.println("Voulez-vous ajouter un topping ? (o/n)");
            String reponse = scanner.nextLine().toLowerCase();

            if (reponse.equals("o")) {
                System.out.println("Choisissez un topping (1-" + toppings.size() + "):");
                for (int i = 0; i < toppings.size(); i++) {
                    System.out.println((i+1) + ". " + toppings.get(i).getNom() + " (" + toppings.get(i).getPrix() + "€)");
                }

                int choixTopping = scanner.nextInt();
                scanner.nextLine();

                if (choixTopping < 1 || choixTopping > toppings.size()) {
                    System.out.println("Choix invalide");
                } else {
                    boolean ajoutReussi = cupcake.ajouterTopping(toppings.get(choixTopping - 1));
                    if (!ajoutReussi) {
                        System.out.println("Vous ne pouvez pas ajouter plus de 2 toppings.");
                        continuerToppings = false;
                    }
                }
            } else {
                continuerToppings = false;
            }
        }

        boolean succes = commande.ajouterCupcake(cupcake);
        if (succes) {
            System.out.println("Cupcake personnalisé ajouté à votre commande !");
        } else {
            System.out.println("Désolé, nous n'avons plus les ingrédients nécessaires pour ce cupcake.");
        }
    }

    private static void voirCommande(Commande commande) {
        List<Cupcake> cupcakes = commande.getCupcakes();

        if (cupcakes.isEmpty()) {
            System.out.println("Votre commande est vide.");
            return;
        }

        System.out.println("\n===== VOTRE COMMANDE =====");
        for (int i = 0; i < cupcakes.size(); i++) {
            System.out.println((i+1) + ". " + cupcakes.get(i) + " (" + cupcakes.get(i).calculerPrix() + "€)");
        }

        System.out.println("\nTotal : " + commande.calculerTotal() + "€");
    }

    private static void finaliserCommande(Commande commande) {
        System.out.println("\n===== FACTURE =====");
        List<Cupcake> cupcakes = commande.getCupcakes();

        if (cupcakes.isEmpty()) {
            System.out.println("Votre commande est vide.");
            return;
        }

        for (int i = 0; i < cupcakes.size(); i++) {
            System.out.println((i+1) + ". " + cupcakes.get(i) + " (" + cupcakes.get(i).calculerPrix() + "€)");
        }

        System.out.println("\nTotal : " + commande.calculerTotal() + "€");
        System.out.println("\nMerci d'avoir commandé chez Cupcake Factory !");
    }
}
