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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author User
 */
@Entity
public class FoodCourtEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodCourtId;
    private String name;
    private String description;
    private String address;
    private String postalCode;
    private BigDecimal ratings;
    @Temporal(javax.persistence.TemporalType.TIME)
    private Calendar startTime;
    @Temporal(javax.persistence.TemporalType.TIME)
    private Calendar endTime;
    private String username;
    private String password;
    
    @OneToMany(mappedBy = "foodCourtEntity")
    private List<VendorEntity> vendorEntities;

    public FoodCourtEntity() {
        this.vendorEntities = new ArrayList<>();
    }

    public FoodCourtEntity(String name, String description, String address, String postalCode, BigDecimal ratings, Calendar startTime, Calendar endTime, String username, String password) {
        this();
        this.name = name;
        this.description = description;
        this.address = address;
        this.postalCode = postalCode;
        this.ratings = ratings;
        this.startTime = startTime;
        this.endTime = endTime;
        this.username = username;
        this.password = password;
    }

    public Long getFoodCourtId() {
        return foodCourtId;
    }

    public void setFoodCourtId(Long foodCourtId) {
        this.foodCourtId = foodCourtId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (foodCourtId != null ? foodCourtId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the foodCourtId fields are not set
        if (!(object instanceof FoodCourtEntity)) {
            return false;
        }
        FoodCourtEntity other = (FoodCourtEntity) object;
        if ((this.foodCourtId == null && other.foodCourtId != null) || (this.foodCourtId != null && !this.foodCourtId.equals(other.foodCourtId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FoodCourtEntity[ id=" + foodCourtId + " ]";
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<VendorEntity> getVendorEntities() {
        return vendorEntities;
    }

    public void setVendorEntities(List<VendorEntity> vendorEntities) {
        this.vendorEntities = vendorEntities;
    }

}
