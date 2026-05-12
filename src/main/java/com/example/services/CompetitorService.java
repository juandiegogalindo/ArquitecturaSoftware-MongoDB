package com.example.services;

import com.example.PersistenceManager;
import com.example.models.Competitor;
import com.example.models.CompetitorDTO;
import com.example.models.Product;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;

@Path("/competitors")
@Produces(MediaType.APPLICATION_JSON)
public class CompetitorService {

    private Object entityManager;

    @GET
    public Response getAll() {
        EntityManager em = PersistenceManager.getInstance().getEntityManagerFactory().createEntityManager();
        List<Competitor> competitors = null;

        try {
            Query q = em.createQuery("SELECT u FROM Competitor u ORDER BY u.surname ASC");
            competitors = q.getResultList();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            em.close();
        }

        return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .entity(competitors)
                .build();
    }

    @POST
    @Path("/vehicle")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCompetitor(CompetitorDTO competitor) {

        EntityManager entityManager = PersistenceManager
                .getInstance()
                .getEntityManagerFactory()
                .createEntityManager();

        Competitor c = new Competitor();

        JSONObject rta = new JSONObject();

        c.setAddress(competitor.getAddress());
        c.setAge(competitor.getAge());
        c.setCellphone(competitor.getCellphone());
        c.setCity(competitor.getCity());
        c.setCountry(competitor.getCountry());
        c.setName(competitor.getName());
        c.setSurname(competitor.getSurname());
        c.setTelephone(competitor.getTelephone());

        c.setVehicle(competitor.getVehicle());

        try {

            entityManager.getTransaction().begin();

            // PRODUCTO OBLIGATORIO
            if (competitor.getProduct() == null) {

                rta.put("error", "El competidor debe tener un producto");

                return Response.status(400)
                        .entity(rta.toJSONString())
                        .build();
            }

            Product p = new Product();

            p.setName(competitor.getProduct().getName());
            p.setBrand(competitor.getProduct().getBrand());

            // FECHA
            Calendar cal = Calendar.getInstance();

            p.setPurchaseDate(cal);

            c.setProduct(p);

            entityManager.persist(c);

            entityManager.getTransaction().commit();

            rta.put("competitor_id", c.getId());

        } catch (Throwable t) {

            t.printStackTrace();

            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            c = null;

        } finally {

            entityManager.clear();
            entityManager.close();
        }

        return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .entity(rta.toJSONString())
                .build();
    }   

    @GET
    @Path("/a")
    public Response getCompetitorsWithA() {

        EntityManager em = PersistenceManager.getInstance()
                .getEntityManagerFactory()
                .createEntityManager();

        List<Competitor> competitors = null;

        try {

            Query q = em.createQuery(
                    "SELECT c FROM Competitor c WHERE c.name LIKE 'A%'"
            );

            competitors = q.getResultList();

        } catch (Throwable t) {

            t.printStackTrace();

        } finally {

            em.close();
        }

        return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .entity(competitors)
                .build();
    }

    @OPTIONS
    public Response cors(@javax.ws.rs.core.Context HttpHeaders requestHeaders) {
        return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "AUTHORIZATION, content-type, accept")
                .build();
    }
}
