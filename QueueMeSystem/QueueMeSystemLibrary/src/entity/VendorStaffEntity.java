/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import util.enumeration.EmployeeAccessRightEnum;

/**
 *
 * @author User
 */
@Entity
public class VendorStaffEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vendorStaffId;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private EmployeeAccessRightEnum accessRightEnum;
    @Column(unique = true)
    private String username;
    private String password;
    @ManyToOne
    private VendorEntity vendorEntity;

    public VendorStaffEntity() {
    }

    public VendorStaffEntity(String firstName, String lastName, EmployeeAccessRightEnum accessRightEnum, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.accessRightEnum = accessRightEnum;
        this.username = username;
        this.password = password;
    }

    public Long getVendorStaffId() {
        return vendorStaffId;
    }

    public void setVendorStaffId(Long vendorStaffId) {
        this.vendorStaffId = vendorStaffId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (vendorStaffId != null ? vendorStaffId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the vendorStaffId fields are not set
        if (!(object instanceof VendorStaffEntity)) {
            return false;
        }
        VendorStaffEntity other = (VendorStaffEntity) object;
        if ((this.vendorStaffId == null && other.vendorStaffId != null) || (this.vendorStaffId != null && !this.vendorStaffId.equals(other.vendorStaffId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.vendorStaffEntity[ id=" + vendorStaffId + " ]";
    }

    public EmployeeAccessRightEnum getAccessRightEnum() {
        return accessRightEnum;
    }

    public void setAccessRightEnum(EmployeeAccessRightEnum accessRightEnum) {
        this.accessRightEnum = accessRightEnum;
    }

    public VendorEntity getVendorEntity() {
        return vendorEntity;
    }

    public void setVendorEntity(VendorEntity vendorEntity) {
        this.vendorEntity = vendorEntity;
    }
}
