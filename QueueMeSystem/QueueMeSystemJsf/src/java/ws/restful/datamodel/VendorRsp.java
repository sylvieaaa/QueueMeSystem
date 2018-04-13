/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.datamodel;

import entity.VendorEntity;
import java.util.List;

/**
 *
 * @author SYLVIA
 */
public class VendorRsp {
    private List<VendorEntity> vendorEntities;

    public VendorRsp() {
    }

    public VendorRsp(List<VendorEntity> vendorEntities) {
        this.vendorEntities = vendorEntities;
    }

    public List<VendorEntity> getVendorEntities() {
        return vendorEntities;
    }

    public void setVendorEntities(List<VendorEntity> vendorEntities) {
        this.vendorEntities = vendorEntities;
    }
    
    
}
