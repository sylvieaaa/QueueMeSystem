/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.VendorEntityControllerLocal;
import entity.FoodCourtEntity;
import entity.VendorEntity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.FileUploadEvent;
import util.exception.VendorNotFoundException;

@Named(value = "updateVendorManagedBean")
@ViewScoped
public class UpdateVendorManagedBean implements Serializable {

    @EJB
    private VendorEntityControllerLocal vendorEntityControllerLocal;

    private VendorEntity vendorEntityToUpdate;
    private Long vendorIdToUpdate;

    private VendorEntity vendorToDisable;

    private List<VendorEntity> vendorEntities = new ArrayList<>();

    private File file;

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
        vendorEntities = vendorEntityControllerLocal.retrieveAllVendors();

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

    public void deleteVendor(ActionEvent event) {

        try {
            vendorEntityControllerLocal.deleteVendor(vendorToDisable.getBusinessId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Vendor deleted successfully", null));

        } catch (VendorNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting vendor: " + ex.getMessage(), null));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + e.getMessage(), null));
        }
    }

    public void upload(FileUploadEvent event) {
        FoodCourtEntity vendorEntity = (FoodCourtEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity");
        try {
            String fileName = "";
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + System.getProperty("file.separator") + "vendorLogos";

            System.err.println("********** Demo03ManagedBean.handleFileUpload(): File name: " + event.getFile().getFileName());
            System.err.println("********** Demo03ManagedBean.handleFileUpload(): newFilePath: " + newFilePath);

            file = new File(newFilePath);
            file = File.createTempFile("F0" + vendorEntity.getBusinessId(), ".png", file);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputstream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            vendorEntityToUpdate.setPhotoURL(file.getName());

            fileOutputStream.close();
            inputStream.close();
            System.err.println(file);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
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

    public VendorEntity getVendorToDisable() {
        return vendorToDisable;
    }

    public void setVendorToDisable(VendorEntity vendorToDisable) {
        this.vendorToDisable = vendorToDisable;
    }

    public List<VendorEntity> getVendorEntities() {
        return vendorEntities;
    }

    public void setVendorEntities(List<VendorEntity> vendorEntities) {
        this.vendorEntities = vendorEntities;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
