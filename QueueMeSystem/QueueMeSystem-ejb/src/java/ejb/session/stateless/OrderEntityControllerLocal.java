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
    
    public List<OrderEntity> retrieveAllOrders(VendorEntity vendorEntity);
    
    public List<OrderEntity> retrieveAllPendingOrders(VendorEntity vendorEntity);
    
    public List<OrderEntity> retrieveAllCompletedOrders(VendorEntity vendorEntity);
    
    public OrderEntity retrieveOrderByOrderId(Long orderId) throws OrderNotFoundException;
    
    public List<SaleTransactionLineItemEntity> retrieveSaleTransactionLineItemEntities(Long orderId);
    
    public BigDecimal getEarnings (Long orderId);

//    public List<OrderEntity> retrieveCustomerOrders(Long customerId);

    public List<OrderEntity> retrieveCustomerOrders(Long saleTransactionId, Long customerId);
  
}
