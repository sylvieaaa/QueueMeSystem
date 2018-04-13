/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.AdminEntityControllerLocal;
import ejb.session.stateless.BusinessEntityControllerLocal;
import ejb.session.stateless.CustomerEntityControllerLocal;
import ejb.session.stateless.FoodCourtEntityControllerLocal;
import ejb.session.stateless.VendorEntityControllerLocal;
import entity.AdminEntity;
import entity.BusinessEntity;
import entity.FoodCourtEntity;
import entity.VendorEntity;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.BusinessEntityNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.FoodCourtNotFoundException;
import util.security.CryptographicHelper;

/**
 *
 * @author SYLVIA
 */
@Named(value = "updatePasswordManagedBean")
@ViewScoped
public class UpdatePasswordManagedBean implements Serializable {

    @EJB
    private AdminEntityControllerLocal adminEntityControllerLocal;

    @EJB
    private CustomerEntityControllerLocal customerEntityControllerLocal;

    @EJB
    private FoodCourtEntityControllerLocal foodCourtEntityControllerLocal;

    @EJB
    private VendorEntityControllerLocal vendorEntityControllerLocal;

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
    private String username;

    private BusinessEntity businessEntityToUpdate;

    public UpdatePasswordManagedBean() {
    }

    public void changePassword(ActionEvent event) throws IOException {

        BusinessEntity businessEntity = (BusinessEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity");
        username = businessEntity.getUsername();
        String passswordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(oldPassword + businessEntity.getSalt()));

        if (businessEntity.getPassword().equals(passswordHash)) {
            if (businessEntity instanceof AdminEntity) {
                adminEntityControllerLocal.updatePassword(username, newPassword);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password updated!", null));
                FacesContext.getCurrentInstance().getExternalContext().redirect("adminMainPage.xhtml");
            } else if (businessEntity instanceof FoodCourtEntity) {
                foodCourtEntityControllerLocal.updatePassword(username, newPassword);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password updated!", null));
                FacesContext.getCurrentInstance().getExternalContext().redirect("foodCourtMainPage.xhtml");
            } else if (businessEntity instanceof VendorEntity) {
                vendorEntityControllerLocal.updatePassword(username, newPassword);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password updated!", null));
                FacesContext.getCurrentInstance().getExternalContext().redirect("mainPage.xhtml");
            } else {
                customerEntityControllerLocal.updatePassword(username, newPassword);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password updated!", null));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect current password", null));
        }
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public BusinessEntity getBusinessEntityToUpdate() {
        return businessEntityToUpdate;
    }

    public void setBusinessEntityToUpdate(BusinessEntity businessEntityToUpdate) {
        this.businessEntityToUpdate = businessEntityToUpdate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
