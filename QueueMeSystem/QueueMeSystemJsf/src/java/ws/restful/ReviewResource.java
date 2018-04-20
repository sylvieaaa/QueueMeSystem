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
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import util.exception.CustomerNotFoundException;
import util.exception.VendorNotFoundException;
import ws.restful.datamodel.ReviewReq;
import ws.restful.datamodel.ReviewRsp;

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
    public Response updateReview(JAXBElement<ReviewReq> jaxbeReviewReq) {
        if ((jaxbeReviewReq != null) && (jaxbeReviewReq.getValue() != null)) {
            ReviewReq reviewReq = jaxbeReviewReq.getValue();
            ReviewEntity reviewEntity = reviewReq.getReviewEntity();
            VendorEntity vendorEntity = reviewReq.getVendorEntity();
            CustomerEntity customerEntity = reviewReq.getCustomerEntity();
            try {
                ReviewEntity review = reviewEntityControllerLocal.createReview(reviewEntity, customerEntity, vendorEntity);
                System.out.println(review.getVendorEntity().getRating());
                Integer rating = review.getVendorEntity().getRating();
                
                return Response.status(Response.Status.OK).entity(""+rating).build();
            } catch (VendorNotFoundException | CustomerNotFoundException ex) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

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
