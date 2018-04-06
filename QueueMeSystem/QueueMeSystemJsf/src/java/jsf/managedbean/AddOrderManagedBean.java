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

    public AddOrderManagedBean() {
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
    
    public void addMenuItem(ActionEvent event) {
        MenuItemEntity menuItemEntityToAdd = (MenuItemEntity) event.getComponent().getAttributes().get("menuItemEntityToAdd");
        checkOutManagedBean.addItem(menuItemEntityToAdd);
        System.err.println(checkOutManagedBean.getSaleTransactionLineItemEntities().size());
    }
    
//    public void onCellEdit(CellEditEvent event) {
//        Object oldValue = event.getOldValue();
//        Object newValue = event.getNewValue();
//         
//        if(newValue != null && !newValue.equals(oldValue)) {
//            SaleTransactionLineItemEntity saleTransactionLineItemEntity = (SaleTransactionLineItemEntity) event.getComponent().getAttributes().get("lineItem");
//            
//        }
//    }

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
}
