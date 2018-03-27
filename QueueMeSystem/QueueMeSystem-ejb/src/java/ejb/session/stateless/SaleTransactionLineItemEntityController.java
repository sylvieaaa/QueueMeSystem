/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.SaleTransactionLineItemEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Stateless
public class SaleTransactionLineItemEntityController implements SaleTransactionLineItemEntityControllerLocal {

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")
    private EntityManager em;

    @Override
    public SaleTransactionLineItemEntity createSaleTransactionLineItemEntity (SaleTransactionLineItemEntity saleTransactionLineItemEntity) {
        em.persist(saleTransactionLineItemEntity);
        em.flush();
        em.refresh(saleTransactionLineItemEntity);
        
        return saleTransactionLineItemEntity;
    }
    
    private String getItemName(Long saleId) {
        Query query = em.createQuery("SELECT m FROM MenuItemEntity m WHERE m.saleTransactionLineItemEntities.saleTransactionLineItemId = :inSaleId");
        query.setParameter("inSaleId", saleId);

        return (String) query.getSingleResult();
    }
}
