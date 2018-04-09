package jsf.managedbean;

import ejb.session.stateless.VendorEntityControllerLocal;
import entity.FoodCourtEntity;
import entity.VendorEntity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import util.enumeration.VendorTypeEnum;
import util.exception.DuplicateEmailUserException;

@Named(value = "createNewVendorManagedBean")
@ViewScoped
public class CreateNewVendorManagedBean implements Serializable {

    @EJB
    private VendorEntityControllerLocal vendorEntityControllerLocal;

    private VendorEntity newVendorEntity;

    private VendorTypeEnum[] vendorTypes = VendorTypeEnum.values();

    private File file;

    /**
     * Creates a new instance of CreateNewVendorManagedBean
     */
    public CreateNewVendorManagedBean() {
        newVendorEntity = new VendorEntity();
    }

    public void createNewVendor(ActionEvent event) throws IOException{
        try {
            FoodCourtEntity foodCourtEntity = (FoodCourtEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity");
            VendorEntity vendorEntity = vendorEntityControllerLocal.createVendorEntity(newVendorEntity, foodCourtEntity);
            newVendorEntity = new VendorEntity();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New vendor created successfully (Vendor ID: " + vendorEntity.getVendorName() + ")", null));
            file = null;
            FacesContext.getCurrentInstance().getExternalContext().redirect("foodCourtMainPage.xhtml");
        } catch (DuplicateEmailUserException err) {
            System.err.println("IZZNOTUNIQUE");
            System.err.println(err.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error. Email is not Unique", null));
        }
    }

    public void upload(FileUploadEvent event) {
        FoodCourtEntity vendorEntity = (FoodCourtEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity");
        try {
            String fileName = "";
            String newFilePath = System.getProperty("user.dir").replaceAll("config", "docroot").replaceFirst("docroot", "config") + System.getProperty("file.separator") + "queueme-uploads" + System.getProperty("file.separator") + "foodPhotos";

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

            newVendorEntity.setPhotoURL(file.getName());

            fileOutputStream.close();
            inputStream.close();
            System.err.println(file);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
    }

    public String onFlowProcess(FlowEvent event) {
        if (event.getNewStep().equals("addVendorForm")) {
            if (file == null) {
                System.err.println("in file");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please upload logo before moving to the next page.", ""));
                return "vendorLogo";
            }
        }

        return event.getNewStep();
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
