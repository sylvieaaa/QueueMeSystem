/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.VendorStaffEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.VendorStaffNotFoundException;

/**
 *
 * @author User
 */
@Stateless
public class VendorStaffEntityController implements VendorStaffEntityControllerLocal {

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")
    private EntityManager em;

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
}
