/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.CreditCardEntityControllerLocal;
import entity.CreditCardEntity;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import ws.restful.datamodel.CreditCardRsp;
import ws.restful.datamodel.ErrorRsp;

/**
 * REST Web Service
 *
 * @author User
 */
@Path("CreditCard")
public class CreditCardResource {

    CreditCardEntityControllerLocal creditCardEntityControllerLocal = lookupCreditCardEntityControllerLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CreditCardResource
     */
    public CreditCardResource() {
    }

    /**
     * Retrieves representation of an instance of ws.restful.CreditCardResource
     * @return an instance of java.lang.String
     */
    @Path("retrieveCreditCards")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllCreditCards(@QueryParam("customerId") Long customerId) {
        try {
            List<CreditCardEntity> creditCardEntities = creditCardEntityControllerLocal.retrieveAllCreditCards(customerId);
            for(CreditCardEntity creditCardEntity: creditCardEntities) {
                creditCardEntity.setCustomerEntity(null);
            }
            return Response.status(Status.OK).entity(new CreditCardRsp(creditCardEntities)).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new ErrorRsp(ex.getMessage())).build();
        }
    }

    /**
     * PUT method for updating or creating an instance of CreditCardResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }

    private CreditCardEntityControllerLocal lookupCreditCardEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CreditCardEntityControllerLocal) c.lookup("java:global/QueueMeSystem/QueueMeSystem-ejb/CreditCardEntityController!ejb.session.stateless.CreditCardEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
