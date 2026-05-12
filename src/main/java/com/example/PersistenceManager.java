/*
 * Esta parte se ha tomado de: http://javanotepad.blogspot.com/2007/05/jpa-entitymanagerfactory-in-web.html
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Mauricio
 */

public class PersistenceManager {

    private static final PersistenceManager INSTANCE = new PersistenceManager();

    private EntityManagerFactory emf;

    private PersistenceManager() {
    }

    public static PersistenceManager getInstance() {
        return INSTANCE;
    }

    public EntityManagerFactory getEntityManagerFactory() {

        if (emf == null) {

            try {

                emf = Persistence.createEntityManagerFactory("mongoPU");

                System.out.println("=================================");
                System.out.println("MongoDB conectado correctamente");
                System.out.println("=================================");

            } catch (Exception e) {

                System.out.println("=================================");
                System.out.println("ERROR conectando MongoDB");
                System.out.println("=================================");

                e.printStackTrace();
            }
        }

        return emf;
    }

    public void closeEntityManagerFactory() {

        try {

            if (emf != null && emf.isOpen()) {
                emf.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}