/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.datamodel;

import entity.SaleTransactionEntity;
import java.util.List;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author KERK
 */
@XmlType(name = "saleTransactionRsp", propOrder = {
    "saleTransactionEntities"
})
public class SaleTransactionRsp {
    
    private List<SaleTransactionEntity> saleTransactionEntities;

    public SaleTransactionRsp() {
    }

    public SaleTransactionRsp(List<SaleTransactionEntity> saleTransactionEntities) {
        this.saleTransactionEntities = saleTransactionEntities;
    }
    
    public List<SaleTransactionEntity> getSaleTransactionEntities() {
        return saleTransactionEntities;
    }

    public void setSaleTransactionEntities(List<SaleTransactionEntity> saleTransactionEntities) {
        this.saleTransactionEntities = saleTransactionEntities;
    }
    
}
