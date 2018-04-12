/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.datamodel;

import entity.CustomerEntity;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author User
 */
@XmlType(name = "customerRsp", propOrder = {
    "customerEntity"
})
public class CustomerRsp {
    private CustomerEntity customerEntity;

    public CustomerRsp() {
    }

    public CustomerRsp(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }
    
    
}
