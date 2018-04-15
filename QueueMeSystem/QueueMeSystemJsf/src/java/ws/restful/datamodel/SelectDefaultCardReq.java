/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.datamodel;

import entity.CreditCardEntity;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "selectDefaultCardReq", propOrder = {
    "creditCardEntity"
})
public class SelectDefaultCardReq {
    private CreditCardEntity creditCardEntity;

    public SelectDefaultCardReq() {
    }

    public SelectDefaultCardReq(CreditCardEntity creditCardEntity) {
        this.creditCardEntity = creditCardEntity;
    }
    
    public CreditCardEntity getCreditCardEntity() {
        return creditCardEntity;
    }

    public void setCreditCardEntity(CreditCardEntity creditCardEntity) {
        this.creditCardEntity = creditCardEntity;
    }

}
