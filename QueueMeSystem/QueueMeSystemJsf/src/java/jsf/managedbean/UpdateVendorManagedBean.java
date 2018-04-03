/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.VendorEntityControllerLocal;
import entity.VendorEntity;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.VendorNotFoundException;

@Named(value = "updateVendorManagedBean")
@ViewScoped
public class UpdateVendorManagedBean implements Serializable {

    @EJB
    private VendorEntityControllerLocal vendorEntityControllerLocal;

    private VendorEntity vendorEntityToUpdate;
    private Long vendorIdToUpdate;

    /**
     * Creates a new instance of UpdateVendorManagedBean
     */
    public UpdateVendorManagedBean() {
    }

    public void updateVendor(ActionEvent event) {
        try {
            vendorEntityControllerLocal.updateVendor(vendorEntityToUpdate);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Vendor updated successfully", null));
        } catch (VendorNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating vendor: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    @PostConstruct
    public void postConstruct() {
        vendorIdToUpdate = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("vendorIdToUpdate");

        try {
            vendorEntityToUpdate = vendorEntityControllerLocal.retrieveVendorById(vendorIdToUpdate);
        } catch (VendorNotFoundException ex) {
            vendorEntityToUpdate = new VendorEntity();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the vendor details: " + ex.getMessage(), null));
        } catch (Exception ex) {
            vendorEntityToUpdate = new VendorEntity();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public VendorEntity getVendorEntityToUpdate() {
        return vendorEntityToUpdate;
    }

    public void setVendorEntityToUpdate(VendorEntity vendorEntityToUpdate) {
        this.vendorEntityToUpdate = vendorEntityToUpdate;
    }

    public Long getVendorIdToUpdate() {
        return vendorIdToUpdate;
    }

    public void setVendorIdToUpdate(Long vendorIdToUpdate) {
        this.vendorIdToUpdate = vendorIdToUpdate;
    }

}
