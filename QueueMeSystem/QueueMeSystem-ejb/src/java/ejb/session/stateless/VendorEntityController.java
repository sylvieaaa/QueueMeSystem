/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
import entity.MenuEntity;
import entity.VendorEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.VendorNotFoundException;

/**
 *
 * @author User
 */
@Stateless
public class VendorEntityController implements VendorEntityControllerLocal {

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")
    private EntityManager em;

    @Override
    public VendorEntity createVendorEntity(VendorEntity vendorEntity) {
//        MenuEntity menuEntity = new MenuEntity();
//        vendorEntity.setMenuEntity(menuEntity);
//        menuEntity.setVendorEntity(vendorEntity);
//        
//        CategoryEntity categoryEntity = new CategoryEntity("Main");
//        menuEntity.getCategoryEntities().add(categoryEntity);
//        categoryEntity.setMenuEntity(menuEntity);
//        
        em.persist(vendorEntity);
        em.flush();
        em.refresh(vendorEntity);
        
        return vendorEntity;
    }
    
    @Override
    public VendorEntity retrieveVendorStaffById(Long vendorStaffId) throws VendorNotFoundException {
        VendorEntity vendorEntity = em.find(VendorEntity.class, vendorStaffId);

        if (vendorEntity != null) {
            return vendorEntity;
        } else {
            throw new VendorNotFoundException("Vendor Staff ID: " + vendorStaffId + " does not exist");
        }
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
