import org.example.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CupcakeFactoryTest {

    private CupcakeFactory cupcakeFactory;
    private Menu menu;

    @Before
    public void setUp() {
        cupcakeFactory = new CupcakeFactory();
        menu = cupcakeFactory.getMenu();
    }

    // Excercice 1: Tests sur le menu

    @Test
    public void testMenuContientCupcakesDuJour() {
        List<Cupcake> cupcakesDuJour = menu.getCupcakesDuJourDisponibles();
        assertFalse("Le menu doit contenir des cupcakes du jour", cupcakesDuJour.isEmpty());
        assertEquals("Le menu doit contenir 3 cupcakes du jour", 3, cupcakesDuJour.size());
    }

    @Test
    public void testMenuContientIngredients() {
        List<Ingredient> ingredients = menu.getIngredientsDisponibles();
        assertFalse("Le menu doit contenir des ingredients", ingredients.isEmpty());

        // Vérifier qu'il y a des bases, des crèmes et des toppings
        assertFalse("Le menu doit contenir des bases", menu.getBasesDisponibles().isEmpty());
        assertFalse("Le menu doit contenir des crèmes", menu.getCremesDisponibles().isEmpty());
        assertFalse("Le menu doit contenir des toppings", menu.getToppingsDisponibles().isEmpty());
    }

    @Test
    public void testIngredientEpuiseDisparaitDuMenu() {
        // Récupérer un ingredient et épuiser son stock
        Ingredient ingredient = menu.getIngredientsDisponibles().get(0);
        int stock = ingredient.getStock();

        // Épuiser le stock
        for (int i = 0; i < stock; i++) {
            ingredient.diminuerStock();
        }

        // Vérifier que l'ingredient n'est plus disponible
        assertFalse("L'ingredient épuisé ne doit plus être disponible", ingredient.estDisponible());
        assertFalse("L'ingredient épuisé ne doit plus apparaître dans le menu",
                menu.getIngredientsDisponibles().contains(ingredient));
    }

    @Test
    public void testCupcakeDuJourEpuiseDisparaitDuMenu() {
        // Récupérer un cupcake du jour
        Cupcake cupcakeDuJour = menu.getCupcakesDuJourDisponibles().get(0);
        int stock = cupcakeDuJour.getStockCupcakeDuJour();

        // Créer une commande et épuiser le stock
        Commande commande = cupcakeFactory.creerNouvelleCommande();
        for (int i = 0; i < stock; i++) {
            commande.ajouterCupcake(cupcakeDuJour);
        }

        // Vérifier que le cupcake n'est plus disponible
        assertFalse("Le cupcake du jour épuisé ne doit plus apparaître dans le menu",
                menu.getCupcakesDuJourDisponibles().contains(cupcakeDuJour));
    }

    @Test
    public void testPlusDeBaseOuCreme() {
        // Épuiser toutes les bases
        List<Ingredient> bases = menu.getBasesDisponibles();
        for (Ingredient base : bases) {
            while (base.estDisponible()) {
                base.diminuerStock();
            }
        }

        // Vérifier qu'on ne peut plus faire de cupcakes
        assertFalse("On ne doit plus pouvoir faire de cupcakes sans base", menu.peutFaireCupcakes());

        // Réinitialiser et épuiser toutes les crèmes
        setUp();
        List<Ingredient> cremes = menu.getCremesDisponibles();
        for (Ingredient creme : cremes) {
            while (creme.estDisponible()) {
                creme.diminuerStock();
            }
        }

        // Vérifier qu'on ne peut plus faire de cupcakes
        assertFalse("On ne doit plus pouvoir faire de cupcakes sans crème", menu.peutFaireCupcakes());
    }

    // Excercice 2: Tests sur les commandes

    @Test
    public void testCreationCupcake() {
        Ingredient base = menu.getBasesDisponibles().get(0);
        Ingredient creme = menu.getCremesDisponibles().get(0);

        Cupcake cupcake = new Cupcake(base, creme);
        assertNotNull("On doit pouvoir créer un cupcake avec une base et une crème", cupcake);
        assertEquals("Le cupcake doit avoir la bonne base", base, cupcake.getBase());
        assertEquals("Le cupcake doit avoir la bonne crème", creme, cupcake.getCreme());
    }

    @Test
    public void testCupcakeAvecTroppDeToppings() {
        Ingredient base = menu.getBasesDisponibles().get(0);
        Ingredient creme = menu.getCremesDisponibles().get(0);
        Cupcake cupcake = new Cupcake(base, creme);

        List<Ingredient> toppings = menu.getToppingsDisponibles();
        assertTrue("On doit pouvoir ajouter un premier topping", cupcake.ajouterTopping(toppings.get(0)));
        assertTrue("On doit pouvoir ajouter un deuxième topping", cupcake.ajouterTopping(toppings.get(1)));
        assertFalse("On ne doit pas pouvoir ajouter un troisième topping", cupcake.ajouterTopping(toppings.get(2)));
    }

    @Test
    public void testPrixCupcakeSimple() {
        Ingredient base = menu.getBasesDisponibles().get(0);
        Ingredient creme = menu.getCremesDisponibles().get(0);
        Cupcake cupcake = new Cupcake(base, creme);

        double prixAttendu = base.getPrix() + creme.getPrix();
        assertEquals("Le prix du cupcake doit être la somme de ses ingrédients", prixAttendu, cupcake.calculerPrix(), 0.01);
    }

    @Test
    public void testPrixCupcakeAvecDeuxToppings() {
        Ingredient base = menu.getBasesDisponibles().get(0);
        Ingredient creme = menu.getCremesDisponibles().get(0);
        Cupcake cupcake = new Cupcake(base, creme);

        List<Ingredient> toppings = menu.getToppingsDisponibles();
        Ingredient topping1 = toppings.get(0); // Pépites de chocolat (0.8€)
        Ingredient topping2 = toppings.get(1); // Vermicelles (0.5€)

        cupcake.ajouterTopping(topping1);
        cupcake.ajouterTopping(topping2);

        // Le topping le moins cher doit être offert (vermicelles à 0.5€)
        double prixAttendu = base.getPrix() + creme.getPrix() + topping1.getPrix();
        assertEquals("Le prix doit comprendre le topping le plus cher seulement", prixAttendu, cupcake.calculerPrix(), 0.01);
    }

    @Test
    public void testPrixCupcakeDuJour() {
        // Récupérer un cupcake du jour
        Cupcake cupcakeDuJour = menu.getCupcakesDuJourDisponibles().get(0);

        // Calculer le prix normal sans la réduction
        double prixNormal = cupcakeDuJour.getBase().getPrix() + cupcakeDuJour.getCreme().getPrix();
        if (!cupcakeDuJour.getToppings().isEmpty()) {
            // Si il y a un seul topping, on l'ajoute
            if (cupcakeDuJour.getToppings().size() == 1) {
                prixNormal += cupcakeDuJour.getToppings().get(0).getPrix();
            } else {
                // Si deux toppings, trouver le plus cher
                Ingredient topping1 = cupcakeDuJour.getToppings().get(0);
                Ingredient topping2 = cupcakeDuJour.getToppings().get(1);
                prixNormal += Math.max(topping1.getPrix(), topping2.getPrix());
            }
        }

        // Prix avec 60% de réduction
        double prixReduit = prixNormal * 0.6;

        assertEquals("Le prix du cupcake du jour doit être à 60% du prix normal",
                prixReduit, cupcakeDuJour.calculerPrix(), 0.01);
    }

    @Test
    public void testCommandeSixCupcakes() {
        Commande commande = cupcakeFactory.creerNouvelleCommande();

        // Créer 6 cupcakes identiques pour simplifier
        Ingredient base = menu.getBasesDisponibles().get(0);
        Ingredient creme = menu.getCremesDisponibles().get(0);

        double prixUnitaire = base.getPrix() + creme.getPrix();
        double prixTotal = prixUnitaire * 5; // 5 payant, 1 gratuit

        // Ajouter 6 cupcakes
        for (int i = 0; i < 6; i++) {
            Cupcake cupcake = new Cupcake(base, creme);
            commande.ajouterCupcake(cupcake);
        }

        assertEquals("Pour 6 cupcakes normaux, le 6ème doit être gratuit",
                prixTotal, commande.calculerTotal(), 0.01);
    }

    @Test
    public void testCommandeSixCupcakesAvecUnDuJour() {
        Commande commande = cupcakeFactory.creerNouvelleCommande();

        // Ajouter 5 cupcakes normaux
        Ingredient base = menu.getBasesDisponibles().get(0);
        Ingredient creme = menu.getCremesDisponibles().get(0);

        double prixCupcakeNormal = base.getPrix() + creme.getPrix();
        double prixCupcakesNormaux = prixCupcakeNormal * 5;

        for (int i = 0; i < 5; i++) {
            Cupcake cupcake = new Cupcake(base, creme);
            commande.ajouterCupcake(cupcake);
        }

        // Ajouter un cupcake du jour
        Cupcake cupcakeDuJour = menu.getCupcakesDuJourDisponibles().get(0);
        commande.ajouterCupcake(cupcakeDuJour);

        // Le prix total doit inclure les 5 cupcakes normaux + le cupcake du jour (la promotion 5+1 ne s'applique pas)
        double prixTotal = prixCupcakesNormaux + cupcakeDuJour.calculerPrix();

        assertEquals("Pour 6 cupcakes dont un du jour, tous doivent être facturés",
                prixTotal, commande.calculerTotal(), 0.01);
    }

    @Test
    public void testCommandeSeptCupcakesAvecUnDuJour() {
        Commande commande = cupcakeFactory.creerNouvelleCommande();

        // Ajouter 6 cupcakes normaux de prix différents
        Ingredient[] bases = menu.getBasesDisponibles().toArray(new Ingredient[0]);
        Ingredient[] cremes = menu.getCremesDisponibles().toArray(new Ingredient[0]);

        // On crée 6 cupcakes avec des configurations différentes pour avoir des prix variés
        double prixTotal = 0;
        double prixMoinsCher = Double.MAX_VALUE;

        for (int i = 0; i < 6; i++) {
            Ingredient base = bases[i % bases.length];
            Ingredient creme = cremes[i % cremes.length];
            Cupcake cupcake = new Cupcake(base, creme);

            double prix = cupcake.calculerPrix();
            if (prix < prixMoinsCher) {
                prixMoinsCher = prix;
            }

            prixTotal += prix;
            commande.ajouterCupcake(cupcake);
        }

        // Soustraire le prix du cupcake le moins cher (qui sera gratuit)
        prixTotal -= prixMoinsCher;

        // Ajouter un cupcake du jour
        Cupcake cupcakeDuJour = menu.getCupcakesDuJourDisponibles().get(0);
        commande.ajouterCupcake(cupcakeDuJour);
        prixTotal += cupcakeDuJour.calculerPrix();

        assertEquals("Pour 7 cupcakes dont un du jour, le moins cher des 6 normaux doit être gratuit",
                prixTotal, commande.calculerTotal(), 0.01);
    }
}
