/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.FoodCourtEntityControllerLocal;
import entity.FoodCourtEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ws.restful.datamodel.FoodCourtRsp;

/**
 * REST Web Service
 *
 * @author SYLVIA
 */
@Path("FoodCourt")
public class FoodCourtResource {

    FoodCourtEntityControllerLocal foodCourtEntityControllerLocal = lookupFoodCourtEntityControllerLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of FoodCourtResource
     */
    public FoodCourtResource() {
    }

    /**
     * Retrieves representation of an instance of ws.restful.FoodCourtResource
     *
     * @return an instance of java.lang.String
     */
    @Path("retrieveAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllFoodCourts() {
        List<FoodCourtEntity> foodCourts = foodCourtEntityControllerLocal.retrieveAllFoodCourts();
        for (FoodCourtEntity foodcourt : foodCourts) {
            foodcourt.getVendorEntities().clear();

        }
        FoodCourtRsp foodCourtRsp = new FoodCourtRsp(foodCourts);

        return Response.status(Response.Status.OK).entity(foodCourtRsp).build();
    }

    /**
     * PUT method for updating or creating an instance of FoodCourtResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }

    private FoodCourtEntityControllerLocal lookupFoodCourtEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (FoodCourtEntityControllerLocal) c.lookup("java:global/QueueMeSystem/QueueMeSystem-ejb/FoodCourtEntityController!ejb.session.stateless.FoodCourtEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
