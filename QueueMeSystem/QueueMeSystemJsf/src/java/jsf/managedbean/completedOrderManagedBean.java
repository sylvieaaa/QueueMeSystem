/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.OrderEntityControllerLocal;
import entity.OrderEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author KERK
 */
@Named(value = "completedOrderManagedBean")
@ViewScoped
public class completedOrderManagedBean implements Serializable {

    @EJB(name = "OrderEntityControllerLocal")
    private OrderEntityControllerLocal orderEntityControllerLocal;
    
    private List<OrderEntity> orderEntities;
    private List<OrderEntity> filteredOrderEntities;
    private OrderEntity newOrderEntity;
    private OrderEntity selectedOrderEntityToView;
    private OrderEntity selectedOrderEntityToUpdate;

    /**
     * Creates a new instance of completedOrderManagedBean
     */
    public completedOrderManagedBean() {
        orderEntities = new ArrayList<>();
        filteredOrderEntities = new ArrayList<>();
        
        newOrderEntity = new OrderEntity();
    }
    
    @PostConstruct
    public void postConstruct()
    {
        orderEntities = orderEntityControllerLocal.retrieveAllOrders();
        filteredOrderEntities = orderEntities;
    }
    
    public void viewOrderDetails(ActionEvent event) throws IOException
    {
        Long orderIdToView = (Long)event.getComponent().getAttributes().get("orderId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("orderIdToView", orderIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewOrderDetails.xhtml");
    }
    
}
