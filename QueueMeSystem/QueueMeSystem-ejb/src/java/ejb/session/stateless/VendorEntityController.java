/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AdminEntity;
import entity.FoodCourtEntity;
import entity.VendorEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AdminNotFoundException;
import util.exception.DuplicateEmailUserException;
import util.exception.FoodCourtNotFoundException;
import util.exception.VendorNotFoundException;

/**
 *
 * @author User
 */
@Stateless
public class VendorEntityController implements VendorEntityControllerLocal {

    @EJB
    private FoodCourtEntityControllerLocal foodCourtEntityControllerLocal;

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")
    private EntityManager em;

    @Override
    public VendorEntity createVendorEntity(VendorEntity vendorEntity, FoodCourtEntity foodCourtEntity) throws DuplicateEmailUserException {
        Query query = em.createQuery("SELECT e FROM VendorEntity e WHERE e.username=:inUsername");
        query.setParameter("inUsername", vendorEntity.getUsername());

        try {
            VendorEntity check = (VendorEntity) query.getSingleResult();

            throw new DuplicateEmailUserException("Email is not unique");
        } catch (NoResultException exc) {
            try {
                foodCourtEntity = foodCourtEntityControllerLocal.retrieveFoodCourtById(foodCourtEntity.getBusinessId());
                vendorEntity.setFoodCourtEntity(foodCourtEntity);
                em.persist(vendorEntity);
                foodCourtEntity.getVendorEntities().add(vendorEntity);
                em.flush();
                em.refresh(vendorEntity);
            } catch (FoodCourtNotFoundException ex) {
                Logger.getLogger(VendorEntityController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return vendorEntity;
    }

    @Override
    public VendorEntity retrieveVendorById(Long vendorStaffId) throws VendorNotFoundException {
        VendorEntity vendorEntity = em.find(VendorEntity.class, vendorStaffId);

        if (vendorEntity != null) {
            return vendorEntity;
        } else {
            throw new VendorNotFoundException("Vendor Staff ID: " + vendorStaffId + " does not exist");
        }
    }

    @Override
    public void updateVendor(VendorEntity vendorEntity) throws VendorNotFoundException {
        em.merge(vendorEntity);
    }

    @Override
    public void deleteVendor(Long vendorId) throws VendorNotFoundException {
        if (vendorId != null) {
            VendorEntity vendorToDisable = retrieveVendorById(vendorId);
            vendorToDisable.setEnabled(Boolean.FALSE);
        } else {
            throw new VendorNotFoundException("No vendor found.");
        }
    }

    @Override
    public List<VendorEntity> retrieveAllVendors() {
        Query query = em.createQuery("SELECT v FROM VendorEntity v WHERE v.enabled = true ORDER BY v.vendorName ASC");
        List<VendorEntity> vendorEntities = query.getResultList();

        return vendorEntities;
    }
    
    @Override
    public VendorEntity retrieveVendorByUsername(String username) throws VendorNotFoundException {
        Query query = em.createQuery("SELECT v FROM VendorEntity v WHERE v.username=:inUsername");
        query.setParameter("inUsername", username);

        try {
            return (VendorEntity) query.getSingleResult();
        } catch (NonUniqueResultException | NoResultException ex) {
            throw new VendorNotFoundException("Vendor " + username + " does not exists!");
        }
    }

    @Override
    public void updatePassword(String username, String password) {
        try {
            VendorEntity vendorToUpdate = retrieveVendorByUsername(username);
            vendorToUpdate.setPassword(password);
            em.merge(vendorToUpdate);
        } catch (VendorNotFoundException ex) {

        }
    }
    
    @Override
    public List<VendorEntity> retrieveVendorsByFoodCourt(FoodCourtEntity foodCourtEntity) {
        Query query = em.createQuery("SELECT v FROM VendorEntity v WHERE v.foodCourtEntity = :inFoodCourtEntity");
        query.setParameter("inFoodCourtEntity", foodCourtEntity);
        
        return query.getResultList();
    }

    /*
    @Override
    public VendorStaffEntity createVendorStaff(VendorStaffEntity vendorStaffEntity) {
        em.persist(vendorStaffEntity);
        em.flush();
        em.refresh(vendorStaffEntity);

        return vendorStaffEntity;
    }

    @Override
    public VendorStaffEntity retrieveVendorStaffById(Long vendorStaffId) throws VendorStaffNotFoundException {
        VendorStaffEntity vendorStaffEntity = em.find(VendorStaffEntity.class, vendorStaffId);

        if (vendorStaffEntity != null) {
            return vendorStaffEntity;
        } else {
            throw new VendorStaffNotFoundException("Vendor Staff ID: " + vendorStaffId + " does not exist");
        }
    }

    @Override
    public List<VendorStaffEntity> retrieveAllVendorStaff() throws VendorStaffNotFoundException {
        Query query = em.createQuery("SELECT * FROM VendorStaffEntity");
        List<VendorStaffEntity> vendorStaffEntities = query.getResultList();

        if (vendorStaffEntities.isEmpty()) {
            throw new VendorStaffNotFoundException("No vendor staff available.");
        } else {
            return vendorStaffEntities;
        }
    }

    @Override
    public void updateVendorStaff(VendorStaffEntity vendorStaffEntity) {
        em.merge(vendorStaffEntity);
    }

    @Override
    public void deleteVendorStaff(Long vendorStaffId) {
        try {
            VendorStaffEntity vendorStaffEntity = retrieveVendorStaffById(vendorStaffId);
            em.remove(vendorStaffEntity);
        } catch (VendorStaffNotFoundException ex) {

        }
    }

    @Override
    public VendorStaffEntity retrieveVendorStaffByUsername(String username) throws VendorStaffNotFoundException {
        Query query = em.createQuery("SELECT v FROM VendorStaffEntity v WHERE v.username=:inUsername");
        query.setParameter("inUsername", username);

        try {
            return (VendorStaffEntity) query.getSingleResult();
        } catch (NonUniqueResultException | NoResultException ex) {
            throw new VendorStaffNotFoundException("Vendor staff: " + username + " does not exists!");
        }
    }

    @Override
    public VendorStaffEntity vendorStaffLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            VendorStaffEntity vendorStaffEntity = retrieveVendorStaffByUsername(username);
            String passswordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + vendorStaffEntity.getSalt()));
            if (vendorStaffEntity.getPassword().equals(passswordHash)) {
                return vendorStaffEntity;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }

        } catch (VendorStaffNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
     */
}
