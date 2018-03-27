/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OrderEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author User
 */
@Local
public interface OrderEntityControllerLocal {

    public void updateOrder(OrderEntity orderEntity);

    public OrderEntity createOrder(OrderEntity orderEntity);
    
    public List<OrderEntity> retrieveAllOrders();
    
}
