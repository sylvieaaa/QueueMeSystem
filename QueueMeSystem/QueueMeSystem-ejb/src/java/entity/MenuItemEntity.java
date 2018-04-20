/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author User
 */
@Entity
public class MenuItemEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuItemId;
    private String menuItemName;
    private String description;
    @Column(precision = 11, scale = 2)
    private BigDecimal price;
    private String photoURL;
    private Boolean enabled;

    @ManyToMany
    private List<TagEntity> tagEntities;
    
    @ManyToMany(mappedBy = "menuItemEntities")
    private List<CategoryEntity> categoryEntities;
    
    @OneToMany(mappedBy = "menuItemEntity")
    private List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities;
    @ManyToOne
    private VendorEntity vendorEntity;

    public MenuItemEntity() {
        this.saleTransactionLineItemEntities = new ArrayList<>();
        this.categoryEntities = new ArrayList<>();
        this.tagEntities = new ArrayList<>();
        enabled = Boolean.TRUE;
    }

    public MenuItemEntity(String menuItemName, String description, BigDecimal price, String photo) {
        this();
        this.menuItemName = menuItemName;
        this.description = description;
        this.price = price;
        this.photoURL = photo;
    }
    
    public MenuItemEntity(String menuItemName, String description, BigDecimal price, String photo, List<TagEntity> tagEntities) {
        this();
        this.menuItemName = menuItemName;
        this.description = description;
        this.price = price;
        this.photoURL = photo;
        this.tagEntities = tagEntities;
    }
    
    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (menuItemId != null ? menuItemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the menuItemId fields are not set
        if (!(object instanceof MenuItemEntity)) {
            return false;
        }
        MenuItemEntity other = (MenuItemEntity) object;
        if ((this.menuItemId == null && other.menuItemId != null) || (this.menuItemId != null && !this.menuItemId.equals(other.menuItemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.MenuItemEntity[ id=" + menuItemId + " ]";
    }

    public List<CategoryEntity> getCategoryEntities() {
        return categoryEntities;
    }

    public void setCategoryEntities(List<CategoryEntity> categoryEntities) {
        this.categoryEntities = categoryEntities;
    }

    public List<SaleTransactionLineItemEntity> getSaleTransactionLineItemEntities() {
        return saleTransactionLineItemEntities;
    }

    public void setSaleTransactionLineItemEntities(List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities) {
        this.saleTransactionLineItemEntities = saleTransactionLineItemEntities;
    }

    public VendorEntity getVendorEntity() {
        return vendorEntity;
    }

    public void setVendorEntity(VendorEntity vendorEntity) {
        this.vendorEntity = vendorEntity;
    }

    public List<TagEntity> getTagEntities() {
        return tagEntities;
    }

    public void setTagEntities(List<TagEntity> tagEntities) {
        this.tagEntities = tagEntities;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
}
