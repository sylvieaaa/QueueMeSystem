/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author User
 */
@Entity
public class VendorEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vendorId;
    private String vendorName;
    private String cuisineType;
    private BigDecimal rating;
    private String information;
    @Temporal(javax.persistence.TemporalType.TIME)
    private Calendar startTime;
    @Temporal(javax.persistence.TemporalType.TIME)
    private Calendar endTime;
    private BigDecimal creditReceived;
    
    @ManyToOne
    private FoodCourtEntity foodCourtEntity;
    @OneToOne(mappedBy = "vendorEntity")
    private MenuEntity menuEntity;
    @OneToMany(mappedBy = "vendorEntity")
    private List<OrderEntity> orderEntities;
    @OneToMany(mappedBy = "vendorEntity")
    private List<ReviewEntity> reviewEntities;
    
    @OneToMany(mappedBy = "vendorEntity")
    private List<VendorStaffEntity> vendorStaffEntities;

    public VendorEntity() {
        this.orderEntities = new ArrayList<>();
        this.reviewEntities = new ArrayList<>();
        this.vendorStaffEntities = new ArrayList<>();
    }

    public VendorEntity(String vendorName, String cuisineType, BigDecimal rating, String information, Calendar startTime, Calendar endTime, BigDecimal creditReceived) {
        this();
        this.vendorName = vendorName;
        this.cuisineType = cuisineType;
        this.rating = rating;
        this.information = information;
        this.startTime = startTime;
        this.endTime = endTime;
        this.creditReceived = creditReceived;
    }

   
    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getCreditReceived() {
        return creditReceived;
    }

    public void setCreditReceived(BigDecimal creditReceived) {
        this.creditReceived = creditReceived;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (vendorId != null ? vendorId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the vendorId fields are not set
        if (!(object instanceof VendorEntity)) {
            return false;
        }
        VendorEntity other = (VendorEntity) object;
        if ((this.vendorId == null && other.vendorId != null) || (this.vendorId != null && !this.vendorId.equals(other.vendorId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.VendorEntity[ id=" + vendorId + " ]";
    }

    public FoodCourtEntity getFoodCourtEntity() {
        return foodCourtEntity;
    }

    public void setFoodCourtEntity(FoodCourtEntity foodCourtEntity) {
        this.foodCourtEntity = foodCourtEntity;
    }

    public MenuEntity getMenuEntity() {
        return menuEntity;
    }

    public void setMenuEntity(MenuEntity menuEntity) {
        this.menuEntity = menuEntity;
    }

    public List<OrderEntity> getOrderEntities() {
        return orderEntities;
    }

    public void setOrderEntities(List<OrderEntity> orderEntities) {
        this.orderEntities = orderEntities;
    }

    public List<ReviewEntity> getReviewEntities() {
        return reviewEntities;
    }

    public void setReviewEntities(List<ReviewEntity> reviewEntities) {
        this.reviewEntities = reviewEntities;
    }

    public List<VendorStaffEntity> getVendorStaffEntities() {
        return vendorStaffEntities;
    }

    public void setVendorStaffEntities(List<VendorStaffEntity> vendorStaffEntities) {
        this.vendorStaffEntities = vendorStaffEntities;
    }
    
}
