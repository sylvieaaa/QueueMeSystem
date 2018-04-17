/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import javax.ejb.Local;
import util.exception.CreateCustomerException;
import util.exception.CustomerNotFoundException;
import util.exception.PasswordDoNotMatchException;

/**
 *
 * @author User
 */
@Local
public interface CustomerEntityControllerLocal {

    public CustomerEntity createCustomer(CustomerEntity customerEntity) throws CreateCustomerException;

    public CustomerEntity retrieveCustomerByUsername(String username) throws CustomerNotFoundException;

    public void updateCustomer(CustomerEntity customerEntity) throws CustomerNotFoundException;

    public CustomerEntity retrieveCustomerById(Long customerId) throws CustomerNotFoundException;

    public void updatePassword(String username, String password);
    
    public void updateCustomerPassword(CustomerEntity customerEntity, String oldPassword, String newPassword) throws CustomerNotFoundException, PasswordDoNotMatchException;

    public void updateToken(CustomerEntity customerEntity);

}
