/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.VendorStaffEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.VendorStaffNotFoundException;

/**
 *
 * @author User
 */
@Local
public interface VendorStaffEntityControllerLocal {

    public void deleteVendorStaff(Long vendorStaffId);

    public void updateVendorStaff(VendorStaffEntity vendorStaffEntity);

    public List<VendorStaffEntity> retrieveAllVendorStaff() throws VendorStaffNotFoundException;

    public VendorStaffEntity retrieveVendorStaffById(Long vendorStaffId) throws VendorStaffNotFoundException;

    public VendorStaffEntity createVendorStaff(VendorStaffEntity vendorStaffEntity);
    
}
