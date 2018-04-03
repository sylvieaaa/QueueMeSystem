/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.OrderEntityControllerLocal;
import entity.OrderEntity;
import entity.SaleTransactionLineItemEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author KERK
 */
@Named(value = "completedOrderManagedBean")
@ViewScoped
public class CompletedOrderManagedBean implements Serializable {

    @EJB(name = "OrderEntityControllerLocal")
    private OrderEntityControllerLocal orderEntityControllerLocal;
    
    
    
    private List<OrderEntity> orderEntities;
    private List<OrderEntity> filteredOrderEntities;
    private List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities;
    private OrderEntity newOrderEntity;
    private OrderEntity selectedOrderEntityToView;

    
    public CompletedOrderManagedBean() {
        orderEntities = new ArrayList<>();
        filteredOrderEntities = new ArrayList<>();
        saleTransactionLineItemEntities = new ArrayList<>();

        
        newOrderEntity = new OrderEntity();
    }
    
@PostConstruct
    public void postConstruct()
    {
        orderEntities = orderEntityControllerLocal.retrieveAllCompletedOrders();
        filteredOrderEntities = orderEntities;
        
    }
    
    public List<OrderEntity> getOrderEntities() {
        return orderEntities;
    }

    public void setProductEntities(List<OrderEntity> orderEntities) {
        this.orderEntities = orderEntities;
    }

    public List<OrderEntity> getFilteredOrderEntities() {
        return filteredOrderEntities;
    }

    public void setFilteredProductEntities(List<OrderEntity> filteredOrderEntities) {
        this.filteredOrderEntities = filteredOrderEntities;
    }

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
    
    public void setSaleTransactionLineItemEntities() {
        this.saleTransactionLineItemEntities = saleTransactionLineItemEntities;
    }
}

