/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author User
 */
@Entity
public class VendorEntity extends BusinessEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private String vendorName;
    private String cuisineType;
    private BigDecimal rating;
    private String information;
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date startTime;
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date endTime;
    private BigDecimal creditReceived;

    @ManyToOne
    private FoodCourtEntity foodCourtEntity;
    @OneToOne(mappedBy = "vendorEntity")
    private List<MenuEntity> menuEntities;
    @OneToMany(mappedBy = "vendorEntity")
    private List<OrderEntity> orderEntities;
    @OneToMany(mappedBy = "vendorEntity")
    private List<ReviewEntity> reviewEntities;
    @OneToMany(mappedBy = "vendorEntity")
    private List<MenuItemEntity> menuItemEntities;

    public VendorEntity() {
        super();
        this.orderEntities = new ArrayList<>();
        this.reviewEntities = new ArrayList<>();
        menuItemEntities = new ArrayList<>();
        this.rating = BigDecimal.ZERO;
        this.creditReceived = BigDecimal.ZERO;

    }

    public VendorEntity(String vendorName, String cuisineType, BigDecimal rating, String information, Date startTime, Date endTime, BigDecimal creditReceived, String username, String password) {
        this();
        this.vendorName = vendorName;
        this.cuisineType = cuisineType;
        this.rating = rating;
        this.information = information;
        this.startTime = startTime;
        this.endTime = endTime;
        this.creditReceived = creditReceived;
        super.setUsername(username);
        super.setPassword(password);
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
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
        hash += (this.getBusinessId() != null ? this.getBusinessId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the vendorId fields are not set
        if (!(object instanceof VendorEntity)) {
            return false;
        }
        VendorEntity other = (VendorEntity) object;
        if ((this.getBusinessId() == null && other.getBusinessId() != null) || (this.getBusinessId() != null && !this.getBusinessId().equals(other.getBusinessId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.VendorEntity[ id=" + this.getBusinessId() + " ]";
    }

    public FoodCourtEntity getFoodCourtEntity() {
        return foodCourtEntity;
    }

    public void setFoodCourtEntity(FoodCourtEntity foodCourtEntity) {
        this.foodCourtEntity = foodCourtEntity;
    }

    public List<MenuEntity> getMenuEntities() {
        return menuEntities;
    }

    public void setMenuEntities(List<MenuEntity> menuEntities) {
        this.menuEntities = menuEntities;
    }

    public List<MenuItemEntity> getMenuItemEntities() {
        return menuItemEntities;
    }

    public void setMenuItemEntities(List<MenuItemEntity> menuItemEntities) {
        this.menuItemEntities = menuItemEntities;
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

}
