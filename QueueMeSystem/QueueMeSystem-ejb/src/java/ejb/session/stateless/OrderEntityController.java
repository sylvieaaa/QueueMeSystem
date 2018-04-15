/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.OrderEntity;
import entity.SaleTransactionLineItemEntity;
import entity.VendorEntity;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.OrderNotFoundException;

/**
 *
 * @author User
 */
@Stateless
public class OrderEntityController implements OrderEntityControllerLocal {

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")
    private EntityManager em;

    @Override
    public OrderEntity createOrder(OrderEntity orderEntity) {
        List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities = orderEntity.getSaleTransactionLineItemEntities();
        System.err.println(saleTransactionLineItemEntities);
        for(SaleTransactionLineItemEntity stlie: saleTransactionLineItemEntities) {
            System.err.println(stlie.getSerialNumber() + " in order");
        }
        em.persist(orderEntity);
        for(SaleTransactionLineItemEntity saleTransactionLineItemEntity: saleTransactionLineItemEntities) {
            saleTransactionLineItemEntity.setOrderEntity(orderEntity);
        }
        em.flush();
        em.refresh(orderEntity);
        
        return orderEntity;
    }
    
    @Override
    public void updateOrder(OrderEntity orderEntity) {

        System.err.println("This is orderEntity id: " + orderEntity.getOrderId());
        OrderEntity updateOrder = em.find(OrderEntity.class, orderEntity.getOrderId());
        updateOrder.setFulfilled(Boolean.TRUE);
        em.merge(updateOrder);

    }
    
    @Override
    public List<OrderEntity> retrieveAllOrders (VendorEntity vendorEntity)
    {
        Long vendorId = vendorEntity.getBusinessId();
        Query query = em.createQuery("SELECT p FROM OrderEntity p ORDER BY p.orderId ASC AND p.vendorEntity.businessId = :inVendorId");
        query.setParameter("inVendorId", vendorId);
        return query.getResultList();
    }
    
     @Override
    public List<OrderEntity> retrieveAllPendingOrders(VendorEntity vendorEntity)
    {
        Long vendorId = vendorEntity.getBusinessId();
        Query query = em.createQuery("SELECT p FROM OrderEntity p WHERE p.fulfilled = 0 AND p.vendorEntity.businessId = :inVendorId");
         query.setParameter("inVendorId", vendorId);
        return query.getResultList();
    }
    
     @Override
    public List<OrderEntity> retrieveAllCompletedOrders(VendorEntity vendorEntity)
    {
        Long vendorId = vendorEntity.getBusinessId();
        Query query = em.createQuery("SELECT p FROM OrderEntity p WHERE p.fulfilled = 1 AND p.vendorEntity.businessId = :inVendorId");
         query.setParameter("inVendorId", vendorId);
        return query.getResultList();
    }
    
    
    @Override
    public OrderEntity retrieveOrderByOrderId(Long orderId) throws OrderNotFoundException {
        OrderEntity orderEntity = em.find(OrderEntity.class, orderId);
        
        if(orderEntity != null)
        {
            return orderEntity;
        }
        else
        {
            throw new OrderNotFoundException("Order ID " + orderId + " does not exist!");
        }               
    }
    
    @Override
    public List<SaleTransactionLineItemEntity> retrieveSaleTransactionLineItemEntities(Long orderId) {
        
        Query query = em.createQuery("SELECT l FROM LineItemEntity l WHERE l.orderId =:inOrderId");   
        return query.getResultList();     
    }
    
    @Override
    public BigDecimal getEarnings (Long orderId) {
        
        Query query = em.createQuery("SELECT l FROM LineItemEntity l WHERE l.orderId =:inOrderId");
        BigDecimal earnings = BigDecimal.ZERO;
        List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities = query.getResultList();
        for (SaleTransactionLineItemEntity saleTransactionLineItemEntity: saleTransactionLineItemEntities) {
            earnings.add(saleTransactionLineItemEntity.getSubTotal());
        }
        
        return earnings;        
    }
    
    @Override
    public List<OrderEntity> retrieveCustomerOrders (Long customerId) {
        Query query = em.createQuery("SELECT p FROM OrderEntity p WHERE p.customerEntity.businessId = :inCustomerId");
         query.setParameter("inCustomerId", customerId);
        return query.getResultList();
    }  
}
