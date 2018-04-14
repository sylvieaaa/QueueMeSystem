/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.BusinessEntityControllerLocal;
import ejb.session.stateless.EmailControllerLocal;
import entity.AdminEntity;
import entity.BusinessEntity;
import entity.CustomerEntity;
import entity.FoodCourtEntity;
import entity.VendorEntity;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import util.email.EmailManager;
import util.exception.BusinessEntityNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author User
 */
@Named(value = "indexManagedBean")
@RequestScoped
public class IndexManagedBean {

    @EJB
    private EmailControllerLocal emailControllerLocal;

    @EJB
    private BusinessEntityControllerLocal businessEntityControllerLocal;

    private String username;
    private String password;
    private String email;

    /**
     * Creates a new instance of IndexManagedBean
     */
    public IndexManagedBean() {
        System.out.println("created");
    }

    public void login(ActionEvent event) throws IOException {
        try {
            BusinessEntity businessEntity = businessEntityControllerLocal.login(username, password);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isLogin", true);

            String accountType;
            if (businessEntity instanceof AdminEntity) {
                accountType = "Admin";
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("businessEntity", (AdminEntity) businessEntity);
                FacesContext.getCurrentInstance().getExternalContext().redirect("adminMainPage.xhtml");
            } else if (businessEntity instanceof FoodCourtEntity) {
                if (((FoodCourtEntity) businessEntity).getEnabled()) {
                    accountType = "FoodCourt";
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("businessEntity", (FoodCourtEntity) businessEntity);
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid login credentials", null));
                    return;
                }
            } else if (businessEntity instanceof VendorEntity) {
                if (((VendorEntity) businessEntity).getEnabled()) {
                    accountType = "Vendor";
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("businessEntity", (VendorEntity) businessEntity);
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid login credentials", null));
                    return;
                }
            } else {
                accountType = "Customer";
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("businessEntity", (CustomerEntity) businessEntity);
            }

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("accountType", accountType);

            FacesContext.getCurrentInstance().getExternalContext().redirect("mainPage.xhtml");

        } catch (InvalidLoginCredentialException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid login credentials", null));
        }
    }

    public void forgetPassword(ActionEvent event) {
        try {
            emailControllerLocal.forgetPasswordEmail(email);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sent", null));
        } catch (BusinessEntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
