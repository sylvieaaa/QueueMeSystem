/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.VendorEntityControllerLocal;
import entity.FoodCourtEntity;
import entity.VendorEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author SYLVIA
 */
@Path("Vendor")
public class VendorResource {

    VendorEntityControllerLocal vendorEntityControllerLocal = lookupVendorEntityControllerLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of VendorResource
     */
    public VendorResource() {
    }

    /**
     * Retrieves representation of an instance of ws.restful.VendorResource
     * @return an instance of java.lang.String
     */
    @Path("retrieveVendor")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public String retrieveAllVendors(@QueryParam("foodCourt") FoodCourtEntity foodCourt ) {
        //TODO return proper representation object
        List<VendorEntity> vendors = foodCourt.getVendorEntities();
        
        
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of VendorResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }

    private VendorEntityControllerLocal lookupVendorEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (VendorEntityControllerLocal) c.lookup("java:global/QueueMeSystem/QueueMeSystem-ejb/VendorEntityController!ejb.session.stateless.VendorEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
