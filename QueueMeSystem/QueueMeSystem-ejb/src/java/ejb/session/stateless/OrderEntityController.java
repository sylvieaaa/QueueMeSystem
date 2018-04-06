/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OrderEntity;
import entity.SaleTransactionLineItemEntity;
import java.math.BigDecimal;
import java.util.Calendar;
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
        em.persist(orderEntity);
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
    public List<OrderEntity> retrieveAllOrders()
    {
        Query query = em.createQuery("SELECT p FROM OrderEntity p ORDER BY p.orderId ASC");
 
        return query.getResultList();
    }
    
     @Override
    public List<OrderEntity> retrieveAllPendingOrders()
    {
        Query query = em.createQuery("SELECT p FROM OrderEntity p WHERE p.fulfilled = 0");
        
        return query.getResultList();
    }
    
     @Override
    public List<OrderEntity> retrieveAllCompletedOrders()
    {
        Query query = em.createQuery("SELECT p FROM OrderEntity p WHERE p.fulfilled = 1");
        
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
    
    public BigDecimal getEarnings (Long orderId) {
        
        Query query = em.createQuery("SELECT l FROM LineItemEntity l WHERE l.orderId =:inOrderId");
        BigDecimal earnings = BigDecimal.ZERO;
        List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities = query.getResultList();
        for (SaleTransactionLineItemEntity saleTransactionLineItemEntity: saleTransactionLineItemEntities) {
            earnings.add(saleTransactionLineItemEntity.getSubTotal());
        }
        
        return earnings;        
    }
    
    public void removeOrderEntity(OrderEntity orderEntity) {
        
    }
   
    

}
