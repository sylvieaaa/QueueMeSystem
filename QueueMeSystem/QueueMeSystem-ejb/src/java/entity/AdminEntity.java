/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;

/**
 *
 * @author User
 */
@Entity
public class AdminEntity extends BusinessEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private String firstName;
    private String lastName;

    public AdminEntity() {
        super();
    }

    public AdminEntity(String firstName, String lastName, String username, String password) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        super.setUsername(username);
        super.setPassword(password);
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getBusinessId() != null ? this.getBusinessId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the adminId fields are not set
        if (!(object instanceof AdminEntity)) {
            return false;
        }
        AdminEntity other = (AdminEntity) object;
        if ((this.getBusinessId() == null && other.getBusinessId() != null) || (this.getBusinessId() != null && !this.getBusinessId().equals(other.getBusinessId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AdminEntity[ id=" + getBusinessId() + " ]";
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

}
