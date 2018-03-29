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
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author User
 */
@Entity
public class FoodCourtEntity extends BusinessEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    private String address;
    private String postalCode;
    private BigDecimal ratings;
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date startTime;
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date endTime;
    
    @OneToMany(mappedBy = "foodCourtEntity")
    private List<VendorEntity> vendorEntities;

    public FoodCourtEntity() {
        super();
        this.vendorEntities = new ArrayList<>();
    }

    public FoodCourtEntity(String name, String description, String address, String postalCode, BigDecimal ratings, Date startTime, Date endTime, String username, String password) {
        this();
        this.name = name;
        this.description = description;
        this.address = address;
        this.postalCode = postalCode;
        this.ratings = ratings;
        this.startTime = startTime;
        this.endTime = endTime;
        super.setUsername(username);
        super.setPassword(password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getRatings() {
        return ratings;
    }

    public void setRatings(BigDecimal ratings) {
        this.ratings = ratings;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getBusinessId() != null ? this.getBusinessId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the foodCourtId fields are not set
        if (!(object instanceof FoodCourtEntity)) {
            return false;
        }
        FoodCourtEntity other = (FoodCourtEntity) object;
        if ((this.getBusinessId() == null && other.getBusinessId() != null) || (this.getBusinessId() != null && !this.getBusinessId().equals(other.getBusinessId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FoodCourtEntity[ id=" + getBusinessId() + " ]";
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public List<VendorEntity> getVendorEntities() {
        return vendorEntities;
    }

    public void setVendorEntities(List<VendorEntity> vendorEntities) {
        this.vendorEntities = vendorEntities;
    }

}
