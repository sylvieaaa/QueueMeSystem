/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.datamodel;

import entity.CreditCardEntity;
import java.util.List;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "creditCardRsp", propOrder = {
    "creditCardEntities"
})
public class CreditCardRsp {
    private List<CreditCardEntity> creditCardEntities;

    public CreditCardRsp() {
    }

    public CreditCardRsp(List<CreditCardEntity> creditCardEntities) {
        this.creditCardEntities = creditCardEntities;
    }

    public List<CreditCardEntity> getCreditCardEntities() {
        return creditCardEntities;
    }

    public void setCreditCardEntities(List<CreditCardEntity> creditCardEntities) {
        this.creditCardEntities = creditCardEntities;
    }
    
}
