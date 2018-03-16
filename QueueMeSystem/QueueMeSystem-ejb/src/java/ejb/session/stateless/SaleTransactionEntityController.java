/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.SaleTransactionEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author User
 */
@Stateless
public class SaleTransactionEntityController implements SaleTransactionEntityControllerLocal {

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")
    private EntityManager em;

    @Override
    public SaleTransactionEntity createSaleTransaction(SaleTransactionEntity saleTransactionEntity) {
        em.persist(saleTransactionEntity);
        em.flush();
        em.refresh(saleTransactionEntity);
        
        return saleTransactionEntity;
    }
    
    @Override
    public List<SaleTransactionEntity> retrieveSaleTransaction(Long customerId) {

        Query query = em.createQuery("SELECT c FROM CreditTransactionEntity c JOIN c.customer b WHERE b.customerId = :inCustomerId ");
        query.setParameter("inCustomerId", customerId);
        return query.getResultList();
    }
}
