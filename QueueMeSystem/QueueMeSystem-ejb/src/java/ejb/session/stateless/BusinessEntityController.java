/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BusinessEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.common.RandomGenerator;
import util.exception.BusinessEntityNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.security.CryptographicHelper;

/**
 *
 * @author User
 */
@Stateless
public class BusinessEntityController implements BusinessEntityControllerLocal {

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")
    private EntityManager em;

    @Override
    public BusinessEntity retrieveBusinessEntityByUsername(String username) throws BusinessEntityNotFoundException {
        Query query = em.createQuery("SELECT b FROM BusinessEntity b WHERE b.username=:inUsername");
        query.setParameter("inUsername", username);

        try {
            return (BusinessEntity) query.getSingleResult();
        } catch (NonUniqueResultException | NoResultException ex) {
            throw new BusinessEntityNotFoundException("User " + username + " does not exist.");
        }
    }

    @Override
    public BusinessEntity login(String username, String password) throws InvalidLoginCredentialException {
        try {
            BusinessEntity businessEntity = retrieveBusinessEntityByUsername(username);
            String passswordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + businessEntity.getSalt()));
            if (businessEntity.getPassword().equals(passswordHash)) {
                return businessEntity;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        } catch (BusinessEntityNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }

    }

    @Override
    public String generateRandomPassword(String username) throws BusinessEntityNotFoundException {
        BusinessEntity businessEntity = retrieveBusinessEntityByUsername(username);
        String newPassword = new RandomGenerator().generatePassword();
        businessEntity.setPassword(newPassword);

        return newPassword;
    }

}
