/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MenuEntityControllerLocal;
import entity.BusinessEntity;
import entity.CategoryEntity;
import entity.MenuEntity;
import entity.MenuItemEntity;
import entity.VendorEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author User
 */
@Named(value = "manageMenuManagedBean")
@ViewScoped
public class ManageMenuManagedBean implements Serializable {

    @EJB
    private MenuEntityControllerLocal menuEntityControllerLocal;

    MenuEntity menuEntity;
    List<MenuItemEntity> menuItemEntities;
    List<CategoryEntity> categoryEntities;
    
    public ManageMenuManagedBean() {
        menuItemEntities = new ArrayList<>();
    }
    
    @PostConstruct
    public void postConstruct() {
        VendorEntity vendorEntity = (VendorEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity");
        menuEntity = menuEntityControllerLocal.retrieveMenuByVendor((VendorEntity) vendorEntity);
        categoryEntities = menuEntity.getCategoryEntities();
        for(CategoryEntity categoryEntity: categoryEntities) {
            if(categoryEntity.getCategory().equals("Main")) {
                menuItemEntities.addAll(categoryEntity.getMenuItemEntities());
            }
        }
    }

    public MenuEntity getMenuEntity() {
        return menuEntity;
    }

    public void setMenuEntity(MenuEntity menuEntity) {
        this.menuEntity = menuEntity;
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
    
    
}
