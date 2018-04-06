/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OrderEntity;
import entity.SaleTransactionLineItemEntity;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;
import util.exception.OrderNotFoundException;

/**
 *
 * @author User
 */
@Local
public interface OrderEntityControllerLocal {

    public void updateOrder(OrderEntity orderEntity);

    public OrderEntity createOrder(OrderEntity orderEntity);
    
    public List<OrderEntity> retrieveAllOrders();
    
    public List<OrderEntity> retrieveAllPendingOrders();
    
    public List<OrderEntity> retrieveAllCompletedOrders();
    
    public OrderEntity retrieveOrderByOrderId(Long orderId) throws OrderNotFoundException;
    
    public List<SaleTransactionLineItemEntity> retrieveSaleTransactionLineItemEntities(Long orderId);
    
    public BigDecimal getEarnings (Long orderId);
  
}
