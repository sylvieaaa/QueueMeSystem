/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.FoodCourtEntityControllerLocal;
import entity.AdminEntity;
import entity.FoodCourtEntity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import util.exception.DeleteFoodCourtException;
import util.exception.DuplicateEmailUserException;
import util.exception.FoodCourtNotFoundException;

/**
 *
 * @author SYLVIA
 */
@Named(value = "viewAllFoodCourtsManagedBean")
@ViewScoped
public class ViewAllFoodCourtsManagedBean implements Serializable {

    @EJB(name = "FoodCourtEntityControllerLocal")
    private FoodCourtEntityControllerLocal foodCourtEntityControllerLocal;

    private List<FoodCourtEntity> foodCourts;
    private List<FoodCourtEntity> filteredFoodCourts;
    private FoodCourtEntity foodCourt;
    private FoodCourtEntity foodCourtToView;
    private FoodCourtEntity foodCourtToUpdate;
    private FoodCourtEntity foodCourtToDisable;
    private FoodCourtEntity newFoodCourt;
    private File file;

    /**
     * Creates a new instance of ViewAllFoodCourtsManagedBean
     */
    public ViewAllFoodCourtsManagedBean() {
        foodCourts = new ArrayList<>();
        filteredFoodCourts = new ArrayList<>();
        foodCourt = new FoodCourtEntity();
        newFoodCourt = new FoodCourtEntity();
    }

    @PostConstruct
    public void postConstruct() {
        setFoodCourts(foodCourtEntityControllerLocal.retrieveAllFoodCourts());
        filteredFoodCourts = this.foodCourts;

    }

    public void createNewFoodCourt() throws IOException {
        try {
            newFoodCourt = foodCourtEntityControllerLocal.createFoodCourt(newFoodCourt);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Food Court created successfully (Id: " + newFoodCourt.getBusinessId() + ")", null));
            foodCourts.add(newFoodCourt);
            newFoodCourt = new FoodCourtEntity();
        } catch (DuplicateEmailUserException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error. Email is not Unique", null));
        }
    }

    public String onFlowProcess(FlowEvent event) {
        if (event.getNewStep().equals("foodcourtDetails")) {
            if (file == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please upload logo before moving to the next page.", ""));
                return "foodcourtPhoto";
            }
        }

        return event.getNewStep();
    }

    public void handleFileUpload(FileUploadEvent event) {
        AdminEntity fc = (AdminEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity");
        try {
            String fileName = "";
            String newFilePath = System.getProperty("user.dir").replaceAll("config", "docroot").replaceFirst("docroot", "config") + System.getProperty("file.separator") + "queueme-uploads" + System.getProperty("file.separator") + "foodCourtLogos";

            System.err.println("********** Demo03ManagedBean.handleFileUpload(): File name: " + event.getFile().getFileName());
            System.err.println("********** Demo03ManagedBean.handleFileUpload(): newFilePath: " + newFilePath);

            file = new File(newFilePath);
            file = File.createTempFile("V0" + fc.getBusinessId(), ".png", file);
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

            newFoodCourt.setFileURL(file.getName());
            System.out.println(file);
            fileOutputStream.close();
            inputStream.close();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
    }

    public void updateFoodCourt(ActionEvent event) {
        try {
            foodCourtEntityControllerLocal.updateFoodCourt(foodCourtToUpdate);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Food Court updated successfully", null));
        } catch (FoodCourtNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating Food Court: " + ex.getMessage(), null));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + e.getMessage(), null));
        }
    }

    public void disableFoodCourt(ActionEvent event) {

        try {
            foodCourtEntityControllerLocal.disableFoodCourt(foodCourtToDisable.getBusinessId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Food Court disabled successfully", null));
            foodCourts.remove(foodCourtToDisable);

        } catch (FoodCourtNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while disabling Food Court: " + ex.getMessage(), null));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + e.getMessage(), null));
        }

    }

    public List<FoodCourtEntity> getFoodCourts() {
        return foodCourts;
    }

    public void setFoodCourts(List<FoodCourtEntity> foodCourts) {
        this.foodCourts = foodCourts;
    }

    public List<FoodCourtEntity> getFilteredFoodCourts() {
        return filteredFoodCourts;
    }

    public void setFilteredFoodCourts(List<FoodCourtEntity> filteredFoodCourts) {
        this.filteredFoodCourts = filteredFoodCourts;
    }

    public FoodCourtEntity getFoodCourt() {
        return foodCourt;
    }

    public void setFoodCourt(FoodCourtEntity foodCourt) {
        this.foodCourt = foodCourt;
    }

    public FoodCourtEntity getFoodCourtToView() {
        return foodCourtToView;
    }

    public void setFoodCourtToView(FoodCourtEntity foodCourtToView) {
        this.foodCourtToView = foodCourtToView;
    }

    public FoodCourtEntity getFoodCourtToUpdate() {
        return foodCourtToUpdate;
    }

    public void setFoodCourtToUpdate(FoodCourtEntity foodCourtToUpdate) {
        this.foodCourtToUpdate = foodCourtToUpdate;
    }

    public FoodCourtEntity getFoodCourtToDisable() {
        return foodCourtToDisable;
    }

    public void setFoodCourtToDisable(FoodCourtEntity foodCourtToDisable) {
        this.foodCourtToDisable = foodCourtToDisable;
    }

    public FoodCourtEntity getNewFoodCourt() {
        return newFoodCourt;
    }

    public void setNewFoodCourt(FoodCourtEntity newFoodCourt) {
        this.newFoodCourt = newFoodCourt;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void viewFoodCourtDetails(ActionEvent event) throws IOException {
        Long foodCourtIdToView = (Long) event.getComponent().getAttributes().get("foodCourtId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("foodCourtIdToView", foodCourtIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewFoodCourtDetails.xhtml");
    }

    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

}
