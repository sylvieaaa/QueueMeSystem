/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.datamodel;

import entity.OrderEntity;
import java.util.List;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author KERK
 */
@XmlType(name = "orderEntityRsp", propOrder = {
    "orderEntities"
})
public class OrderEntityRsp {
    private List<OrderEntity> orderEntities;
    
    public OrderEntityRsp() {
    }
        
    public OrderEntityRsp(List<OrderEntity> orderEntities) {
        this.orderEntities = orderEntities;
    }

    public List<OrderEntity> getOrderEntities() {
        return orderEntities;
    }

    public void setOrderEntities(List<OrderEntity> orderEntities) {
        this.orderEntities = orderEntities;
    }

    
}
