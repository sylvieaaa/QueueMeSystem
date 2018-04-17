/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.CustomerEntityControllerLocal;
import ejb.session.stateless.MenuItemEntityControllerLocal;
import ejb.session.stateless.SaleTransactionEntityControllerLocal;
import entity.CustomerEntity;
import entity.MenuItemEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBElement;
import ws.restful.datamodel.CheckoutReq;

/**
 * REST Web Service
 *
 * @author User
 */
@Path("Checkout")
public class CheckoutResource {

    MenuItemEntityControllerLocal menuItemEntityControllerLocal = lookupMenuItemEntityControllerLocal();

    CustomerEntityControllerLocal customerEntityControllerLocal = lookupCustomerEntityControllerLocal();

    SaleTransactionEntityControllerLocal saleTransactionEntityControllerLocal = lookupSaleTransactionEntityControllerLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CheckoutResource
     */
    public CheckoutResource() {
    }

    /**
     * Retrieves representation of an instance of ws.restful.CheckoutResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response doCheckout(JAXBElement<CheckoutReq> jaxbeCheckoutReq) {
        if ((jaxbeCheckoutReq != null) && (jaxbeCheckoutReq.getValue() != null)) {
            try {
                SaleTransactionEntity saleTransactionEntity = jaxbeCheckoutReq.getValue().getSaleTransactionEntity();
                CustomerEntity customerEntity = jaxbeCheckoutReq.getValue().getCustomerEntity();
                
                customerEntity = customerEntityControllerLocal.retrieveCustomerByUsername(customerEntity.getUsername());
                
                SaleTransactionEntity newSaleTransactionEntity = new SaleTransactionEntity();
                newSaleTransactionEntity.setTotalAmount(saleTransactionEntity.getTotalAmount());
                newSaleTransactionEntity.setTotalLineItem(saleTransactionEntity.getTotalLineItem());
                newSaleTransactionEntity.setTotalQuantity(saleTransactionEntity.getTotalQuantity());
                newSaleTransactionEntity.setIsTakeaway(saleTransactionEntity.getIsTakeaway());
                newSaleTransactionEntity.setIsVoided(Boolean.FALSE);
                newSaleTransactionEntity.setTransactionDateTime(saleTransactionEntity.getTransactionDateTime());
                newSaleTransactionEntity.setCustomerEntity(customerEntity);
                
                for(SaleTransactionLineItemEntity stlie: saleTransactionEntity.getSaleTransactionLineItemEntities()) {
                    MenuItemEntity menuItemEntity = menuItemEntityControllerLocal.retrieveMenuItemById(stlie.getMenuItemEntity().getMenuItemId());
                    stlie.setMenuItemEntity(menuItemEntity);
                }
                newSaleTransactionEntity.setSaleTransactionLineItemEntities(saleTransactionEntity.getSaleTransactionLineItemEntities());
                
                
                saleTransactionEntity = saleTransactionEntityControllerLocal.createSaleTransaction(newSaleTransactionEntity);
                return Response.status(Status.OK).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return Response.status(Status.BAD_REQUEST).build();
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

    private CustomerEntityControllerLocal lookupCustomerEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CustomerEntityControllerLocal) c.lookup("java:global/QueueMeSystem/QueueMeSystem-ejb/CustomerEntityController!ejb.session.stateless.CustomerEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private MenuItemEntityControllerLocal lookupMenuItemEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (MenuItemEntityControllerLocal) c.lookup("java:global/QueueMeSystem/QueueMeSystem-ejb/MenuItemEntityController!ejb.session.stateless.MenuItemEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
