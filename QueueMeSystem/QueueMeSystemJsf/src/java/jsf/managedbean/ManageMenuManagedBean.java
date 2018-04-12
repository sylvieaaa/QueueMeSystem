/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CategoryEntityControllerLocal;
import ejb.session.stateless.MenuEntityControllerLocal;
import ejb.session.stateless.MenuItemEntityControllerLocal;
import ejb.session.stateless.TagEntityControllerLocal;
import entity.BusinessEntity;
import entity.CategoryEntity;
import entity.MenuEntity;
import entity.MenuItemEntity;
import entity.TagEntity;
import entity.VendorEntity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.primefaces.context.RequestContext;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import util.exception.CategoryNotFoundException;
import util.exception.MenuItemNotFoundException;
import util.exception.MenuNotFoundException;
import util.exception.VendorNotFoundException;

/**
 *
 * @author User
 */
@Named(value = "manageMenuManagedBean")
@ViewScoped
public class ManageMenuManagedBean implements Serializable {

    @EJB
    private TagEntityControllerLocal tagEntityControllerLocal;

    @EJB
    private CategoryEntityControllerLocal categoryEntityControllerLocal;

    @EJB
    private MenuItemEntityControllerLocal menuItemEntityControllerLocal;

    @EJB
    private MenuEntityControllerLocal menuEntityControllerLocal;

    List<MenuEntity> menuEntities;
    List<MenuItemEntity> menuItemEntities;
    List<MenuItemEntity> menuItemEntitiesCopy;
    List<CategoryEntity> categoryEntities;
    List<TagEntity> tagEntities;

    List<SelectItem> selectItems;

    MenuEntity selectedMenuEntity;

    MenuItemEntity newMenuItemEntity;
    MenuItemEntity menuItemEntityToEdit;
    MenuItemEntity menuItemEntityToView;
    File file;

    Boolean deleteMultipleItems;
    HashMap<MenuItemEntity, Boolean> menuItemEntitiesToDelete;

    MenuEntity newMenuEntity;

    CategoryEntity newCategoryEntity;
    CategoryEntity selectedCategoryEntity;
//    List<MenuItemEntity> menuItemEntitiesToDelete;
//    Integer activeTab;
//    StreamedContent filePhoto;
    public ManageMenuManagedBean() {
        //selectedMenuEntity = new MenuEntity();
        menuItemEntities = new ArrayList<>();
        menuItemEntitiesCopy = new ArrayList<>();
        selectItems = new ArrayList<>();
        newMenuItemEntity = new MenuItemEntity();
        deleteMultipleItems = false;
//        menuItemEntitiesToDelete = new ArrayList<>();
        menuItemEntitiesToDelete = new HashMap<>();
        menuItemEntityToEdit = null;
        menuItemEntityToView = null;
        newMenuEntity = new MenuEntity();
        newCategoryEntity = new CategoryEntity();
//        activeTab = 0;
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

        for (MenuItemEntity mie : menuItemEntities) {
            menuItemEntitiesToDelete.put(mie, Boolean.TRUE);
        }

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("MenuEntityConverter.menuEntities", menuEntities);
        
        tagEntities = tagEntityControllerLocal.retrieveAllTags();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("TagEntityConverter.tagEntities", tagEntities);
//        for(CategoryEntity categoryEntity: categoryEntities) {
//            if(categoryEntity.getCategory().equals("Main")) {
//                menuItemEntities.addAll(categoryEntity.getMenuItemEntities());
//            }
//        }
    }

    @PreDestroy
    public void preDestroy() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("MenuEntityConverter.menuEntities", null);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("TagEntityConverter.tagEntities", null);
    }

    public void onChange() {
        if (selectedMenuEntity != null && !selectedMenuEntity.getCategoryEntities().isEmpty()) {
            selectedCategoryEntity = selectedMenuEntity.getCategoryEntities().get(0);
        }
    }

    public void onDrop(DragDropEvent ddEvent) {
        MenuItemEntity menuItemToBeAdded = (MenuItemEntity) ddEvent.getData();
        menuItemEntities.clear();
        menuItemEntities.addAll(menuItemEntitiesCopy);
        if (selectedCategoryEntity == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please create a category before adding items", ""));
            return;
        }
        
        if(selectedCategoryEntity.getMenuItemEntities().contains(menuItemToBeAdded)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Item already added to category", ""));
            return;
        }
        selectedCategoryEntity.getMenuItemEntities().add(menuItemToBeAdded);
        try {
            categoryEntityControllerLocal.addMenuItem(selectedCategoryEntity, menuItemToBeAdded);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item added", ""));
        } catch (CategoryNotFoundException | MenuItemNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while trying to retrieve menu details", ""));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), ""));
        }
    }

    public void removeItemFromCategory(ActionEvent event) {
        MenuItemEntity menuItemToBeRemoved = (MenuItemEntity) event.getComponent().getAttributes().get("menuItem");
        selectedCategoryEntity.getMenuItemEntities().remove(menuItemToBeRemoved);
        System.err.println(menuItemToBeRemoved);
        try {
            categoryEntityControllerLocal.removeMenuItem(selectedCategoryEntity, menuItemToBeRemoved);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item removed", ""));
        } catch (CategoryNotFoundException | MenuItemNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while trying to retrieve menu details", ""));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), ""));
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        VendorEntity vendorEntity = (VendorEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity");
        String from = (String) event.getComponent().getAttributes().get("from");
        try {
            String fileName = "";
            String newFilePath = System.getProperty("user.dir").replaceAll("config", "docroot").replaceFirst("docroot", "config") + System.getProperty("file.separator") + "queueme-uploads" + System.getProperty("file.separator") + "foodPhotos";
            System.out.println(newFilePath);
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

            if (from.equals("edit")) {
                menuItemEntityToEdit.setPhotoURL(file.getName());
            } else if (from.equals("create")) {
                newMenuItemEntity.setPhotoURL(file.getName());
            }

            fileOutputStream.close();
            inputStream.close();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
    }

    public void createNewMenuItem(ActionEvent event) {
        newMenuItemEntity.setPhotoURL(file.getName());
        VendorEntity vendorEntity = (VendorEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity");
        try {
            newMenuItemEntity = menuItemEntityControllerLocal.createMenuItem(newMenuItemEntity, vendorEntity);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, newMenuItemEntity.getMenuItemName() + " successfully created", ""));

            menuItemEntities.add(newMenuItemEntity);
            menuItemEntitiesCopy.add(newMenuItemEntity);
            newMenuItemEntity = new MenuItemEntity();
            file = null;
        } catch (VendorNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while trying to retrieve your account details", null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), ""));
        }
    }

    public void deleteItem(ActionEvent event) {
        MenuItemEntity menuItemEntityToDelete = (MenuItemEntity) event.getComponent().getAttributes().get("menuItemEntityToDelete");
        menuItemEntityControllerLocal.deleteMenuItem(menuItemEntityToDelete.getMenuItemId());
        menuItemEntities.remove(menuItemEntityToDelete);
        menuItemEntitiesCopy.remove(menuItemEntityToDelete);
        for (CategoryEntity categoryEntity : menuEntities.get(0).getCategoryEntities()) {
            List<MenuItemEntity> mie = categoryEntity.getMenuItemEntities();
            mie.remove(menuItemEntityToDelete);
        }
        String newFilePath = System.getProperty("user.dir").replaceAll("config", "docroot").replaceFirst("docroot", "config") + System.getProperty("file.separator") 
                + "queueme-uploads" + System.getProperty("file.separator") + "foodPhotos" + System.getProperty("file.separator") + menuItemEntityToDelete.getPhotoURL();
        File deletePhoto = new File(newFilePath);
        if (deletePhoto.exists()) {
            deletePhoto.delete();
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Menu item deleted successfully", ""));
    }

    public String onFlowProcess(FlowEvent event) {
        if (event.getNewStep().equals("itemConfirmation")) {
            if (file == null) {
                System.err.println("in file");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please upload photo before moving to the next page.", ""));
                return "itemImage";
            }
        }

        return event.getNewStep();
    }

    public void toggleDeleteMultipleItems(ActionEvent event) {
        deleteMultipleItems = !deleteMultipleItems;
    }

    public void updateItemsToDelete() {
//        MenuItemEntity menuItemEntity = (MenuItemEntity) event.getComponent().getAttributes().get("menuItemToDelete");
//        if(menuItemEntitiesToDelete.contains(menuItemEntity)) {
//            menuItemEntitiesToDelete.remove(menuItemEntity);
//        } else {
//            menuItemEntitiesToDelete.add(menuItemEntity);
//        }

//        System.err.println("Added " + menuItemEntity.getMenuItemName() + " " + menuItemEntitiesToDelete.size());
        System.err.println("in ");
    }

    public void saveMenuItemEdit(ActionEvent event) {
        try {
            menuItemEntityControllerLocal.updateMenuItem(menuItemEntityToEdit);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Menu item: " + menuItemEntityToEdit.getMenuItemName() + " updated successfully", ""));
        } catch (MenuItemNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while trying to retrieve the item details", ""));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), ""));
        }
    }

    public void createNewMenu(ActionEvent event) {
        try {
            VendorEntity vendorEntity = (VendorEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity");
            menuEntityControllerLocal.createMenu(newMenuEntity, vendorEntity);
            menuEntities.add(newMenuEntity);
            selectItems.add(new SelectItem(newMenuEntity, newMenuEntity.getName(), newMenuEntity.getMenuId().toString()));

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New menu: " + newMenuEntity.getName() + " is created.", ""));
            newMenuEntity = new MenuEntity();
        } catch (VendorNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while trying to retrieve your account details", ""));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), ""));
        }
    }

    public void deleteMenu() {
        VendorEntity vendorEntity = (VendorEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity");
        try {
            menuEntityControllerLocal.removeMenuEntity(selectedMenuEntity, vendorEntity);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("MenuEntityConverter.menuEntities", menuEntities);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, selectedMenuEntity.getName() + " is deleted.", ""));
            int size = selectItems.size();
            for (int i = 0; i < size; i++) {
                if (selectItems.get(i).getValue().equals(selectedMenuEntity)) {
                    System.err.println("remove selected");
                    selectItems.remove(i);
                    break;
                }
            }
            menuEntities.remove(selectedMenuEntity);
            selectedMenuEntity = null;
            selectedCategoryEntity = null;
        } catch (VendorNotFoundException | MenuNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while trying to retrieve your account details", ""));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), ""));
        }
    }

    public void createNewCategory(ActionEvent event) {
        try {
            categoryEntityControllerLocal.createCategory(newCategoryEntity, selectedMenuEntity);
            
            if (selectedMenuEntity.getCategoryEntities().isEmpty()) {
                System.err.println("empty");
                selectedCategoryEntity = newCategoryEntity;
            }
            selectedMenuEntity.getCategoryEntities().add(newCategoryEntity);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New category: " + newCategoryEntity.getName() + " is created.", ""));

            newCategoryEntity = new CategoryEntity();
        } catch (MenuNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while trying to retrieve menu details", ""));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), ""));
        }
    }

    public void deleteCategory() {
        try {
            menuEntityControllerLocal.removeCategoryFromMenu(selectedMenuEntity, selectedCategoryEntity);

            int indexOfObject = selectedMenuEntity.getCategoryEntities().indexOf(selectedCategoryEntity);
            selectedMenuEntity.getCategoryEntities().remove(selectedCategoryEntity);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Category: " + selectedCategoryEntity.getName() + " succesfully removed.", ""));
            if (!selectedMenuEntity.getCategoryEntities().isEmpty()) {
                if(selectedMenuEntity.getCategoryEntities().size() == 1 || selectedMenuEntity.getCategoryEntities().size() <= indexOfObject) {
                    indexOfObject = 0;
                } 
                selectedCategoryEntity = selectedMenuEntity.getCategoryEntities().get(indexOfObject);
            } else {
                selectedCategoryEntity = null;
            }
        } catch (CategoryNotFoundException | MenuNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while trying to retrieve menu/category details", ""));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), ""));
        }
    }

    public void changeDefaultMenu() {
        VendorEntity vendorEntity = (VendorEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity");
        for (MenuEntity me : menuEntities) {
            if (!me.equals(selectedMenuEntity)) {
                me.setSelected(Boolean.FALSE);
            }
        }

        menuEntityControllerLocal.selectDefaultMenu(selectedMenuEntity, vendorEntity);
    }

    public void onTabChange(TabChangeEvent event) {
        System.err.println("called");
        String catTitle = event.getTab().getTitle();
        for (CategoryEntity ce : selectedMenuEntity.getCategoryEntities()) {
            if (ce.getName().equals(catTitle)) {
                selectedCategoryEntity = ce;
                return;
            }
        }
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

    public Boolean getDeleteMultipleItems() {
        return deleteMultipleItems;
    }

    public void setDeleteMultipleItems(Boolean deleteMultipleItems) {
        this.deleteMultipleItems = deleteMultipleItems;
    }

    public HashMap<MenuItemEntity, Boolean> getMenuItemEntitiesToDelete() {
        System.err.println("getting");
        return menuItemEntitiesToDelete;
    }

    public void setMenuItemEntitiesToDelete(HashMap<MenuItemEntity, Boolean> menuItemEntitiesToDelete) {
        System.err.println("setting");
        this.menuItemEntitiesToDelete = menuItemEntitiesToDelete;
    }

    public MenuItemEntity getMenuItemEntityToEdit() {
        return menuItemEntityToEdit;
    }

    public void setMenuItemEntityToEdit(MenuItemEntity menuItemEntityToEdit) {
        this.menuItemEntityToEdit = menuItemEntityToEdit;
    }

    public MenuEntity getNewMenuEntity() {
        return newMenuEntity;
    }

    public void setNewMenuEntity(MenuEntity newMenuEntity) {
        this.newMenuEntity = newMenuEntity;
    }

    public CategoryEntity getNewCategoryEntity() {
        return newCategoryEntity;
    }

    public void setNewCategoryEntity(CategoryEntity newCategoryEntity) {
        this.newCategoryEntity = newCategoryEntity;
    }

    public CategoryEntity getSelectedCategoryEntity() {
        return selectedCategoryEntity;
    }

    public void setSelectedCategoryEntity(CategoryEntity selectedCategoryEntity) {
        this.selectedCategoryEntity = selectedCategoryEntity;
    }

    public MenuItemEntity getMenuItemEntityToView() {
        return menuItemEntityToView;
    }

    public void setMenuItemEntityToView(MenuItemEntity menuItemEntityToView) {
        this.menuItemEntityToView = menuItemEntityToView;
    }

//    public Integer getActiveTab() {
//        System.err.println(activeTab);
//        return activeTab;
//    }
//
//    public void setActiveTab(Integer activeTab) {
//        this.activeTab = activeTab;
//    }

}
