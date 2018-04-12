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
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author User
 */
@Entity
public class OrderEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;
    private BigDecimal totalEarnings;
    private Boolean fulfilled;
    @ManyToOne
    @JoinColumn(nullable = true)
    private CustomerEntity customerEntity;  
    @ManyToOne
    @JoinColumn(nullable = false)
    private VendorEntity vendorEntity;    
    @OneToMany(mappedBy = "orderEntity", fetch=FetchType.EAGER)
    private List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities;

    public OrderEntity() {
        this.saleTransactionLineItemEntities = new ArrayList<>();
    }

    public OrderEntity(Date dateTime, BigDecimal totalEarnings, Boolean fulfilled) {
        this.dateTime = dateTime;
        this.totalEarnings = totalEarnings;
       // this.saleTransactionLineItemEntities = saleTransactionLineItemEntities;
        this.fulfilled = false;
    }

    public OrderEntity(Date dateTime, BigDecimal totalEarnings, Boolean fulfilled, CustomerEntity customerEntity, VendorEntity vendorEntity, List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities) {
        this.dateTime = dateTime;
        this.totalEarnings = totalEarnings;
        this.fulfilled = fulfilled;
        this.customerEntity = customerEntity;
        this.vendorEntity = vendorEntity;
        this.saleTransactionLineItemEntities = saleTransactionLineItemEntities;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderId != null ? orderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the orderId fields are not set
        if (!(object instanceof OrderEntity)) {
            return false;
        }
        OrderEntity other = (OrderEntity) object;
        if ((this.orderId == null && other.orderId != null) || (this.orderId != null && !this.orderId.equals(other.orderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.OrderEntity[ id=" + orderId + " ]";
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public BigDecimal getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(BigDecimal totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public Boolean getFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(Boolean fulfilled) {
        this.fulfilled = fulfilled;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public VendorEntity getVendorEntity() {
        return vendorEntity;
    }

    public void setVendorEntity(VendorEntity vendorEntity) {
        this.vendorEntity = vendorEntity;
    }
    
    public List<SaleTransactionLineItemEntity> getSaleTransactionLineItemEntities() {
        return saleTransactionLineItemEntities;
    }

    public void setSaleTransactionLineItemEntities(List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities) {
        this.saleTransactionLineItemEntities = saleTransactionLineItemEntities;
    }
    
    public String listItemName() {
        
        // loop thru lineItems in selectedOrderEntity
        // if size == 1, just add to string
        // else >1, add to string and add a comma
        String lineItemMenuName = "";
        for (SaleTransactionLineItemEntity xyz: saleTransactionLineItemEntities) {
            lineItemMenuName = xyz.getMenuItemEntity().getMenuItemName();
            lineItemMenuName = lineItemMenuName + ", " + "Quantity = " + xyz.getQuantity();
        }
        return lineItemMenuName;
    }

}
