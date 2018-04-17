/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AdminEntity;
import entity.CustomerEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AdminNotFoundException;
import util.exception.CreateCustomerException;
import util.exception.CustomerNotFoundException;
import util.exception.PasswordDoNotMatchException;
import util.security.CryptographicHelper;

/**
 *
 * @author User
 */
@Stateless
public class CustomerEntityController implements CustomerEntityControllerLocal {

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")
    private EntityManager em;

    @Override
    public CustomerEntity createCustomer(CustomerEntity customerEntity) throws CreateCustomerException {
        em.persist(customerEntity);
        em.flush();
        em.refresh(customerEntity);

        return customerEntity;
    }

    @Override
    public CustomerEntity retrieveCustomerByUsername(String username) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM CustomerEntity c WHERE c.username = :inUsername");
        query.setParameter("inUsername", username);

        try {
            return (CustomerEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer Username " + username + " does not exist!");
        }
    }

    @Override
    public CustomerEntity retrieveCustomerById(Long customerId) throws CustomerNotFoundException {
        CustomerEntity customerEntity = em.find(CustomerEntity.class, customerId);

        if (customerEntity != null) {
            return customerEntity;
        } else {
            throw new CustomerNotFoundException("Customer ID: " + customerId + " does not exist");
        }
    }

    @Override
    public void updateCustomer(CustomerEntity customerEntity) throws CustomerNotFoundException {

        CustomerEntity ce = retrieveCustomerByUsername(customerEntity.getUsername());

        ce.setAddress(customerEntity.getAddress());
        ce.setContactNumber(customerEntity.getContactNumber());
        ce.setFirstName(customerEntity.getFirstName());
        ce.setLastName(customerEntity.getLastName());
    }

    @Override
    public void updatePassword(String username, String password) {
        try {
            CustomerEntity customerToUpdate = retrieveCustomerByUsername(username);
            customerToUpdate.setPassword(password);
            em.merge(customerToUpdate);
        } catch (CustomerNotFoundException ex) {

        }
    }

    @Override
    public void updateCustomerPassword(CustomerEntity customerEntity, String oldPassword, String newPassword) throws CustomerNotFoundException, PasswordDoNotMatchException {
        customerEntity = retrieveCustomerByUsername(customerEntity.getUsername());
        String oldPasswordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(oldPassword + customerEntity.getSalt()));
        if (customerEntity.getPassword().equals(oldPasswordHash)) {
            customerEntity.setPassword(newPassword);
        } else {
            throw new PasswordDoNotMatchException("Passwords do not match");
        }
    }

    @Override
    public void updateToken(CustomerEntity customerEntity) {
        String pushToken = customerEntity.getPushToken();
        try {
            customerEntity = retrieveCustomerByUsername(customerEntity.getUsername());
            customerEntity.setPushToken(pushToken);
        } catch (CustomerNotFoundException ex) {
        }

    }
}
