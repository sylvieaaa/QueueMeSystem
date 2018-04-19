/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.datamodel;

import entity.CustomerEntity;
import entity.ReviewEntity;
import entity.VendorEntity;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author SYLVIA
 */
@XmlType(name = "reviewReq", propOrder = {
    "vendorEntity",
    "reviewEntity",
    "customerEntity"
})
public class ReviewReq {
    private VendorEntity vendorEntity;
    private ReviewEntity reviewEntity;
    private CustomerEntity customerEntity;

    public ReviewReq(VendorEntity vendorEntity, ReviewEntity reviewEntity, CustomerEntity customerEntity) {
        this.vendorEntity = vendorEntity;
        this.reviewEntity = reviewEntity;
        this.customerEntity = customerEntity;
    }

    public ReviewReq() {
    }

    public VendorEntity getVendorEntity() {
        return vendorEntity;
    }

    public void setVendorEntity(VendorEntity vendorEntity) {
        this.vendorEntity = vendorEntity;
    }

    public ReviewEntity getReviewEntity() {
        return reviewEntity;
    }

    public void setReviewEntity(ReviewEntity reviewEntity) {
        this.reviewEntity = reviewEntity;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

  
    
}
