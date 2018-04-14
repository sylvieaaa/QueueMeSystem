/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCardEntity;
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
public class CreditCardEntityController implements CreditCardEntityControllerLocal {

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")
    private EntityManager em;

    @Override
    public CreditCardEntity createCreditCard(CreditCardEntity creditCardEntity) {
        em.persist(creditCardEntity);
        em.flush();
        em.refresh(creditCardEntity);
        
        return creditCardEntity;
    }
    
    @Override
    public void updateCreditCard(CreditCardEntity creditCardEntity) {
        em.merge(creditCardEntity);
       
    }
    
    @Override
    public List<CreditCardEntity> retrieveAllCreditCards(Long customerId) {
        Query query = em.createQuery("SELECT c FROM CreditCardEntity c WHERE c.customerEntity.businessId=:inCustomerId");
        query.setParameter("inCustomerId", customerId);
        
        return query.getResultList();
    }
}
