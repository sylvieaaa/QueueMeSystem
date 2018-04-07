/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MenuEntityControllerLocal;
import entity.CategoryEntity;
import entity.MenuEntity;
import entity.MenuItemEntity;
import entity.SaleTransactionLineItemEntity;
import entity.VendorEntity;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.event.CellEditEvent;
import util.exception.MenuNotFoundException;

/**
 *
 * @author User
 */
@Named(value = "addOrderManagedBean")
@ViewScoped
public class AddOrderManagedBean implements Serializable {

    @Inject
    private CheckOutManagedBean checkOutManagedBean;
    
    @EJB
    private MenuEntityControllerLocal menuEntityControllerLocal;

    private MenuEntity displayMenuEntity;
    private List<CategoryEntity> displayCategoryEntities;
    
    private Integer addMenuItemQty;
    private String addMenuItemSpecialReq;
    private MenuItemEntity menuItemEntityToAdd;
    
    public AddOrderManagedBean() {
        addMenuItemQty = 1;
    }

    @PostConstruct
    public void postConstruct() {
        VendorEntity vendorEntity = (VendorEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity");
        try {
            displayMenuEntity = menuEntityControllerLocal.retrieveDisplayMenu(vendorEntity);
            displayCategoryEntities = displayMenuEntity.getCategoryEntities();
        } catch (MenuNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No menu available for display", null));
        }
    }
    
    public void addMenuItem() {
        checkOutManagedBean.addItem(menuItemEntityToAdd, addMenuItemSpecialReq, addMenuItemQty);
        System.err.println(checkOutManagedBean.getSaleTransactionLineItemEntities().size());
        addMenuItemQty = 1;
        addMenuItemSpecialReq = "";
    }
    
     public void removeItem(ActionEvent event) {
         SaleTransactionLineItemEntity lineItemToRemove = (SaleTransactionLineItemEntity) event.getComponent().getAttributes().get("lineItem");
         checkOutManagedBean.removeItem(lineItemToRemove);
     }
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
         System.err.println(oldValue + " " + newValue);
        if(newValue != null && !newValue.equals(oldValue)) {
            SaleTransactionLineItemEntity saleTransactionLineItemEntity = (SaleTransactionLineItemEntity) event.getComponent().getAttributes().get("lineItem");
            
        }
    }

    public MenuEntity getDisplayMenuEntity() {
        return displayMenuEntity;
    }

    public void setDisplayMenuEntity(MenuEntity displayMenuEntity) {
        this.displayMenuEntity = displayMenuEntity;
    }

    public List<CategoryEntity> getDisplayCategoryEntities() {
        return displayCategoryEntities;
    }

    public void setDisplayCategoryEntities(List<CategoryEntity> displayCategoryEntities) {
        this.displayCategoryEntities = displayCategoryEntities;
    }

    public Integer getAddMenuItemQty() {
        return addMenuItemQty;
    }

    public void setAddMenuItemQty(Integer addMenuItemQty) {
        this.addMenuItemQty = addMenuItemQty;
    }

    public MenuItemEntity getMenuItemEntityToAdd() {
        return menuItemEntityToAdd;
    }

    public void setMenuItemEntityToAdd(MenuItemEntity menuItemEntityToAdd) {
        this.menuItemEntityToAdd = menuItemEntityToAdd;
    }

    public String getAddMenuItemSpecialReq() {
        return addMenuItemSpecialReq;
    }

    public void setAddMenuItemSpecialReq(String addMenuItemSpecialReq) {
        this.addMenuItemSpecialReq = addMenuItemSpecialReq;
    }
    
}
