/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.VendorEntityControllerLocal;
import entity.FoodCourtEntity;
import entity.MenuEntity;
import entity.MenuItemEntity;
import entity.ReviewEntity;
import entity.VendorEntity;
import java.util.ArrayList;
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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBElement;
import ws.restful.datamodel.ErrorRsp;
import ws.restful.datamodel.VendorReq;
import ws.restful.datamodel.VendorRsp;

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
     * PUT method for updating or creating an instance of CustomerResource
     *
     * @param vendorReq
     * @return
     */
    @Path("retrieveVendors")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllVendorsByFoodCourt(JAXBElement<VendorReq> vendorReq) {
        if ((vendorReq != null) && (vendorReq.getValue() != null)) {

            try {
                FoodCourtEntity foodCourtEntity = vendorReq.getValue().getFoodCourtEntity();
                List<VendorEntity> vendorEntities = vendorEntityControllerLocal.retrieveVendorsByFoodCourt(foodCourtEntity);

                for (VendorEntity vendorEntity : vendorEntities) {
                    vendorEntity.getOrderEntities().clear();
                    vendorEntity.getMenuItemEntities().clear();
                    vendorEntity.getMenuEntities().clear();
                    vendorEntity.setFoodCourtEntity(null);

                    for (ReviewEntity reviewEntity : vendorEntity.getReviewEntities()) {
                        reviewEntity.setCustomerEntity(null);
                        reviewEntity.setVendorEntity(null);
                    }
                }

                return Response.status(Status.OK).entity(new VendorRsp(vendorEntities)).build();
            } catch (Exception ex) {
                ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }

        } else {
            ErrorRsp errorRsp = new ErrorRsp("Invalid create customer request");

            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /**
     * PUT method for updating or creating an instance of VendorResource
     *
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
