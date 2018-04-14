/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FoodCourtEntity;
import entity.VendorEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.DuplicateEmailUserException;
import util.exception.VendorNotFoundException;

/**
 *
 * @author User
 */
@Local
public interface VendorEntityControllerLocal {

    public VendorEntity retrieveVendorById(Long vendorStaffId) throws VendorNotFoundException;

    public VendorEntity createVendorEntity(VendorEntity vendorEntity, FoodCourtEntity foodCourtEntity) throws DuplicateEmailUserException;

    public void updateVendor(VendorEntity vendorEntity) throws VendorNotFoundException;

    public void deleteVendor(Long vendorId) throws VendorNotFoundException;

    public List<VendorEntity> retrieveAllVendors();

    public VendorEntity retrieveVendorByUsername(String username) throws VendorNotFoundException;

    public void updatePassword(String username, String password);

    public List<VendorEntity> retrieveVendorsByFoodCourtId(Long foodCourtId);

}
