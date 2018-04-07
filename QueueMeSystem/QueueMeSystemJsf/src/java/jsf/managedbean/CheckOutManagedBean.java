/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MenuEntityControllerLocal;
import ejb.session.stateless.MenuItemEntityController;
import ejb.session.stateless.MenuItemEntityControllerLocal;
import ejb.session.stateless.SaleTransactionEntityControllerLocal;
import entity.CategoryEntity;
import entity.CustomerEntity;
import entity.MenuEntity;
import entity.MenuItemEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
import entity.VendorEntity;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import util.exception.CreateNewSaleTransactionException;
import util.exception.MenuItemNotFoundException;
import util.exception.MenuNotFoundException;

/**
 *
 * @author KERK
 */
@Named(value = "checkOutManagedBean")
@SessionScoped
public class CheckOutManagedBean implements Serializable {

    @EJB
    private MenuItemEntityControllerLocal menuItemEntityControllerLocal;

    @EJB
    private SaleTransactionEntityControllerLocal saleTransactionEntityControllerLocal;

    private List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities;
    private Integer totalLineItem;
    private Integer totalQuantity;
    private BigDecimal totalAmount;

    private Integer quantity;
    private String paymentType;
    private Boolean isTakeaway;

    public CheckOutManagedBean() {
        initialiseState();
    }

    private void initialiseState() {
        saleTransactionLineItemEntities = new ArrayList<>();
        totalLineItem = 0;
        totalQuantity = 0;
        totalAmount = new BigDecimal("0.00");
        quantity = 0;
        paymentType = null;
        isTakeaway = null;
    }

    public void addItem(MenuItemEntity menuItemEntity, String specialRequest, Integer menuItemQuantity) {
        //MenuItemEntity menuItemEntity; 
        try {
            //menuItemEntity = menuItemEntityControllerLocal.retrieveMenuItemById(skuCode);
            BigDecimal subTotal = menuItemEntity.getPrice().multiply(new BigDecimal(menuItemQuantity));

            ++totalLineItem;
            saleTransactionLineItemEntities.add(new SaleTransactionLineItemEntity(totalLineItem, menuItemQuantity, menuItemEntity.getPrice(), subTotal, specialRequest, menuItemEntity));
            totalQuantity += menuItemQuantity;
            totalAmount = totalAmount.add(subTotal);
           
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, menuItemEntity.getMenuItemName() + " added successfully!: " + menuItemQuantity + " unit @ " + NumberFormat.getCurrencyInstance().format(subTotal), null));
            //skuCode = "";
            quantity = 0;
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void removeItem(SaleTransactionLineItemEntity saleTransactionLineItemEntityToRemove) {
        totalAmount = totalAmount.subtract(saleTransactionLineItemEntityToRemove.getSubTotal());
        totalLineItem --;
        totalQuantity -= saleTransactionLineItemEntityToRemove.getQuantity();
        saleTransactionLineItemEntities.remove(saleTransactionLineItemEntityToRemove);
        int serialNum = 1;
        for(SaleTransactionLineItemEntity stlie: saleTransactionLineItemEntities) {
            stlie.setSerialNumber(serialNum);
            serialNum++;
        }
    }
    
    public void updateQuantity(ValueChangeEvent valueChangeEvent) {
        Integer oldValue = (Integer) valueChangeEvent.getOldValue();
        Integer newValue = (Integer) valueChangeEvent.getNewValue();
        SaleTransactionLineItemEntity saleTransactionLineItemEntity = (SaleTransactionLineItemEntity) valueChangeEvent.getComponent().getAttributes().get("lineItemToUpdate");
        
        int updateQty = newValue - oldValue;
        BigDecimal menuItemPrice = saleTransactionLineItemEntity.getMenuItemEntity().getPrice();
        BigDecimal updateSubTotal = menuItemPrice.multiply(new BigDecimal(updateQty));
        
        saleTransactionLineItemEntity.setSubTotal(saleTransactionLineItemEntity.getSubTotal().add(updateSubTotal));
        totalQuantity += updateQty;
        totalAmount = totalAmount.add(updateSubTotal);
    }

    public void doCheckout() throws CreateNewSaleTransactionException {
        if (saleTransactionLineItemEntities.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No item in cart.", null));
            return;
        } else if(isTakeaway == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a dining option.", null));
            return;
        } else if(paymentType == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a payment type.", null));
            return;
        }
        VendorEntity vendorEntity = (VendorEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity");
        SaleTransactionEntity newSaleTransactionEntity = saleTransactionEntityControllerLocal.createSaleTransaction(new SaleTransactionEntity(totalLineItem, totalQuantity, totalAmount, Calendar.getInstance().getTime(), Boolean.FALSE, isTakeaway));
        initialiseState();

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Checkout completed successfully (Sales Transaction ID: " + newSaleTransactionEntity.getSaleTransactionId() + ")", null));
    }

//    public void processCheckout(ActionEvent actionEvent) throws IOException{
//        FacesContext.getCurrentInstance().getExternalContext().redirect("processCheckout.xhtml");
//    }
    public void clearShoppingCart() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Shopping cart cleared", null));
        initialiseState();
    }

    public SaleTransactionEntityControllerLocal getSaleTransactionEntityControllerLocal() {
        return saleTransactionEntityControllerLocal;
    }

    public void setSaleTransactionEntityControllerLocal(SaleTransactionEntityControllerLocal saleTransactionEntityControllerLocal) {
        this.saleTransactionEntityControllerLocal = saleTransactionEntityControllerLocal;
    }

    public List<SaleTransactionLineItemEntity> getSaleTransactionLineItemEntities() {
        return saleTransactionLineItemEntities;
    }

    public void setSaleTransactionLineItemEntities(List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities) {
        this.saleTransactionLineItemEntities = saleTransactionLineItemEntities;
    }

    public Integer getTotalLineItem() {
        return totalLineItem;
    }

    public void setTotalLineItem(Integer totalLineItem) {
        this.totalLineItem = totalLineItem;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Boolean getIsTakeaway() {
        return isTakeaway;
    }

    public void setIsTakeaway(Boolean isTakeaway) {
        this.isTakeaway = isTakeaway;
    }

}
