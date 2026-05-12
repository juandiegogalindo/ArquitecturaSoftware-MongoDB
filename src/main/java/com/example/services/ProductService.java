/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.services;

import com.example.PersistenceManager;
import com.example.models.Product;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jdgal
 */

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductService {

    @GET
    public Response getProducts() {

        EntityManager em = PersistenceManager.getInstance()
                .getEntityManagerFactory()
                .createEntityManager();

        List<Product> products = null;

        try {

            Query q = em.createQuery("SELECT p FROM Product p");

            products = q.getResultList();

        } catch (Throwable t) {

            t.printStackTrace();

        } finally {

            em.close();
        }

        return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .entity(products)
                .build();
    }
}