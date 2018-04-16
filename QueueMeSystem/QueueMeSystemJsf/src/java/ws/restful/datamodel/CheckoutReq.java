/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.datamodel;

import entity.CustomerEntity;
import entity.SaleTransactionEntity;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "checkoutReq", propOrder = {
    "saleTransactionEntity",
    "customerEntity"
})
public class CheckoutReq {
    private SaleTransactionEntity saleTransactionEntity;
    private CustomerEntity customerEntity;

    public CheckoutReq() {
    }

    public CheckoutReq(SaleTransactionEntity saleTransactionEntity, CustomerEntity customerEntity) {
        this.saleTransactionEntity = saleTransactionEntity;
        this.customerEntity = customerEntity;
    }

    public SaleTransactionEntity getSaleTransactionEntity() {
        return saleTransactionEntity;
    }

    public void setSaleTransactionEntity(SaleTransactionEntity saleTransactionEntity) {
        this.saleTransactionEntity = saleTransactionEntity;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }
    
}
