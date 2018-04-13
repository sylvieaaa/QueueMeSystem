/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AdminEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AdminNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.security.CryptographicHelper;

/**
 *
 * @author User
 */
@Stateless
public class AdminEntityController implements AdminEntityControllerLocal {

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")
    private EntityManager em;

    @Override
    public AdminEntity createAdmin(AdminEntity adminEntity) {
        em.persist(adminEntity);
        em.flush();
        em.refresh(adminEntity);

        return adminEntity;
    }

    @Override
    public AdminEntity retrieveAdminByUsername(String username) throws AdminNotFoundException {
        Query query = em.createQuery("SELECT a FROM AdminEntity a WHERE a.username=:inUsername");
        query.setParameter("inUsername", username);

        try {
            return (AdminEntity) query.getSingleResult();
        } catch (NonUniqueResultException | NoResultException ex) {
            throw new AdminNotFoundException("Admin " + username + " does not exists!");
        }
    }

    @Override
    public AdminEntity retrieveAdminById(Long adminId) throws AdminNotFoundException {
        AdminEntity adminEntity = em.find(AdminEntity.class, adminId);

        if (adminEntity != null) {
            return adminEntity;
        } else {
            throw new AdminNotFoundException("Admin ID: " + adminId + " does not exist");
        }
    }

    @Override
    public AdminEntity adminLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            AdminEntity adminEntity = retrieveAdminByUsername(username);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + adminEntity.getSalt()));

            if (passwordHash.equals(adminEntity.getPassword())) {
                return adminEntity;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        } catch (AdminNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }

    }

    @Override
    public void updatePassword(String username, String password) {
        try {
            AdminEntity adminToUpdate = retrieveAdminByUsername(username);
            adminToUpdate.setPassword(password);
            em.merge(adminToUpdate);
        } catch (AdminNotFoundException ex) {

        }
    }

}
