/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OrderEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
        em.merge(orderEntity);
    }
    
     @Override
    public List<OrderEntity> retrieveAllOrders()
    {
        Query query = em.createQuery("SELECT p FROM OrderEntity p ORDER BY p.orderId ASC");
        
        return query.getResultList();
    }
}
