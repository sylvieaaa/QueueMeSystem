/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless; 
import entity.CustomerEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CreateNewSaleTransactionException;

/**
 *
 * @author User
 */
@Stateless
public class SaleTransactionEntityController implements SaleTransactionEntityControllerLocal {

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")
    
    private EntityManager em;
    
    @Resource
    private EJBContext eJBContext;
    

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
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
    
    @Override
    public List<SaleTransactionEntity> retrieveAllSaleTransactions()
    {
        Query query = em.createQuery("SELECT st FROM SaleTransactionEntity st");
        
        List<SaleTransactionEntity> saleTransactionEntities = query.getResultList();
        
        for(SaleTransactionEntity saleTransactionEntity:saleTransactionEntities)
        {
            saleTransactionEntity.getSaleTransactionLineItemEntities().size();
        }
        
        return saleTransactionEntities;
    }
  
}
