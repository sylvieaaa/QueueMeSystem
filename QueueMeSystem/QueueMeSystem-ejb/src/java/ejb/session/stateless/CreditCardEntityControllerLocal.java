/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCardEntity;
import entity.CustomerEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author User
 */
@Local
public interface CreditCardEntityControllerLocal {

    public CreditCardEntity createCreditCard(String cardNum, String cardName, CustomerEntity customerEntity);

    public void updateCreditCard(CreditCardEntity creditCardEntity);

    public List<CreditCardEntity> retrieveAllCreditCards(Long customerId);

    public void selectedCreditCard(CustomerEntity customerEntity,CreditCardEntity creditCardEntity);

    public void selectDefaultCard(Long creditCardId);

    public CreditCardEntity retrieveCreditCard(Long creditCardId);

    public void deleteCreditCard(CreditCardEntity creditCard);
    
}
