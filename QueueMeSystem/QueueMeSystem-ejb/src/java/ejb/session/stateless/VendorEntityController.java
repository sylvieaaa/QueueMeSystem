/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.VendorEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
        em.persist(vendorEntity);
        em.flush();
        em.refresh(vendorEntity);
        
        return vendorEntity;
    }
}
