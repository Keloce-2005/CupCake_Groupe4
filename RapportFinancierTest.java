package org.example.modele;

import static org.junit.Assert.*;

import org.example.modele.*;
import org.junit.Before;
import org.junit.Test;


public class RapportFinancierTest {
    private org.example.modele.CupcakeFactory cupcakeFactory;
    private Base baseTest;
    private Creme cremeTest;
    private Topping toppingTest;

    @Before
    public void setUp() {

        baseTest = new Base("Test Base", 2.0, 20);
        cremeTest = new Creme("Test Crème", 1.5, 20);
        toppingTest = new Topping("Test Topping", 0.5, 20);


        cupcakeFactory = new CupcakeFactory();
    }


    @Test
    public void testChiffreAffairesInitial() {
        assertEquals("Le chiffre d'affaires initial devrait être zéro",
                0.0, cupcakeFactory.getChiffreAffaires(), 0.001);
    }


    @Test
    public void testChiffreAffairesApresCommande() {

        Commande commande = cupcakeFactory.creerCommande();


        Cupcake cupcake = new Cupcake(baseTest, cremeTest);
        commande.getCupcakes().add(cupcake);


        double prixCommande = cupcakeFactory.finaliserCommande(commande);


        assertEquals("Le chiffre d'affaires devrait être égal au prix de la commande",
                prixCommande, cupcakeFactory.getChiffreAffaires(), 0.001);
    }


    @Test
    public void testChiffreAffairesAvecPlusieursCommandes() {
        double chiffreAffairesTotal = 0.0;


        for (int i = 0; i < 3; i++) {

            Commande commande = cupcakeFactory.creerCommande();


            Cupcake cupcake = new Cupcake(baseTest, cremeTest);
            commande.getCupcakes().add(cupcake);


            double prixCommande = cupcakeFactory.finaliserCommande(commande);
            chiffreAffairesTotal += prixCommande;
        }


        assertEquals("Le chiffre d'affaires devrait être la somme des trois commandes",
                chiffreAffairesTotal, cupcakeFactory.getChiffreAffaires(), 0.001);
    }


    @Test
    public void testChiffreAffairesAvecCupcakesDuJour() {

        Commande commande = cupcakeFactory.creerCommande();


        CupcakeDuJour cupcakeDuJour = new CupcakeDuJour(baseTest, cremeTest, 5);
        commande.getCupcakes().add(cupcakeDuJour);


        double prixNormal = baseTest.getCout() + cremeTest.getCout();
        double prixReduit = prixNormal * 0.6;


        double prixCommande = cupcakeFactory.finaliserCommande(commande);


        assertEquals("Le chiffre d'affaires devrait inclure la réduction",
                prixReduit, cupcakeFactory.getChiffreAffaires(), 0.001);
    }


    @Test
    public void testChiffreAffairesAvecPromotion6emeCupcake() {

        Commande commande = cupcakeFactory.creerCommande();


        for (int i = 0; i < 6; i++) {
            Cupcake cupcake = new Cupcake(baseTest, cremeTest);
            commande.getCupcakes().add(cupcake);
        }


        double prixCupcake = baseTest.getCout() + cremeTest.getCout();
        double prixAttendu = prixCupcake * 5;


        double prixCommande = cupcakeFactory.finaliserCommande(commande);


        assertEquals("Le chiffre d'affaires devrait inclure la promotion du 6ème offert",
                prixAttendu, cupcakeFactory.getChiffreAffaires(), 0.001);
    }


    @Test
    public void testChiffreAffairesAvecPromotionsCombinees() {

        Commande commande = cupcakeFactory.creerCommande();


        for (int i = 0; i < 6; i++) {
            Cupcake cupcake = new Cupcake(baseTest, cremeTest);
            commande.getCupcakes().add(cupcake);
        }


        CupcakeDuJour cupcakeDuJour = new CupcakeDuJour(baseTest, cremeTest, 5);
        commande.getCupcakes().add(cupcakeDuJour);


        double prixCupcakeNormal = baseTest.getCout() + cremeTest.getCout();
        double prixCupcakeDuJour = prixCupcakeNormal * 0.6;
        double prixAttendu = (prixCupcakeNormal * 5) + prixCupcakeDuJour;


        double prixCommande = cupcakeFactory.finaliserCommande(commande);


        assertEquals("Le chiffre d'affaires devrait inclure les deux promotions",
                prixAttendu, cupcakeFactory.getChiffreAffaires(), 0.001);
    }
}