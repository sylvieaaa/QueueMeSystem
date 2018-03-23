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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author User
 */
@Entity
public class CustomerEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String address;
    private BigDecimal creditBalance;
    private String emailAddress;
    private String password;
    
    @OneToMany(mappedBy = "customerEntity")
    private List<CreditCardEntity> creditCardEntities;

    @OneToMany(mappedBy = "customerEntity")
    private List<SaleTransactionEntity> saleTransactionEntities;
    
    @OneToMany(mappedBy = "customerEntity")
    private List<OrderEntity> orderEntities;
   
    @OneToMany(mappedBy = "customerEntity")
    private List<ReviewEntity> reviewEntities;
    
    public CustomerEntity() {
        this.creditCardEntities = new ArrayList<>();
        this.saleTransactionEntities = new ArrayList<>();
        this.orderEntities = new ArrayList<>();
        this.reviewEntities = new ArrayList<>();
    }

    public CustomerEntity(Long customerId, String firstName, String lastName, String contactNumber, String address, BigDecimal creditBalance, String emailAddress, String password) {
        this();
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.address = address;
        this.creditBalance = creditBalance;
        this.emailAddress = emailAddress;
        this.password = password;
    }

    public CustomerEntity(String firstName, String lastName, String contactNumber, String address, BigDecimal creditBalance, String emailAddress, String password) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.address = address;
        this.creditBalance = creditBalance;
        this.emailAddress = emailAddress;
        this.password = password;
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

    public BigDecimal getCreditBalance() {
        return creditBalance;
    }

    public void setCreditBalance(BigDecimal creditBalance) {
        this.creditBalance = creditBalance;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customerId fields are not set
        if (!(object instanceof CustomerEntity)) {
            return false;
        }
        CustomerEntity other = (CustomerEntity) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CustomerEntity[ id=" + customerId + " ]";
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
