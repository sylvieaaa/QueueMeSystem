/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FoodCourtEntity;
import entity.VendorEntity;
import javax.ejb.Local;
import util.exception.VendorNotFoundException;

/**
 *
 * @author User
 */
@Local
public interface VendorEntityControllerLocal {

    public VendorEntity retrieveVendorById(Long vendorStaffId) throws VendorNotFoundException;

    public VendorEntity createVendorEntity(VendorEntity vendorEntity, FoodCourtEntity foodCourtEntity);

    public void updateVendor(VendorEntity vendorEntity) throws VendorNotFoundException;

    public void deleteVendor(Long vendorId) throws VendorNotFoundException;

}
