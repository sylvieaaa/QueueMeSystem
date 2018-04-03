package jsf.managedbean;

import ejb.session.stateless.VendorEntityControllerLocal;
import entity.FoodCourtEntity;
import entity.VendorEntity;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import util.enumeration.VendorTypeEnum;

@Named(value = "createNewVendorManagedBean")
@RequestScoped
public class CreateNewVendorManagedBean implements Serializable{

    @EJB
    private VendorEntityControllerLocal vendorEntityControllerLocal;

    private VendorEntity newVendorEntity;

    private VendorTypeEnum[] vendorTypes = VendorTypeEnum.values();

    private UploadedFile file;

    /**
     * Creates a new instance of CreateNewVendorManagedBean
     */
    public CreateNewVendorManagedBean() {
        newVendorEntity = new VendorEntity();
    }

    public void createNewVendor(ActionEvent event) {
        FoodCourtEntity foodCourtEntity = (FoodCourtEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity");
        VendorEntity vendorEntity = vendorEntityControllerLocal.createVendorEntity(newVendorEntity, foodCourtEntity);
        newVendorEntity = new VendorEntity();

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New vendor created successfully (Vendor ID: " + vendorEntity.getVendorName() + ")", null));
    }

    public void upload(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public VendorEntity getNewVendorEntity() {
        return newVendorEntity;
    }

    public void setNewVendorEntity(VendorEntity newVendorEntity) {
        this.newVendorEntity = newVendorEntity;
    }

    public VendorTypeEnum[] getVendorTypes() {
        return vendorTypes;
    }

    public void setVendorTypes(VendorTypeEnum[] vendorTypes) {
        this.vendorTypes = vendorTypes;
    }

}
