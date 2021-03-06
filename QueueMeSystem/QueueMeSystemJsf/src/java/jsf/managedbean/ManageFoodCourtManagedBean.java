/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.FoodCourtEntityControllerLocal;
import ejb.session.stateless.VendorEntityControllerLocal;
import entity.AdminEntity;
import entity.BusinessEntity;
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
import org.primefaces.event.FlowEvent;
import util.enumeration.VendorTypeEnum;
import util.exception.DuplicateEmailUserException;
import util.exception.FoodCourtNotFoundException;
import util.exception.VendorNotFoundException;

@Named(value = "manageFoodCourtManagedBean")
@ViewScoped
public class ManageFoodCourtManagedBean implements Serializable {

    @EJB
    private FoodCourtEntityControllerLocal foodCourtEntityControllerLocal;

    @EJB
    private VendorEntityControllerLocal vendorEntityControllerLocal;

    private VendorEntity vendorEntityToUpdate;

    private VendorEntity newVendorEntity;

    private Long vendorIdToUpdate;

    private VendorEntity vendorToDisable;

    private FoodCourtEntity currentFoodCourt;

    private List<VendorEntity> vendorEntities = new ArrayList<>();

    private File file;

    private VendorTypeEnum[] vendorTypes = VendorTypeEnum.values();

    private String uploadFrom;

    /**
     * Creates a new instance of UpdateVendorManagedBean
     */
    public ManageFoodCourtManagedBean() {
        vendorEntityToUpdate = new VendorEntity();
        newVendorEntity = new VendorEntity();
        uploadFrom = "";
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
        String accountType = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("accountType");

        Long foodCourtId;
        if (accountType.equals("Admin")) {
            foodCourtId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("foodCourtIdToUpdate");
        } else {
            foodCourtId = ((FoodCourtEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity")).getBusinessId();
        }
        try {
            currentFoodCourt = foodCourtEntityControllerLocal.retrieveFoodCourtById(foodCourtId);
        } catch (FoodCourtNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }

        try {
            vendorEntities = vendorEntityControllerLocal.retrieveAllVendorsByFoodCourtId(foodCourtId);
        } catch (FoodCourtNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No vendors found.", null));
        }

    }

    public void deleteVendor(ActionEvent event) {
        try {
            vendorEntityControllerLocal.deleteVendor(vendorToDisable.getBusinessId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Vendor deleted successfully", null));
            vendorEntities.remove(vendorToDisable);
        } catch (VendorNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting vendor: " + ex.getMessage(), null));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + e.getMessage(), null));
        }
    }

    public void upload(FileUploadEvent event) {
        BusinessEntity businessEntity = (BusinessEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity");
        FoodCourtEntity foodCourtEntity;

        if (businessEntity instanceof AdminEntity) {
            foodCourtEntity = (FoodCourtEntity) event.getComponent().getAttributes().get("foodCourtEntity");
        } else {
            foodCourtEntity = (FoodCourtEntity) businessEntity;
        }

        uploadFrom = (String) event.getComponent().getAttributes().get("uploadFrom");
        String status = (String) event.getComponent().getAttributes().get("status");
        String newFilePath;
        try {
            String fileName = "";
            if (uploadFrom.equals("vendor")) {
                newFilePath = System.getProperty("user.dir").replaceAll("config", "docroot").replaceFirst("docroot", "config") + System.getProperty("file.separator") + "queueme-uploads" + System.getProperty("file.separator") + "vendorLogos";
            } else {
                newFilePath = System.getProperty("user.dir").replaceAll("config", "docroot").replaceFirst("docroot", "config") + System.getProperty("file.separator") + "queueme-uploads" + System.getProperty("file.separator") + "foodCourtLogos";
            }

            file = new File(newFilePath);
            file = File.createTempFile("F0" + foodCourtEntity.getBusinessId(), ".png", file);
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

            if (uploadFrom.equals("vendor")) {
                if (status.equals("new")) {
                    newVendorEntity.setPhotoURL(file.getName());
                } else {
                    vendorEntityToUpdate.setPhotoURL(file.getName());
                    vendorEntityControllerLocal.updateFileUrl(vendorEntityToUpdate.getBusinessId(), file.getName());
                }
            } else {
                currentFoodCourt.setFileURL(file.getName());
                foodCourtEntityControllerLocal.updateFileUrl(currentFoodCourt.getBusinessId(), file.getName());
            }

            fileOutputStream.close();
            inputStream.close();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
    }

    public void updateFoodCourt(ActionEvent event) {
        try {
            foodCourtEntityControllerLocal.updateFoodCourt(currentFoodCourt);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Food Court updated successfully", null));
        } catch (FoodCourtNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating Food Court: " + ex.getMessage(), null));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + e.getMessage(), null));
        }
    }

    public void createVendorPage(ActionEvent event) {
        try {
            Long foodCourtIdToView = (Long) event.getComponent().getAttributes().get("foodCourtId");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("foodCourtIdToUpdate", foodCourtIdToView);
            FacesContext.getCurrentInstance().getExternalContext().redirect("createNewVendor.xhtml");
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
    }

    public void viewVendor(ActionEvent event) {
        try {
            Long vendorIdToView = (Long) event.getComponent().getAttributes().get("viewVendorId");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("vendorIdToView", vendorIdToView);
            FacesContext.getCurrentInstance().getExternalContext().redirect("mainPage.xhtml");
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
    }

    public void createNewVendor(ActionEvent event) throws IOException {
        try {
            Long foodCourtIdToView = (Long) event.getComponent().getAttributes().get("foodCourtId");
            FoodCourtEntity foodCourtEntity;

            try {
                foodCourtEntity = foodCourtEntityControllerLocal.retrieveFoodCourtById(foodCourtIdToView);
            } catch (FoodCourtNotFoundException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
                return;
            }

            VendorEntity vendorEntity = vendorEntityControllerLocal.createVendorEntity(newVendorEntity, foodCourtEntity);
            newVendorEntity = new VendorEntity();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New vendor created successfully (Vendor ID: " + vendorEntity.getVendorName() + ")", null));
            file = null;

            try {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("foodCourtIdToUpdate", foodCourtIdToView);
                FacesContext.getCurrentInstance().getExternalContext().redirect("foodCourtMainPage.xhtml");
            } catch (IOException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
            }

        } catch (DuplicateEmailUserException err) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error. Email is not Unique", null));
        }
    }

    public String onFlowProcess(FlowEvent event) {
        if (event.getNewStep().equals("addVendorForm")) {
            if (file == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please upload logo before moving to the next page.", ""));
                return "vendorLogo";
            }
        }

        return event.getNewStep();
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

    public FoodCourtEntity getCurrentFoodCourt() {
        return currentFoodCourt;
    }

    public void setCurrentFoodCourt(FoodCourtEntity currentFoodCourt) {
        this.currentFoodCourt = currentFoodCourt;
    }

    public VendorTypeEnum[] getVendorTypes() {
        return vendorTypes;
    }

    public void setVendorTypes(VendorTypeEnum[] vendorTypes) {
        this.vendorTypes = vendorTypes;
    }

    public VendorEntity getNewVendorEntity() {
        return newVendorEntity;
    }

    public void setNewVendorEntity(VendorEntity newVendorEntity) {
        this.newVendorEntity = newVendorEntity;
    }

    public String getUploadFrom() {
        return uploadFrom;
    }

    public void setUploadFrom(String uploadFrom) {
        this.uploadFrom = uploadFrom;
    }

}
