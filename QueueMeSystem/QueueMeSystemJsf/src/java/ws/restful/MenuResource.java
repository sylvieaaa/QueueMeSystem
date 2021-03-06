/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.MenuEntityControllerLocal;
import entity.CategoryEntity;
import entity.MenuEntity;
import entity.MenuItemEntity;
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
import util.exception.MenuNotFoundException;
import ws.restful.datamodel.MenuEntityRsp;

/**
 * REST Web Service
 *
 * @author User
 */
@Path("Menu")
public class MenuResource {

    MenuEntityControllerLocal menuEntityControllerLocal = lookupMenuEntityControllerLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of MenuResource
     */
    public MenuResource() {
    }

    @Path("retrieveMenu")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveMenuEntity(@QueryParam("vendorId") Long vendorId) {
        try {
            System.err.println(vendorId);
            MenuEntity menuEntity = menuEntityControllerLocal.retrieveDefaultMenuByVendorId(vendorId);
            System.out.println("menu here is " + menuEntity);
            menuEntity.setVendorEntity(null);
            for (CategoryEntity categoryEntity : menuEntity.getCategoryEntities()) {
                categoryEntity.setMenuEntity(null);
                for (MenuItemEntity menuItemEntity : categoryEntity.getMenuItemEntities()) {
                    menuItemEntity.getCategoryEntities().clear();
                    menuItemEntity.setVendorEntity(null);
                    menuItemEntity.getSaleTransactionLineItemEntities().clear();
                }
            }

            return Response.status(Status.OK).entity(new MenuEntityRsp(menuEntity)).build();
        } catch (MenuNotFoundException ex) {
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    /**
     * PUT method for updating or creating an instance of MenuResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }

    private MenuEntityControllerLocal lookupMenuEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (MenuEntityControllerLocal) c.lookup("java:global/QueueMeSystem/QueueMeSystem-ejb/MenuEntityController!ejb.session.stateless.MenuEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
