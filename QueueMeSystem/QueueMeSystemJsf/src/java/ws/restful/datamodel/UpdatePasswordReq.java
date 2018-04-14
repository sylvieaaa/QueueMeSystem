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
@XmlType(name = "updatePasswordReq", propOrder = {
    "customerEntity", "oldPassword", "newPassword"
})

public class UpdatePasswordReq {
    
    private CustomerEntity customerEntity;
    private String oldPassword;
    private String newPassword;

    public UpdatePasswordReq() {
    }

    public UpdatePasswordReq(CustomerEntity customerEntity, String oldPassword, String newPassword) {
        this.customerEntity = customerEntity;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    
    
}
