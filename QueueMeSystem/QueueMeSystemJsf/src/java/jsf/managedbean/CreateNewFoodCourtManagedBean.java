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
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author SYLVIA
 */
@Named(value = "createNewFoodCourtManagedBean")
@ViewScoped
public class CreateNewFoodCourtManagedBean implements Serializable{

    @EJB(name = "FoodCourtEntityControllerLocal")
    private FoodCourtEntityControllerLocal foodCourtEntityControllerLocal;

    private FoodCourtEntity newFoodCourt;
    
    
    private File file;

    /**
     * Creates a new instance of CreateNewFoodCourtManagedBean
     */

    public CreateNewFoodCourtManagedBean() {
        newFoodCourt = new FoodCourtEntity();
    }

    public FoodCourtEntity getNewFoodCourt() {
        return newFoodCourt;
    }

    public void setNewFoodCourt(FoodCourtEntity newFoodCourt) {
        this.newFoodCourt = newFoodCourt;
    }

    public void createNewFoodCourt() throws IOException {

        FoodCourtEntity fc = foodCourtEntityControllerLocal.createFoodCourt(newFoodCourt);
        newFoodCourt = new FoodCourtEntity();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Food Court created successfully (Id: " + fc.getBusinessId()+ ")", null));
        
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
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

}
