/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.datamodel;

import entity.CreditCardEntity;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author KERK
 */
@XmlType(name = "creditCardReq", propOrder = {
    "creditCardEntity"
})
public class CreditcardReq {
    private CreditCardEntity creditCardEntity;

    public CreditcardReq() {
    }

    public CreditcardReq(CreditCardEntity creditCardEntity) {
        this.creditCardEntity = creditCardEntity;
    }

    public CreditCardEntity getCreditCardEntity() {
        return creditCardEntity;
    }

    public void setCreditCardEntity(CreditCardEntity creditCardEntity) {
        this.creditCardEntity = creditCardEntity;
    } 
}
