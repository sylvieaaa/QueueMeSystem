/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.OrderEntityControllerLocal;
import ejb.session.stateless.SaleTransactionEntityControllerLocal;
import entity.MenuItemEntity;
import entity.OrderEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import ws.restful.datamodel.ErrorRsp;
import ws.restful.datamodel.OrderEntityRsp;
import ws.restful.datamodel.SaleTransactionRsp;

/**
 * REST Web Service
 *
 * @author KERK
 */
@Path("Order")
public class OrderResource {

    SaleTransactionEntityControllerLocal saleTransactionEntityControllerLocal = lookupSaleTransactionEntityControllerLocal();

    OrderEntityControllerLocal orderEntityControllerLocal = lookupOrderEntityControllerLocal();

    @Context
    private UriInfo context;

    
    public OrderResource() {
    }
    
    @Path("retrieveAllSaleTransactions")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllSaleTransactions(@QueryParam("customerId") Long customerId) {
        try {
            List<SaleTransactionEntity> saleTransactionEntities = saleTransactionEntityControllerLocal.retrieveSaleTransaction(customerId);
            for (SaleTransactionEntity saleTransactionEntity : saleTransactionEntities) {
               saleTransactionEntity.setCustomerEntity(null);       
               for (SaleTransactionLineItemEntity saleTransactionLineItem : saleTransactionEntity.getSaleTransactionLineItemEntities()) {
                   
                   saleTransactionLineItem.setSaleTransactionEntity(null);
                   saleTransactionLineItem.setOrderEntity(null);
     
                   MenuItemEntity menuItem = saleTransactionLineItem.getMenuItemEntity();
                   menuItem.getSaleTransactionLineItemEntities().clear();
                   
                   VendorEntity ve  = menuItem.getVendorEntity();
                   ve.getMenuItemEntities().clear();
                   ve.getReviewEntities().clear();
                   ve.getOrderEntities().clear();
                   ve.getMenuEntities().clear();
                   
                   ve.getFoodCourtEntity().getVendorEntities().clear();
                   
                   menuItem.getCategoryEntities().clear();                
               } 
            }
            System.err.println("size of saleTrasanctionEntities: " + saleTransactionEntities.size());
            for (SaleTransactionEntity abc : saleTransactionEntities) {
                System.err.println("size of lineItemEntity: " + abc.getSaleTransactionLineItemEntities().size());
            }
            return Response.status(Status.OK).entity(new SaleTransactionRsp(saleTransactionEntities)).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new ErrorRsp(ex.getMessage())).build();
        }
    }
    
    @Path("retrieveAllOrders")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllOrders(@QueryParam("saleTransactionId") Long saleTransactionId) {
        try {
            List<OrderEntity> orderEntities = orderEntityControllerLocal.retrieveCustomerOrders(saleTransactionId);
            for (OrderEntity oe : orderEntities) {
                oe.setCustomerEntity(null);
                oe.getVendorEntity().getOrderEntities().clear();
               
                for (SaleTransactionLineItemEntity stlie: oe.getSaleTransactionLineItemEntities()) {
                    
                    stlie.setOrderEntity(null);
                    stlie.setSaleTransactionEntity(null);
                    
                    MenuItemEntity menuItem = stlie.getMenuItemEntity();
                    menuItem.getSaleTransactionLineItemEntities().clear();
                    menuItem.getCategoryEntities().clear();    
                    
                    VendorEntity ve = menuItem.getVendorEntity();
                    ve.getMenuItemEntities().clear();
                    ve.setFoodCourtEntity(null);
                    ve.getMenuEntities().clear();
                    ve.getReviewEntities().clear();
                    ve.getOrderEntities().clear();
                              
                }         
               }
            System.err.println("Response status 200 ok!");
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

    private SaleTransactionEntityControllerLocal lookupSaleTransactionEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (SaleTransactionEntityControllerLocal) c.lookup("java:global/QueueMeSystem/QueueMeSystem-ejb/SaleTransactionEntityController!ejb.session.stateless.SaleTransactionEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
