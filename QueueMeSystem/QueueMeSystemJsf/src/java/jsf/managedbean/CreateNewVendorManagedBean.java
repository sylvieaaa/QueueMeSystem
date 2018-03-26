package jsf.managedbean;

import ejb.session.stateless.VendorEntityControllerLocal;
import entity.VendorEntity;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@Named(value = "createNewVendorManagedBean")
@RequestScoped
public class CreateNewVendorManagedBean {

    @EJB
    private VendorEntityControllerLocal vendorEntityControllerLocal;

    private VendorEntity newVendorEntity;

    /**
     * Creates a new instance of CreateNewVendorManagedBean
     */
    public CreateNewVendorManagedBean() {
        newVendorEntity = new VendorEntity();
    }

    public void createNewProduct(ActionEvent event) {
        VendorEntity vendorEntity = vendorEntityControllerLocal.createVendorEntity(newVendorEntity);
        newVendorEntity = new VendorEntity();

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New vendor created successfully (Vendor ID: " + vendorEntity.getVendorName() + ")", null));
    }

    public VendorEntity getNewVendorEntity() {
        return newVendorEntity;
    }

    public void setNewVendorEntity(VendorEntity newVendorEntity) {
        this.newVendorEntity = newVendorEntity;
    }

}
