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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.SelectEvent;

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
    
    public ManageMenuManagedBean() {
        //selectedMenuEntity = new MenuEntity();
        menuItemEntities = new ArrayList<>();
        menuItemEntitiesCopy = new ArrayList<>();
        selectItems = new ArrayList<>();
    }
    
    @PostConstruct
    public void postConstruct() {
        VendorEntity vendorEntity = (VendorEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity");
        menuItemEntities = menuItemEntityControllerLocal.retrieveAllMenuItemEntityByVendor(vendorEntity);
        menuItemEntitiesCopy.addAll(menuItemEntities);
        menuEntities = menuEntityControllerLocal.retrieveMenusByVendor(vendorEntity);
        
        for(MenuEntity menuEntity: menuEntities) {
            selectItems.add(new SelectItem(menuEntity, menuEntity.getName(), menuEntity.getMenuId().toString()));
        }
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("MenuEntityConverter.menuEntities", menuEntities);
//        for(CategoryEntity categoryEntity: categoryEntities) {
//            if(categoryEntity.getCategory().equals("Main")) {
//                menuItemEntities.addAll(categoryEntity.getMenuItemEntities());
//            }
//        }
    }
    
    @PreDestroy
    public void preDestroy()
    {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("MenuEntityConverter.menuEntities", null);
    }
    
    public void onChange(){
        System.err.println("changed");
    }
    public void onDrop(DragDropEvent ddEvent){
        menuItemEntities.clear();
        menuItemEntities.addAll(menuItemEntitiesCopy);
        System.err.println("drop");
        menuEntities.get(0).getCategoryEntities().get(0).getMenuItemEntities().add((MenuItemEntity) ddEvent.getData());
    }
    
    
    public void createNewMenu() {
        
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
    
}
