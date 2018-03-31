/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MenuEntityControllerLocal;
import ejb.session.stateless.MenuItemEntityControllerLocal;
import entity.BusinessEntity;
import entity.CategoryEntity;
import entity.MenuEntity;
import entity.MenuItemEntity;
import entity.VendorEntity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import util.exception.VendorNotFoundException;

/**
 *
 * @author User
 */
@Named(value = "manageMenuManagedBean")
@ViewScoped
public class ManageMenuManagedBean implements Serializable {

    @EJB
    private MenuItemEntityControllerLocal menuItemEntityControllerLocal;

    @EJB
    private MenuEntityControllerLocal menuEntityControllerLocal;

    List<MenuEntity> menuEntities;
    List<MenuItemEntity> menuItemEntities;
    List<MenuItemEntity> menuItemEntitiesCopy;
    List<CategoryEntity> categoryEntities;

    List<SelectItem> selectItems;

    MenuEntity selectedMenuEntity;

    MenuItemEntity newMenuItemEntity;

    File file;
    
//    StreamedContent filePhoto;

    public ManageMenuManagedBean() {
        //selectedMenuEntity = new MenuEntity();
        menuItemEntities = new ArrayList<>();
        menuItemEntitiesCopy = new ArrayList<>();
        selectItems = new ArrayList<>();
        newMenuItemEntity = new MenuItemEntity();
    }

    @PostConstruct
    public void postConstruct() {
        VendorEntity vendorEntity = (VendorEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity");
        menuItemEntities = menuItemEntityControllerLocal.retrieveAllMenuItemEntityByVendor(vendorEntity);
        menuItemEntitiesCopy.addAll(menuItemEntities);
        menuEntities = menuEntityControllerLocal.retrieveMenusByVendor(vendorEntity);

        for (MenuEntity menuEntity : menuEntities) {
            selectItems.add(new SelectItem(menuEntity, menuEntity.getName(), menuEntity.getMenuId().toString()));
        }

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("MenuEntityConverter.menuEntities", menuEntities);
//        for(CategoryEntity categoryEntity: categoryEntities) {
//            if(categoryEntity.getCategory().equals("Main")) {
//                menuItemEntities.addAll(categoryEntity.getMenuItemEntities());
//            }
//        }
    }

//    public StreamedContent getFilePhoto() throws IOException {
//        filePhoto = new DefaultStreamedContent(new FileInputStream(file));
//        return filePhoto;
//    }
//
//    public void setFilePhoto(StreamedContent filePhoto) {
//        this.filePhoto = filePhoto;
//        
//    }
    

//    public StreamedContent getImage() throws IOException {
//        FacesContext context = FacesContext.getCurrentInstance();
//
//        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
//            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
//            return new DefaultStreamedContent();
//        } else {
//            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
//            String photoUrl = context.getExternalContext().getRequestParameterMap().get("photoURL");
//            String filePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + System.getProperty("file.separator");
//            return new DefaultStreamedContent(new FileInputStream(new File(filePath, photoUrl)));
//        }
//    }

    @PreDestroy
    public void preDestroy() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("MenuEntityConverter.menuEntities", null);
    }

    public void onChange() {
        System.err.println("changed");
    }

    public void onDrop(DragDropEvent ddEvent) {
        menuItemEntities.clear();
        menuItemEntities.addAll(menuItemEntitiesCopy);
        System.err.println("drop");
        menuEntities.get(0).getCategoryEntities().get(0).getMenuItemEntities().add((MenuItemEntity) ddEvent.getData());
    }

    public void handleFileUpload(FileUploadEvent event) {
        VendorEntity vendorEntity = (VendorEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity");
        try {
            String fileName = "";
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + System.getProperty("file.separator");

            System.err.println("********** Demo03ManagedBean.handleFileUpload(): File name: " + event.getFile().getFileName());
            System.err.println("********** Demo03ManagedBean.handleFileUpload(): newFilePath: " + newFilePath);

            file = new File(newFilePath);
            file = File.createTempFile("V0" + vendorEntity.getBusinessId(), ".png", file);
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

            fileOutputStream.close();
            inputStream.close();
            newMenuItemEntity.setPhotoURL(file.getName());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
    }

    public void createNewMenu() {

    }

    public void createNewMenuItem(ActionEvent event) {
        newMenuItemEntity.setPhotoURL(file.getName());
        VendorEntity vendorEntity = (VendorEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity");
        try {
            newMenuItemEntity = menuItemEntityControllerLocal.createMenuItem(newMenuItemEntity, vendorEntity);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, newMenuItemEntity.getMenuItemName() + " successfully created", null));

            menuItemEntities.add(newMenuItemEntity);
            menuItemEntitiesCopy.add(newMenuItemEntity);
            newMenuItemEntity = new MenuItemEntity();
            file = null;
        } catch (VendorNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while trying to retrieve your account details", null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void deleteItem(ActionEvent event) {
        MenuItemEntity menuItemEntityToDelete = (MenuItemEntity) event.getComponent().getAttributes().get("menuItemEntityToDelete");
        menuItemEntityControllerLocal.deleteMenuItem(menuItemEntityToDelete.getMenuItemId());
        menuItemEntities.remove(menuItemEntityToDelete);
        menuItemEntitiesCopy.remove(menuItemEntityToDelete);
        String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + System.getProperty("file.separator") + menuItemEntityToDelete.getPhotoURL();
        File deletePhoto = new File(newFilePath);
        if(deletePhoto.exists()) {
            deletePhoto.delete();
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product deleted successfully", null));
    }

    public String onFlowProcess(FlowEvent event) {
        if (event.getNewStep().equals("itemConfirmation")) {
            if (file == null) {
                System.err.println("in file");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please upload file before moving to the next page.", ""));
                return "itemImage";
            }
        }

        return event.getNewStep();
    }

    public List<MenuEntity> getMenuEntities() {
        return menuEntities;
    }

    public void setMenuEntities(List<MenuEntity> menuEntities) {
        this.menuEntities = menuEntities;
    }

    public List<MenuItemEntity> getMenuItemEntities() {
        return menuItemEntities;
    }

    public void setMenuItemEntities(List<MenuItemEntity> menuItemEntities) {
        this.menuItemEntities = menuItemEntities;
    }

    public List<CategoryEntity> getCategoryEntities() {
        return categoryEntities;
    }

    public void setCategoryEntities(List<CategoryEntity> categoryEntities) {
        this.categoryEntities = categoryEntities;
    }

    public MenuEntity getSelectedMenuEntity() {
        return selectedMenuEntity;
    }

    public void setSelectedMenuEntity(MenuEntity selectedMenuEntity) {
        this.selectedMenuEntity = selectedMenuEntity;
    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    public void setSelectItems(List<SelectItem> selectItems) {
        this.selectItems = selectItems;
    }

    public MenuItemEntity getNewMenuItemEntity() {
        return newMenuItemEntity;
    }

    public void setNewMenuItemEntity(MenuItemEntity newMenuItemEntity) {
        this.newMenuItemEntity = newMenuItemEntity;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
