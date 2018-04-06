/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.OrderEntityControllerLocal;
import entity.OrderEntity;
import entity.SaleTransactionLineItemEntity;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.component.calendar.Calendar;

/**
 *
 * @author KERK
 */
@Named
@ViewScoped
public class ManageOrderManagedBean implements Serializable {

    @EJB(name = "OrderEntityControllerLocal")
    private OrderEntityControllerLocal orderEntityControllerLocal;
    
    private List<OrderEntity> pendingOrderEntities;
    private List<OrderEntity> completedOrderEntities;
    private List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities;
    private OrderEntity newOrderEntity;
    private OrderEntity selectedOrderEntityToView;

    /**
     * Creates a new instance of completedOrderManagedBean
     */
    public ManageOrderManagedBean() {
        pendingOrderEntities = new ArrayList<>();
        completedOrderEntities = new ArrayList<>();
        saleTransactionLineItemEntities = new ArrayList<>();
        newOrderEntity = new OrderEntity();
    }
    
    @PostConstruct
    public void postConstruct()
    {
        pendingOrderEntities = orderEntityControllerLocal.retrieveAllPendingOrders();
        completedOrderEntities = orderEntityControllerLocal.retrieveAllCompletedOrders();
        
    }
    
    public void editFulfilled(ActionEvent event) {
        
        newOrderEntity = (OrderEntity) event.getComponent().getAttributes().get("orderFulfilled");
        orderEntityControllerLocal.updateOrder(newOrderEntity);
        pendingOrderEntities.remove(newOrderEntity);
        completedOrderEntities.add(newOrderEntity);
    }

    public List<OrderEntity> getCompletedOrderEntities() {
        return completedOrderEntities;
    }

    public void setCompletedOrderEntities(List<OrderEntity> completedOrderEntities) {
        this.completedOrderEntities = completedOrderEntities;
    }

    public List<OrderEntity> getPendingOrderEntities() {
        return pendingOrderEntities;
    }

    public void setProductEntities(List<OrderEntity> orderEntities) {
        this.pendingOrderEntities = orderEntities;
    }

    public OrderEntity getNewOrderEntity() {
        return newOrderEntity;
    }

    public void setNewOrderEntity(OrderEntity newOrderEntity) {
        this.newOrderEntity = newOrderEntity;
    }

    public OrderEntity getSelectedOrderEntityToView() {
        return selectedOrderEntityToView;
    }

    public void setSelectedOrderEntityToView(OrderEntity selectedOrderEntityToView) {
        this.selectedOrderEntityToView = selectedOrderEntityToView;
    }
    
    public List<SaleTransactionLineItemEntity> getSaleTransactionLineItemEntities() {
        return saleTransactionLineItemEntities;
    }
    
    public void setSaleTransactionLineItemEntities() {
        this.saleTransactionLineItemEntities = saleTransactionLineItemEntities;
    }
 
}
