/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.ReviewEntityControllerLocal;
import entity.CustomerEntity;
import entity.ReviewEntity;
import entity.VendorEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import ws.restful.datamodel.ReviewReq;

/**
 * REST Web Service
 *
 * @author User
 */
@Path("Review")
public class ReviewResource {

    ReviewEntityControllerLocal reviewEntityControllerLocal = lookupReviewEntityControllerLocal();

    @Context
    private UriInfo context;
    
    

    /**
     * Creates a new instance of ReviewResource
     */
    public ReviewResource() {
        
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateReview(JAXBElement<ReviewReq> reviewReq) {
       if (reviewReq != null){
           System.out.println(reviewReq.getValue().getCustomerEntity());
            return Response.status(Response.Status.OK).build();
       }
       else{
             return Response.status(Response.Status.BAD_REQUEST).build();
       }
        
    }

    /**
     * PUT method for updating or creating an instance of ReviewResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }

    private ReviewEntityControllerLocal lookupReviewEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ReviewEntityControllerLocal) c.lookup("java:global/QueueMeSystem/QueueMeSystem-ejb/ReviewEntityController!ejb.session.stateless.ReviewEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
