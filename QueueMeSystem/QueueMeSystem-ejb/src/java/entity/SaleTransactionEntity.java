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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.EntityInstanceMissingInCollectionException;

/**
 *
 * @author User
 */
@Entity
public class SaleTransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleTransactionId;
    private Integer totalLineItem;    
    private Integer totalQuantity;
    @Column(precision = 11, scale = 2)
    private BigDecimal totalAmount;
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar transactionDateTime;
    private Boolean isVoided;
    
    @ManyToOne
    @JoinColumn(nullable = true)
    private CustomerEntity customerEntity;
    @OneToMany(mappedBy = "saleTransactionEntity", cascade = CascadeType.PERSIST)
    private List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities;
    
    public SaleTransactionEntity() {
        this.saleTransactionLineItemEntities = new ArrayList<>();
    }

    public SaleTransactionEntity(Integer totalLineItem, Integer totalQuantity, BigDecimal totalAmount, Calendar transactionDateTime, Boolean isVoided) {//, List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities) {
        this();
        this.totalLineItem = totalLineItem;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.transactionDateTime = transactionDateTime;
        this.isVoided = isVoided;
       // this.saleTransactionLineItemEntities = saleTransactionLineItemEntities;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (saleTransactionId != null ? saleTransactionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the saleTransactionId fields are not set
        if (!(object instanceof SaleTransactionEntity)) {
            return false;
        }
        SaleTransactionEntity other = (SaleTransactionEntity) object;
        if ((this.saleTransactionId == null && other.saleTransactionId != null) || (this.saleTransactionId != null && !this.saleTransactionId.equals(other.saleTransactionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SaleTransactionEntity[ id=" + saleTransactionId + " ]";
    }
    
    public void addSaleTransactionLineItemEntity(SaleTransactionLineItemEntity saleTransactionLineItemEntity) throws EntityInstanceExistsInCollectionException
    {
        if(!this.saleTransactionLineItemEntities.contains(saleTransactionLineItemEntity))
        {
            this.saleTransactionLineItemEntities.add(saleTransactionLineItemEntity);
        }
        else
        {
            throw new EntityInstanceExistsInCollectionException("Sale Transaction Line Item already exist");
        }
    }
    
    public void removeSaleTransactionLineItemEntity(SaleTransactionLineItemEntity saleTransactionLineItemEntity) throws EntityInstanceMissingInCollectionException
    {
        if(this.saleTransactionLineItemEntities.contains(saleTransactionLineItemEntity))
        {
            this.saleTransactionLineItemEntities.remove(saleTransactionLineItemEntity);
        }
        else
        {
            throw new EntityInstanceMissingInCollectionException("Sale Transaction Line Item missing");
        }
    }

    public Long getSaleTransactionId() {
        return saleTransactionId;
    }

    public void setSaleTransactionId(Long saleTransactionId) {
        this.saleTransactionId = saleTransactionId;
    }

    public Integer getTotalLineItem() {
        return totalLineItem;
    }

    public void setTotalLineItem(Integer totalLineItem) {
        this.totalLineItem = totalLineItem;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Calendar getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(Calendar transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public Boolean getIsVoided() {
        return isVoided;
    }

    public void setIsVoided(Boolean isVoided) {
        this.isVoided = isVoided;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public List<SaleTransactionLineItemEntity> getSaleTransactionLineItemEntities() {
        return saleTransactionLineItemEntities;
    }

    public void setSaleTransactionLineItemEntities(List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities) {
        this.saleTransactionLineItemEntities = saleTransactionLineItemEntities;
    }

    
    
}
