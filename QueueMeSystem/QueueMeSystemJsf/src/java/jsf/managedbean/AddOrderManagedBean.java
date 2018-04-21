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
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import util.exception.CreateNewSaleTransactionException;
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

    private BigDecimal cashReceived;

    private String creditCardNumber;

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
        if (newValue != null && !newValue.equals(oldValue)) {
            SaleTransactionLineItemEntity saleTransactionLineItemEntity = (SaleTransactionLineItemEntity) event.getComponent().getAttributes().get("lineItem");

        }
    }

    public void processCheckout() {
        String paymentType = checkOutManagedBean.getPaymentType();
        if (checkOutManagedBean.getSaleTransactionLineItemEntities().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No item in cart.", null));
        } else if (checkOutManagedBean.getIsTakeaway() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a dining option.", null));
        } else if (paymentType == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a payment type.", null));
        } else {
            if (paymentType.equals("cash")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cash", ""));
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('dialogCashCheckout').show();");
            } else if (paymentType.equals("creditCard")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Credit Card", ""));
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('dialogCreditCardCheckout').show();");
            } else if (paymentType.equals("nets")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Nets", ""));
            } else if (paymentType.equals("paynow")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Paynow", ""));
            }
        }
    }

    public void confirmCheckout() throws CreateNewSaleTransactionException {
        RequestContext context = RequestContext.getCurrentInstance();

        if (checkOutManagedBean.getPaymentType().equals("cash")) {
            if (cashReceived.compareTo(checkOutManagedBean.getTotalAmount()) < 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid amount", ""));
                context.addCallbackParam("isSuccess", false);
            } else {
                checkOutManagedBean.doCheckout();
                context.addCallbackParam("isSuccess", true);
                cashReceived = null;
            }
        } else if (checkOutManagedBean.getPaymentType().equals("creditCard")) {
            checkOutManagedBean.doCheckout();
            context.addCallbackParam("isSuccess", true);
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

    public BigDecimal getCashReceived() {
        return cashReceived;
    }

    public void setCashReceived(BigDecimal cashReceived) {
        this.cashReceived = cashReceived;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

}
