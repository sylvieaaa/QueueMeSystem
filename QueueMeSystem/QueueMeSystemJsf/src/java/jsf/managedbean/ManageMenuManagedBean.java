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
import ejb.session.stateless.VendorEntityControllerLocal;
import entity.AdminEntity;
import entity.BusinessEntity;
import entity.CategoryEntity;
import entity.MenuEntity;
import entity.MenuItemEntity;
import entity.TagEntity;
import entity.VendorEntity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.UnselectEvent;
import util.exception.CategoryNotFoundException;
import util.exception.MenuItemNotFoundException;
import util.exception.MenuNotFoundException;
import util.exception.TagAlreadyExistException;
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

    @EJB
    private VendorEntityControllerLocal vendorEntityControllerLocal;

    List<MenuEntity> menuEntities;
    List<MenuItemEntity> menuItemEntities;
    List<MenuItemEntity> menuItemEntitiesCopy;
    List<CategoryEntity> categoryEntities;
    List<TagEntity> tagEntities;
    List<TagEntity> unSelectedtagEntities;

    List<TagEntity> selectedTagEntities;

    List<SelectItem> selectItems;

    MenuEntity selectedMenuEntity;

    MenuItemEntity newMenuItemEntity;
    MenuItemEntity menuItemEntityToEdit;
    MenuItemEntity menuItemEntityToView;
    File file;

    Boolean deleteMultipleItems;
    HashMap<MenuItemEntity, Boolean> menuItemEntitiesToDelete;

    MenuEntity newMenuEntity;

    TagEntity newTagEntity;

    CategoryEntity newCategoryEntity;
    CategoryEntity selectedCategoryEntity;
//    List<MenuItemEntity> menuItemEntitiesToDelete;
//    Integer activeTab;
//    StreamedContent filePhoto;

    VendorEntity currentVendorEntity;

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
        unSelectedtagEntities = new ArrayList<>();
        selectedTagEntities = new ArrayList<>();
        newTagEntity = new TagEntity();
//        activeTab = 0;
        currentVendorEntity = new VendorEntity();
    }

    @PostConstruct
    public void postConstruct() {
        BusinessEntity businessEntity = (BusinessEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity");

        if (businessEntity instanceof AdminEntity) {
            try {
                Long vendorId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("vendorId");
                currentVendorEntity = vendorEntityControllerLocal.retrieveVendorById(vendorId);
            } catch (VendorNotFoundException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
                return;
            }
        } else {
            currentVendorEntity = (VendorEntity) businessEntity;
        }
        menuItemEntities = menuItemEntityControllerLocal.retrieveAllMenuItemEntityByVendor(currentVendorEntity);
        menuItemEntitiesCopy.addAll(menuItemEntities);
        menuEntities = menuEntityControllerLocal.retrieveMenusByVendor(currentVendorEntity);

        for (MenuEntity menuEntity : menuEntities) {
            selectItems.add(new SelectItem(menuEntity, menuEntity.getName(), menuEntity.getMenuId().toString()));
        }

        for (MenuItemEntity mie : menuItemEntities) {
            menuItemEntitiesToDelete.put(mie, Boolean.TRUE);
        }

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("MenuEntityConverter.menuEntities", menuEntities);

        tagEntities = tagEntityControllerLocal.retrieveAllTags();
        unSelectedtagEntities.addAll(tagEntities);
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

        if (selectedCategoryEntity.getMenuItemEntities().contains(menuItemToBeAdded)) {
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
        BusinessEntity businessEntity = (BusinessEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity");

        if (!(businessEntity instanceof AdminEntity)) {
            currentVendorEntity = (VendorEntity) businessEntity;
        }
        
        String from = (String) event.getComponent().getAttributes().get("from");
        try {
            String fileName = "";
            String newFilePath = System.getProperty("user.dir").replaceAll("config", "docroot").replaceFirst("docroot", "config") + System.getProperty("file.separator") + "queueme-uploads" + System.getProperty("file.separator") + "foodPhotos";
            System.out.println(newFilePath);
            System.err.println("********** Demo03ManagedBean.handleFileUpload(): File name: " + event.getFile().getFileName());
            System.err.println("********** Demo03ManagedBean.handleFileUpload(): newFilePath: " + newFilePath);

            file = new File(newFilePath);
            file = File.createTempFile("V0" + currentVendorEntity.getBusinessId(), ".png", file);
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
                System.err.println("INDEED EDIT.");
            } else if (from.equals("create")) {
                newMenuItemEntity.setPhotoURL(file.getName());
            }

            fileOutputStream.close();
            inputStream.close();
            System.err.println("ALL CLOSED :D");
            System.err.println(file);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
            System.err.println("PRINT THIS");
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
    }

    public void createNewMenuItem(ActionEvent event) {
//        newMenuItemEntity.setPhotoURL(file.getName());
        BusinessEntity businessEntity = (BusinessEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity");

        if (businessEntity instanceof AdminEntity) {
            currentVendorEntity = (VendorEntity) event.getComponent().getAttributes().get("vendorEntity");
        } else {
            currentVendorEntity = (VendorEntity) businessEntity;
        }

        try {
            newMenuItemEntity.setTagEntities(selectedTagEntities);
            newMenuItemEntity = menuItemEntityControllerLocal.createMenuItem(newMenuItemEntity, currentVendorEntity);
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, newMenuItemEntity.getMenuItemName() + " successfully created", ""));

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
//        System.err.println("in ");
    }

    public void saveMenuItemEdit(ActionEvent event) {
        try {
            menuItemEntityControllerLocal.updateMenuItem(menuItemEntityToEdit);
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Menu item: " + menuItemEntityToEdit.getMenuItemName() + " updated successfully", ""));
        } catch (MenuItemNotFoundException ex) {
            System.err.println("me");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while trying to retrieve the item details", ""));
        } catch (Exception ex) {
            System.err.println("ex");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), ""));
        }
    }

    public void createNewMenu(ActionEvent event) {
        try {
            BusinessEntity businessEntity = (BusinessEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity");

            if (businessEntity instanceof AdminEntity) {
                currentVendorEntity = (VendorEntity) event.getComponent().getAttributes().get("vendorEntity");
            } else {
                currentVendorEntity = (VendorEntity) businessEntity;
            }

            menuEntityControllerLocal.createMenu(newMenuEntity, currentVendorEntity);
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

    public void deleteMenu(ActionEvent event) {
        BusinessEntity businessEntity = (BusinessEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity");

        if (businessEntity instanceof AdminEntity) {
            currentVendorEntity = (VendorEntity) event.getComponent().getAttributes().get("vendorEntity");
        } else {
            currentVendorEntity = (VendorEntity) businessEntity;
        }

        try {
            menuEntityControllerLocal.removeMenuEntity(selectedMenuEntity, currentVendorEntity);
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
                if (selectedMenuEntity.getCategoryEntities().size() == 1 || selectedMenuEntity.getCategoryEntities().size() <= indexOfObject) {
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
        for (MenuEntity me : menuEntities) {
            if (!me.equals(selectedMenuEntity)) {
                me.setSelected(Boolean.FALSE);
            }
        }

        menuEntityControllerLocal.selectDefaultMenu(selectedMenuEntity, currentVendorEntity);
    }

    public void onTabChange(TabChangeEvent event) {
        String catTitle = event.getTab().getTitle();
        for (CategoryEntity ce : selectedMenuEntity.getCategoryEntities()) {
            if (ce.getName().equals(catTitle)) {
                selectedCategoryEntity = ce;
                return;
            }
        }
    }

    public List<TagEntity> completeTag(String query) {
        List<TagEntity> filteredTags = new ArrayList<>();
        for (TagEntity tagEntity : unSelectedtagEntities) {
            if (tagEntity.getType().toLowerCase().startsWith(query)) {
                filteredTags.add(tagEntity);
            }
        }
        return filteredTags;
    }

    public void dialogEditOpen(ActionEvent event) {
        menuItemEntityToEdit = (MenuItemEntity) event.getComponent().getAttributes().get("menuItemEntityToEdit");

        System.err.println(menuItemEntityToEdit + " null");
        System.err.println(menuItemEntityToEdit.getTagEntities());
        if (menuItemEntityToEdit.getTagEntities() == null || menuItemEntityToEdit.getTagEntities().isEmpty()) {
            for (TagEntity tagEntity : menuItemEntityToEdit.getTagEntities()) {
                unSelectedtagEntities.remove(tagEntity);
            }
        }
    }

    public void dialogEditClose(CloseEvent event) {
        unSelectedtagEntities.clear();
        unSelectedtagEntities.addAll(tagEntities);
    }

    public void onTagSelect(SelectEvent event) {
        unSelectedtagEntities.remove((TagEntity) event.getObject());
    }

    public void onTagUnselect(UnselectEvent event) {
        unSelectedtagEntities.add((TagEntity) event.getObject());
    }

    public void createNewTag(ActionEvent event) {
//        RequestContext context = RequestContext.getCurrentInstance();
        try {
            newTagEntity = tagEntityControllerLocal.createTagEntity(newTagEntity);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Tag: " + newTagEntity.getType() + " created", ""));
            tagEntities.add(newTagEntity);
            unSelectedtagEntities.add(newTagEntity);

            newTagEntity = new TagEntity();
//            context.addCallbackParam("isSuccess", true);
        } catch (TagAlreadyExistException ex) {
            System.err.println("tex");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ""));
//            context.addCallbackParam("isSuccess", false);
        } catch (Exception ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), ""));
        }
    }

    public void backToVendor(ActionEvent event) {
        try {
            Long vendorId = (Long) event.getComponent().getAttributes().get("vendorId");
//            String from = (String) event.getComponent().getAttributes().get("from");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("vendorIdToView", vendorId);
//            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("from", from);
            FacesContext.getCurrentInstance().getExternalContext().redirect("mainPage.xhtml");
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
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
        System.err.println(menuItemEntityToEdit.getTagEntities());
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
    public List<TagEntity> getTagEntities() {
        return tagEntities;
    }

    public void setTagEntities(List<TagEntity> tagEntities) {
        this.tagEntities = tagEntities;
    }

    public List<TagEntity> getSelectedTagEntities() {
        return selectedTagEntities;
    }

    public void setSelectedTagEntities(List<TagEntity> selectedTagEntities) {
        this.selectedTagEntities = selectedTagEntities;
    }

    public TagEntity getNewTagEntity() {
        return newTagEntity;
    }

    public void setNewTagEntity(TagEntity newTagEntity) {
        this.newTagEntity = newTagEntity;
    }

    public VendorEntity getCurrentVendorEntity() {
        return currentVendorEntity;
    }

    public void setCurrentVendorEntity(VendorEntity currentVendorEntity) {
        this.currentVendorEntity = currentVendorEntity;
    }

    public List<MenuItemEntity> getMenuItemEntitiesCopy() {
        return menuItemEntitiesCopy;
    }

    public void setMenuItemEntitiesCopy(List<MenuItemEntity> menuItemEntitiesCopy) {
        this.menuItemEntitiesCopy = menuItemEntitiesCopy;
    }

    public List<TagEntity> getUnSelectedtagEntities() {
        return unSelectedtagEntities;
    }

    public void setUnSelectedtagEntities(List<TagEntity> unSelectedtagEntities) {
        this.unSelectedtagEntities = unSelectedtagEntities;
    }
}
