/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.BusinessEntityControllerLocal;
import ejb.session.stateless.EmailControllerLocal;
import entity.BusinessEntity;
import entity.CustomerEntity;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.BusinessEntityNotFoundException;
import util.exception.InvalidLoginCredentialException;
import ws.restful.datamodel.CustomerLoginRsp;

/**
 * REST Web Service
 *
 * @author User
 */
@Path("Customer")
public class CustomerResource {

    EmailControllerLocal emailControllerLocal = lookupEmailControllerLocal();

    BusinessEntityControllerLocal businessEntityControllerLocal = lookupBusinessEntityControllerLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CustomerResource
     */
    public CustomerResource() {
    }

    @Path("login")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response customerLogin(@QueryParam("username") String username, @QueryParam("password") String password) {
        try {
            //TODO return proper representation object
            BusinessEntity businessEntity =  businessEntityControllerLocal.login(username, password);
            if(businessEntity instanceof CustomerEntity) {
                CustomerLoginRsp customerLoginRsp = new CustomerLoginRsp((CustomerEntity) businessEntity);
                return Response.status(Status.OK).entity(customerLoginRsp).build();
            } else {
                return Response.status(Status.UNAUTHORIZED).entity("Create a customer account to make purchase!").build();
            }
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }
    
    @Path("resetPassword")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response customerResetPassword(@QueryParam("username") String username) {
        try {
            //TODO return proper representation object
            emailControllerLocal.forgetPasswordEmail(username);
            return Response.status(Status.OK).build();
        } catch (BusinessEntityNotFoundException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }

    /**
     * PUT method for updating or creating an instance of CustomerResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }

    private BusinessEntityControllerLocal lookupBusinessEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (BusinessEntityControllerLocal) c.lookup("java:global/QueueMeSystem/QueueMeSystem-ejb/BusinessEntityController!ejb.session.stateless.BusinessEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private EmailControllerLocal lookupEmailControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (EmailControllerLocal) c.lookup("java:global/QueueMeSystem/QueueMeSystem-ejb/EmailController!ejb.session.stateless.EmailControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
