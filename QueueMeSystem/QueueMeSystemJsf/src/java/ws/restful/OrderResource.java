/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.OrderEntityControllerLocal;
import entity.OrderEntity;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import ws.restful.datamodel.ErrorRsp;
import ws.restful.datamodel.OrderEntityRsp;

/**
 * REST Web Service
 *
 * @author KERK
 */
@Path("Order")
public class OrderResource {

    OrderEntityControllerLocal orderEntityControllerLocal = lookupOrderEntityControllerLocal();

    @Context
    private UriInfo context;

    
    public OrderResource() {
    }
    
    @Path("retrieveAllOrders")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllOrders(@QueryParam("customerId") Long customerId) {
        try {
            List<OrderEntity> orderEntities = orderEntityControllerLocal.retrieveCustomerOrders(customerId);
            for (OrderEntity orderEntity : orderEntities) {
               orderEntity.getSaleTransactionLineItemEntities().clear();
               orderEntity.setVendorEntity(null);
               orderEntity.setCustomerEntity(null);            
            }
            return Response.status(Status.OK).entity(new OrderEntityRsp(orderEntities)).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new ErrorRsp(ex.getMessage())).build();
        }
    }


    private OrderEntityControllerLocal lookupOrderEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (OrderEntityControllerLocal) c.lookup("java:global/QueueMeSystem/QueueMeSystem-ejb/OrderEntityController!ejb.session.stateless.OrderEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
