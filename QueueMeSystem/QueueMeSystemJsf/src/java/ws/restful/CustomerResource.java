/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.BusinessEntityControllerLocal;
import ejb.session.stateless.CustomerEntityControllerLocal;
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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBElement;
import util.exception.BusinessEntityNotFoundException;
import util.exception.CreateCustomerException;
import util.exception.InvalidLoginCredentialException;
import ws.restful.datamodel.CreateCustomerReq;
import ws.restful.datamodel.CustomerRsp;
import ws.restful.datamodel.ErrorRsp;
import ws.restful.datamodel.UpdateCustomerReq;
import ws.restful.datamodel.UpdatePasswordReq;

/**
 * REST Web Service
 *
 * @author User
 */
@Path("Customer")
public class CustomerResource {

    EmailControllerLocal emailControllerLocal = lookupEmailControllerLocal();

    BusinessEntityControllerLocal businessEntityControllerLocal = lookupBusinessEntityControllerLocal();

    CustomerEntityControllerLocal customerEntityControllerLocal = lookupCustomerEntityControllerLocal();

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
            BusinessEntity businessEntity = businessEntityControllerLocal.login(username, password);
            if (businessEntity instanceof CustomerEntity) {

                CustomerEntity customerEntity = (CustomerEntity) businessEntity;
                customerEntity.getCreditCardEntities().clear();
                customerEntity.getOrderEntities().clear();
                customerEntity.getReviewEntities().clear();
                customerEntity.getSaleTransactionEntities().clear();
                
                customerEntity.setPassword(null);

                CustomerRsp customerLoginRsp = new CustomerRsp(customerEntity);

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

    @Path("updateCustomer")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomer(JAXBElement<UpdateCustomerReq> jaxbUpdateCustomerReq) {
        if ((jaxbUpdateCustomerReq != null) && (jaxbUpdateCustomerReq.getValue() != null)) {
            try {
                UpdateCustomerReq updateCustomerReq = jaxbUpdateCustomerReq.getValue();

                customerEntityControllerLocal.updateCustomer(updateCustomerReq.getCustomerEntity());

                return Response.status(Response.Status.OK).entity(this).build();
            } catch (Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception Ex").build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request").build();
        }
    }

    /**
     * PUT method for updating or creating an instance of CustomerResource
     *
     * @param createCustomerReq
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(JAXBElement<CreateCustomerReq> createCustomerReq) {
        if ((createCustomerReq != null) && (createCustomerReq.getValue() != null)) {
            try {
                CreateCustomerReq createCustomer = createCustomerReq.getValue();
                System.err.println(createCustomer.getCustomerEntity().getPassword());
                CustomerEntity customerEntity = customerEntityControllerLocal.createCustomer(createCustomer.getCustomerEntity());

                return Response.status(Response.Status.OK).build();
            } catch (CreateCustomerException ex) {
                ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

                return Response.status(Response.Status.BAD_REQUEST).build();
            } catch (Exception ex) {
                ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            ErrorRsp errorRsp = new ErrorRsp("Invalid create customer request");

            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    

    @Path("changePassword")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomerPassword(JAXBElement<UpdatePasswordReq> jaxbUpdatePasswordReq) {
        if ((jaxbUpdatePasswordReq != null) && (jaxbUpdatePasswordReq.getValue() != null)) {
            try {
                UpdatePasswordReq updatePasswordReq = jaxbUpdatePasswordReq.getValue();
                customerEntityControllerLocal.updateCustomerPassword(updatePasswordReq.getCustomerEntity(), updatePasswordReq.getOldPassword(), updatePasswordReq.getNewPassword());
                return Response.status(Response.Status.OK).build();
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception Ex").build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request").build();
        }
    }
    
    @Path("updateToken")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateToken(JAXBElement<UpdateCustomerReq> jaxbUpdateCustomerReq) {
        if ((jaxbUpdateCustomerReq != null) && (jaxbUpdateCustomerReq.getValue() != null)) {
            try {
                UpdateCustomerReq updateCustomerReq = jaxbUpdateCustomerReq.getValue();
                CustomerEntity customerEntity = updateCustomerReq.getCustomerEntity();
                customerEntityControllerLocal.updateToken(customerEntity);
                return Response.status(Status.OK).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return Response.status(Status.EXPECTATION_FAILED).build();
        }
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

    private CustomerEntityControllerLocal lookupCustomerEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CustomerEntityControllerLocal) c.lookup("java:global/QueueMeSystem/QueueMeSystem-ejb/CustomerEntityController!ejb.session.stateless.CustomerEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}

