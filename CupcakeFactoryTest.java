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

    /* Excercice 1 : Test sur le menu */

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


        assertFalse("Le menu doit contenir des bases", menu.getBasesDisponibles().isEmpty());
        assertFalse("Le menu doit contenir des crèmes", menu.getCremesDisponibles().isEmpty());
        assertFalse("Le menu doit contenir des toppings", menu.getToppingsDisponibles().isEmpty());
    }

    @Test
    public void testIngredientEpuiseDisparaitDuMenu() {

        Ingredient ingredient = menu.getIngredientsDisponibles().get(0);
        int stock = ingredient.getStock();


        for (int i = 0; i < stock; i++) {
            ingredient.diminuerStock();
        }


        assertFalse("l'ingredient epuise ne doit plus etre disponible", ingredient.estDisponible());
        assertFalse("l'ingredient epuise ne doit plus apparaitre dans le menu",
                menu.getIngredientsDisponibles().contains(ingredient));
    }

    @Test
    public void testCupcakeDuJourEpuiseDisparaitDuMenu() {

        Cupcake cupcakeDuJour = menu.getCupcakesDuJourDisponibles().get(0);
        int stock = cupcakeDuJour.getStockCupcakeDuJour();


        Commande commande = cupcakeFactory.creerNouvelleCommande();
        for (int i = 0; i < stock; i++) {
            commande.ajouterCupcake(cupcakeDuJour);
        }

        assertFalse("Le cupcake du jour épuisé ne doit plus apparaître dans le menu",
                menu.getCupcakesDuJourDisponibles().contains(cupcakeDuJour));
    }

    @Test
    public void testPlusDeBaseOuCreme() {

        List<Ingredient> bases = menu.getBasesDisponibles();
        for (Ingredient base : bases) {
            while (base.estDisponible()) {
                base.diminuerStock();
            }
        }


        assertFalse("On ne doit plus pouvoir faire de cupcakes sans base", menu.peutFaireCupcakes());

        // Réinitialiser et épuiser toutes les crèmes
        setUp();
        List<Ingredient> cremes = menu.getCremesDisponibles();
        for (Ingredient creme : cremes) {
            while (creme.estDisponible()) {
                creme.diminuerStock();
            }
        }


        assertFalse("on ne doit plus pouvoir faire de cupcakes sans creme", menu.peutFaireCupcakes());
    }

    /* Excercice 2: Tests sur les commandes */

    @Test
    public void testCreationCupcake() {
        Ingredient base = menu.getBasesDisponibles().get(0);
        Ingredient creme = menu.getCremesDisponibles().get(0);

        Cupcake cupcake = new Cupcake(base, creme);
        assertNotNull("On doit pouvoir cree un cupcake avec une base et une crème", cupcake);
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


        double prixAttendu = base.getPrix() + creme.getPrix() + topping1.getPrix();
        assertEquals("Le prix doit comprendre le topping le plus cher seulement", prixAttendu, cupcake.calculerPrix(), 0.01);
    }

    @Test
    public void testPrixCupcakeDuJour() {

        Cupcake cupcakeDuJour = menu.getCupcakesDuJourDisponibles().get(0);


        double prixNormal = cupcakeDuJour.getBase().getPrix() + cupcakeDuJour.getCreme().getPrix();
        if (!cupcakeDuJour.getToppings().isEmpty()) {

            if (cupcakeDuJour.getToppings().size() == 1) {
                prixNormal += cupcakeDuJour.getToppings().get(0).getPrix();
            } else {

                Ingredient topping1 = cupcakeDuJour.getToppings().get(0);
                Ingredient topping2 = cupcakeDuJour.getToppings().get(1);
                prixNormal += Math.max(topping1.getPrix(), topping2.getPrix());
            }
        }


        double prixReduit = prixNormal * 0.6;

        assertEquals("Le prix du cupcake du jour doit être à 60% du prix normal",
                prixReduit, cupcakeDuJour.calculerPrix(), 0.01);
    }

    @Test
    public void testCommandeSixCupcakes() {
        Commande commande = cupcakeFactory.creerNouvelleCommande();


        Ingredient base = menu.getBasesDisponibles().get(0);
        Ingredient creme = menu.getCremesDisponibles().get(0);

        double prixUnitaire = base.getPrix() + creme.getPrix();
        double prixTotal = prixUnitaire * 5; // 5 payant, 1 gratuit


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


        Ingredient base = menu.getBasesDisponibles().get(0);
        Ingredient creme = menu.getCremesDisponibles().get(0);

        double prixCupcakeNormal = base.getPrix() + creme.getPrix();
        double prixCupcakesNormaux = prixCupcakeNormal * 5;

        for (int i = 0; i < 5; i++) {
            Cupcake cupcake = new Cupcake(base, creme);
            commande.ajouterCupcake(cupcake);
        }


        Cupcake cupcakeDuJour = menu.getCupcakesDuJourDisponibles().get(0);
        commande.ajouterCupcake(cupcakeDuJour);


        double prixTotal = prixCupcakesNormaux + cupcakeDuJour.calculerPrix();

        assertEquals("Pour 6 cupcakes dont un du jour, tous doivent être facturés",
                prixTotal, commande.calculerTotal(), 0.01);
    }

    @Test
    public void testCommandeSeptCupcakesAvecUnDuJour() {
        Commande commande = cupcakeFactory.creerNouvelleCommande();


        Ingredient[] bases = menu.getBasesDisponibles().toArray(new Ingredient[0]);
        Ingredient[] cremes = menu.getCremesDisponibles().toArray(new Ingredient[0]);


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


        prixTotal -= prixMoinsCher;


        Cupcake cupcakeDuJour = menu.getCupcakesDuJourDisponibles().get(0);
        commande.ajouterCupcake(cupcakeDuJour);
        prixTotal += cupcakeDuJour.calculerPrix();

        assertEquals("pour 7 cupcakes dont un du jour, le moins cher des 6 normaux doit etre gratuit",
                prixTotal, commande.calculerTotal(), 0.01);
    }
}
