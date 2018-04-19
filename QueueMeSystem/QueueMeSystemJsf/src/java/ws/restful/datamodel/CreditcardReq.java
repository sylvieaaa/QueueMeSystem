/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.datamodel;

import entity.CreditCardEntity;
import entity.CustomerEntity;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author KERK
 */
@XmlType(name = "creditCardReq", propOrder = {
    "cardNum", "cardName", "customerEntity"
})
public class CreditcardReq {
    
    private String cardNum;
    private String cardName;
    private CustomerEntity customerEntity;

    public CreditcardReq() {
    }

    public CreditcardReq(String cardNum, String cardName, CustomerEntity customerEntity) {
        this.cardNum = cardNum;
        this.cardName = cardName;
        this.customerEntity = customerEntity;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }
}
