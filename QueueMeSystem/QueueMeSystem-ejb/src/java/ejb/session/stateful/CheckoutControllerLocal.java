/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import entity.CustomerEntity;
import entity.MenuItemEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewSaleTransactionException;

/**
 *
 * @author KERK
 */
@Local
public interface CheckoutControllerLocal {
    
    BigDecimal addItem(MenuItemEntity menuItemEntity, Integer quantity);
    
    SaleTransactionEntity doCheckout() throws CreateNewSaleTransactionException;

    void clearShoppingCart();

    

    List<SaleTransactionLineItemEntity> getSaleTransactionLineItemEntities();
    
    void setSaleTransactionLineItemEntities(List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities);

    Integer getTotalLineItem();
    
    void setTotalLineItem(Integer totalLineItem);
    
    Integer getTotalQuantity();
    
    void setTotalQuantity(Integer totalQuantity);
    
    BigDecimal getTotalAmount();

    void setTotalAmount(BigDecimal totalAmount);
    
}
