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
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.component.calendar.Calendar;

/**
 *
 * @author KERK
 */
@Named
@ViewScoped
public class PendingOrderManagedBean implements Serializable {

    @EJB(name = "OrderEntityControllerLocal")
    private OrderEntityControllerLocal orderEntityControllerLocal;
    
    private List<OrderEntity> orderEntities;
//    private List<OrderEntity> filteredOrderEntities;
    private List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities;
    private OrderEntity newOrderEntity;
    private OrderEntity selectedOrderEntityToView;
    private boolean value;

    /**
     * Creates a new instance of completedOrderManagedBean
     */
    public PendingOrderManagedBean() {
        orderEntities = new ArrayList<>();
//        filteredOrderEntities = new ArrayList<>();
        saleTransactionLineItemEntities = new ArrayList<>();
        value = true;

        
        //newOrderEntity = new OrderEntity();
    }
    
    @PostConstruct
    public void postConstruct()
    {
        orderEntities = orderEntityControllerLocal.retrieveAllPendingOrders();
//        filteredOrderEntities = orderEntities;
        
    }
    
    public void editFulfilled(ValueChangeEvent event) {
        
        OrderEntity newOrderEntity = (OrderEntity) event.getComponent().getAttributes().get("orderFulfilled");
        orderEntityControllerLocal.updateOrder(newOrderEntity);
        System.err.println("THIS IS SPARTAN!");
    }

    public List<OrderEntity> getOrderEntities() {
        return orderEntities;
    }

    public void setProductEntities(List<OrderEntity> orderEntities) {
        this.orderEntities = orderEntities;
    }

//    public List<OrderEntity> getFilteredOrderEntities() {
//        return filteredOrderEntities;
//    }
//
//    public void setFilteredProductEntities(List<OrderEntity> filteredOrderEntities) {
//        this.filteredOrderEntities = filteredOrderEntities;
//    }

    public OrderEntity getNewOrderEntity() {
        return newOrderEntity;
    }

    public void setNewProductEntity(OrderEntity newOrderEntity) {
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

    public boolean getValue() {
        System.err.println(value);
        return value;
    }

    public void setValue(boolean value) {
       System.err.println("THIS IS SET VALUE" + value); 
        this.value = value;
    }
    
    public void setSaleTransactionLineItemEntities() {
        this.saleTransactionLineItemEntities = saleTransactionLineItemEntities;
    }

}
