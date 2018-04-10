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
@XmlType(name = "customerLoginRsp", propOrder = {
    "customerEntity"
})
public class CustomerLoginRsp {
    private CustomerEntity customerEntity;

    public CustomerLoginRsp() {
    }

    public CustomerLoginRsp(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }
    
    
}
