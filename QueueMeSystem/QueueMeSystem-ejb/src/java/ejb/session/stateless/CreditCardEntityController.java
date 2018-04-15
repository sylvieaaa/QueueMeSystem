/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCardEntity;
import entity.CustomerEntity;
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
    public CreditCardEntity createCreditCard(String cardNum, String cardName, CustomerEntity customerEntity) {
        CreditCardEntity creditCard = new CreditCardEntity();
        creditCard.setCardNo(cardNum);
        creditCard.setName(cardName);
        creditCard.setDefaultCard(false);
        em.persist(creditCard);
        em.flush();
        em.refresh(creditCard);
        creditCard.setCustomerEntity(customerEntity);
        customerEntity.getCreditCardEntities().add(creditCard);
        
        return creditCard;
    }
    
    @Override
    public void updateCreditCard(CreditCardEntity creditCardEntity) {
        em.merge(creditCardEntity);      
    }
    
    @Override
    public List<CreditCardEntity> retrieveAllCreditCards(Long customerId) {
 
        Query query = em.createQuery("SELECT c FROM CreditCardEntity c WHERE c.customerEntity.businessId = :inCustomerId");
        query.setParameter("inCustomerId", customerId);
        return query.getResultList();
    }
    
    @Override
    public void selectedCreditCard(CustomerEntity customerEntity, CreditCardEntity creditCardEntity) {
        Long customerId = customerEntity.getBusinessId();
        System.err.println("1");
        List<CreditCardEntity> ces = retrieveAllCreditCards(customerId);
        for (CreditCardEntity ce : ces) {
            ce.setDefaultCard(false);
        }
        System.err.println("2");
         creditCardEntity.setDefaultCard(true);  
    }
    
    @Override
    public void selectDefaultCard(Long creditCardId) {
        CreditCardEntity creditCardEntity = em.find(CreditCardEntity.class, creditCardId);
        for(CreditCardEntity cce: creditCardEntity.getCustomerEntity().getCreditCardEntities()) {
            cce.setDefaultCard(false);
        }
        creditCardEntity.setDefaultCard(true);
    }
}
