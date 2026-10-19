package com.example.jpa;

import com.example.jpa.entities.Salle;
import com.example.jpa.entities.Utilisateur;
import com.example.jpa.service.ServiceGestion;

import javax.persistence.EntityManager;
import java.util.List;

public class Application {

    public static void main(String[] args) {
        System.out.println("========================================================================");
        System.out.println(" TP 2 : Gestion des Entités Salle et Utilisateur avec JPA et Hibernate ");
        System.out.println("========================================================================");

        ServiceGestion service = new ServiceGestion();

        try {
            // 1. Création et ajout des Salles
            System.out.println("\n--- [1] Ajout des Salles ---");
            Salle salleA = new Salle("Salle Java", 25);
            Salle salleB = new Salle("Salle Big Data", 40);
            Salle salleC = new Salle("Salle Réseau", 15);

            service.ajouterSalle(salleA);
            service.ajouterSalle(salleB);
            service.ajouterSalle(salleC);
            System.out.println("✔ Salles ajoutées avec succès.");

            // 2. Affichage des Salles
            System.out.println("\n--- [2] Liste de toutes les Salles (findAll) ---");
            List<Salle> salles = service.listerSalles();
            for (Salle s : salles) {
                System.out.println("  -> " + s);
            }

            // 3. Création et ajout des Utilisateurs
            System.out.println("\n--- [3] Ajout des Utilisateurs avec affectation aux Salles ---");
            Utilisateur u1 = new Utilisateur("Oussama FETOUAKI", "oussamafetouaki2@gmail.com", salleA);
            Utilisateur u2 = new Utilisateur("Amine ALAMI", "amine.alami@example.com", salleA);
            Utilisateur u3 = new Utilisateur("Sara BENANI", "sara.benani@example.com", salleB);
            Utilisateur u4 = new Utilisateur("Karim TAZI", "karim.tazi@example.com", salleC);

            service.ajouterUtilisateur(u1);
            service.ajouterUtilisateur(u2);
            service.ajouterUtilisateur(u3);
            service.ajouterUtilisateur(u4);
            System.out.println("✔ Utilisateurs ajoutés avec succès.");

            // 4. Affichage des Utilisateurs
            System.out.println("\n--- [4] Liste de tous les Utilisateurs (findAll) ---");
            List<Utilisateur> utilisateurs = service.listerUtilisateurs();
            for (Utilisateur u : utilisateurs) {
                System.out.println("  -> " + u);
            }

            // 5. Recherche par SalleDao : Salles avec capacité >= 20
            System.out.println("\n--- [5] Recherche spécifique (SalleDao.findByCapaciteMin >= 20) ---");
            List<Salle> grandesSalles = service.getSalleDao().findByCapaciteMin(20);
            for (Salle s : grandesSalles) {
                System.out.println("  -> " + s);
            }

            // 6. Recherche par UtilisateurDao : Utilisateurs dans la 'Salle Java'
            System.out.println("\n--- [6] Recherche spécifique (UtilisateurDao.findBySalleNom('Salle Java')) ---");
            List<Utilisateur> usersJava = service.utilisateursParSalle("Salle Java");
            for (Utilisateur u : usersJava) {
                System.out.println("  -> " + u);
            }

            // 7. Requêtes JPQL avancées (Étape 13)
            System.out.println("\n--- [7] Requêtes JPQL avancées (Étape 13) ---");
            EntityManager em = service.getEntityManager();

            System.out.println("  [JPQL 1] Salles avec capacité > 20 :");
            List<Salle> jpqlSalles = em.createQuery(
                    "SELECT s FROM Salle s WHERE s.capacite > :minCapacite", Salle.class)
                    .setParameter("minCapacite", 20)
                    .getResultList();
            for (Salle s : jpqlSalles) {
                System.out.println("    * " + s);
            }

            System.out.println("  [JPQL 2] Utilisateurs dont le nom contient 'Oussama' :");
            List<Utilisateur> jpqlUsers = em.createQuery(
                    "SELECT u FROM Utilisateur u WHERE u.nom LIKE :pattern", Utilisateur.class)
                    .setParameter("pattern", "%Oussama%")
                    .getResultList();
            for (Utilisateur u : jpqlUsers) {
                System.out.println("    * " + u);
            }

            // 8. Mise à jour (Update via GenericDao)
            System.out.println("\n--- [8] Test Mise à jour (Update via GenericDao) ---");
            salleC.setCapacite(18);
            service.getSalleDao().update(salleC);
            System.out.println("  -> Capacité mise à jour pour Salle Réseau : " + service.getSalleDao().find(salleC.getId()));

            System.out.println("\n========================================================================");
            System.out.println("             ✔ TOUTES LES OPÉRATIONS ONT RÉUSSI AVEC SUCCÈS             ");
            System.out.println("========================================================================");

        } finally {
            service.fermer();
        }
    }
}
