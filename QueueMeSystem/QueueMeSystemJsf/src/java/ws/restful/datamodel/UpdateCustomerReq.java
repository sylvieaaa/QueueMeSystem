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
 * @author KERK
 */
@XmlType(name = "updateCustomerRsp", propOrder = {
    "customerEntity"
})
public class UpdateCustomerReq {
    
    private CustomerEntity customerEntity;
    
    public UpdateCustomerReq() {
    }

    public UpdateCustomerReq(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }
}
