/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.VendorEntityControllerLocal;
import entity.VendorEntity;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;
import util.exception.VendorNotFoundException;

/**
 *
 * @author User
 */
@Named(value = "mainPageManagedBean")
@ViewScoped
public class MainPageManagedBean implements Serializable {

    @EJB(name = "VendorEntityControllerLocal")
    private VendorEntityControllerLocal vendorEntityControllerLocal;

    private VendorEntity currentVendor;

    /**
     * Creates a new instance of MainPageManagedBean
     */
    public MainPageManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        String accountType = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("accountType");
        System.err.println(accountType);
        Long vendorId;
        if (accountType.equals("Admin")) {
            vendorId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("vendorIdToView");
        } else {
            vendorId = ((VendorEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity")).getBusinessId();
        }
        try {
            currentVendor = vendorEntityControllerLocal.retrieveVendorById(vendorId);
        } catch (VendorNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }

    }

    public void backToFoodCourt(ActionEvent event) {
        try {
            Long foodCourtIdToView = (Long) event.getComponent().getAttributes().get("foodCourtId");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("foodCourtIdToUpdate", foodCourtIdToView);
            FacesContext.getCurrentInstance().getExternalContext().redirect("foodCourtMainPage.xhtml");
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
    }

    public void manageMenu(ActionEvent event) {
        try {
            Long vendorIdToManage = (Long) event.getComponent().getAttributes().get("vendorId");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("vendorId", vendorIdToManage);
            FacesContext.getCurrentInstance().getExternalContext().redirect("manageMenu.xhtml");
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
    }

    public VendorEntity getCurrentVendor() {
        return currentVendor;
    }

    public void setCurrentVendor(VendorEntity currentVendor) {
        this.currentVendor = currentVendor;
    }

}
