/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;
import ejb.session.stateless.OrderEntityControllerLocal;
import ejb.session.stateless.SaleTransactionEntityControllerLocal;
import entity.CustomerEntity;
import entity.MenuItemEntity;
import entity.OrderEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
import entity.VendorEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import util.exception.CreateNewSaleTransactionException;

/**
 *
 * @author KERK
 */
@Stateful
public class CheckoutController implements CheckoutControllerLocal {

    @EJB(name = "OrderEntityControllerLocal")
    private OrderEntityControllerLocal orderEntityControllerLocal;

    @EJB(name = "SaleTransactionEntityControllerLocal")
    private SaleTransactionEntityControllerLocal saleTransactionEntityControllerLocal;
    
    private List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities;
    private SaleTransactionEntity saleTransactionEntity;
    private Integer totalLineItem;    
    private Integer totalQuantity;    
    private BigDecimal totalAmount;
    
    public CheckoutController() 
    {
        initialiseState();
    }
    
    private void initialiseState()
    {
        saleTransactionLineItemEntities = new ArrayList<>();
        totalLineItem = 0;
        totalQuantity = 0;
        totalAmount = new BigDecimal("0.00");
    }
    
    @Override
    public BigDecimal addItem(MenuItemEntity menuItemEntity, Integer quantity)
    {
        BigDecimal subTotal = menuItemEntity.getPrice().multiply(new BigDecimal(quantity));
        
        ++totalLineItem;      
        //saleTransactionLineItemEntities.add(new SaleTransactionLineItemEntity(totalLineItem, quantity, menuItemEntity.getPrice(), subTotal, Boolean.FALSE, 0));
        totalQuantity += quantity;
        totalAmount = totalAmount.add(subTotal);
        
        
        return subTotal;
    }
    
    @Override
    public SaleTransactionEntity doCheckout() //CustomerEntity customerEntity) throws CreateNewSaleTransactionException
    {
        SaleTransactionEntity newSaleTransactionEntity = saleTransactionEntityControllerLocal.createSaleTransaction(new SaleTransactionEntity(totalLineItem, totalQuantity, totalAmount, Calendar.getInstance(), Boolean.FALSE));
         
        initialiseState();
        
        return newSaleTransactionEntity;
    }
    
    @Override
    public void clearShoppingCart()
    {
        initialiseState();
    }
    
    @Override
    public List<SaleTransactionLineItemEntity> getSaleTransactionLineItemEntities() {
        return saleTransactionLineItemEntities;
    }

    @Override
    public void setSaleTransactionLineItemEntities(List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities) {
        this.saleTransactionLineItemEntities = saleTransactionLineItemEntities;
    }

    @Override
    public Integer getTotalLineItem() {
        return totalLineItem;
    }

    @Override
    public void setTotalLineItem(Integer totalLineItem) {
        this.totalLineItem = totalLineItem;
    }

    @Override
    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    @Override
    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    @Override
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    @Override
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
    
