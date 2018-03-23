/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author User
 */
@Entity
public class CustomerEntity extends BusinessEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;
    private String contactNumber;
    private String address;

    @OneToMany(mappedBy = "customerEntity")
    private List<CreditCardEntity> creditCardEntities;

    @OneToMany(mappedBy = "customerEntity")
    private List<SaleTransactionEntity> saleTransactionEntities;
    
    @OneToMany(mappedBy = "customerEntity")
    private List<OrderEntity> orderEntities;
   
    @OneToMany(mappedBy = "customerEntity")
    private List<ReviewEntity> reviewEntities;
    
    public CustomerEntity() {
        super();
        this.creditCardEntities = new ArrayList<>();
        this.saleTransactionEntities = new ArrayList<>();
        this.orderEntities = new ArrayList<>();
        this.reviewEntities = new ArrayList<>();
    }

    public CustomerEntity(String firstName, String lastName, String contactNumber, String address, String emailAddress, String password) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.address = address;
        super.setUsername(emailAddress);
        super.setPassword(password);
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getBusinessId() != null ? this.getBusinessId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customerId fields are not set
        if (!(object instanceof CustomerEntity)) {
            return false;
        }
        CustomerEntity other = (CustomerEntity) object;
        if ((this.getBusinessId() == null && other.getBusinessId() != null) || (this.getBusinessId() != null && !this.getBusinessId().equals(other.getBusinessId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CustomerEntity[ id=" + this.getBusinessId() + " ]";
    }

    public List<CreditCardEntity> getCreditCardEntities() {
        return creditCardEntities;
    }

    public void setCreditCardEntities(List<CreditCardEntity> creditCardEntities) {
        this.creditCardEntities = creditCardEntities;
    }

    public List<SaleTransactionEntity> getSaleTransactionEntities() {
        return saleTransactionEntities;
    }

    public void setSaleTransactionEntities(List<SaleTransactionEntity> saleTransactionEntities) {
        this.saleTransactionEntities = saleTransactionEntities;
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
