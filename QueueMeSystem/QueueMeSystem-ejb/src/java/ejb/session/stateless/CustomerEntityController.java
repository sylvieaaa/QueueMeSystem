/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CreateCustomerException;
import util.exception.CustomerNotFoundException;

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
    public void updateCustomer(CustomerEntity customerEntity) throws CustomerNotFoundException {

        CustomerEntity ce = retrieveCustomerByUsername(customerEntity.getUsername());

        ce.setAddress(customerEntity.getAddress());
        ce.setContactNumber(customerEntity.getContactNumber());
        ce.setFirstName(customerEntity.getFirstName());
        ce.setLastName(customerEntity.getLastName());
    }
}
