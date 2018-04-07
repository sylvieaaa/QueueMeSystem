/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AdminEntityControllerLocal;
import ejb.session.stateless.CategoryEntityControllerLocal;
import ejb.session.stateless.CustomerEntityControllerLocal;
import ejb.session.stateless.FoodCourtEntityControllerLocal;
import ejb.session.stateless.MenuEntityControllerLocal;
import ejb.session.stateless.MenuItemEntityControllerLocal;
import ejb.session.stateless.OrderEntityControllerLocal;
import ejb.session.stateless.ReviewEntityControllerLocal;
import ejb.session.stateless.SaleTransactionEntityControllerLocal;
import ejb.session.stateless.VendorEntityControllerLocal;
import entity.AdminEntity;
import entity.CategoryEntity;
import entity.CustomerEntity;
import entity.FoodCourtEntity;
import entity.MenuEntity;
import entity.MenuItemEntity;
import entity.OrderEntity;
import entity.ReviewEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
import entity.VendorEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.AdminNotFoundException;

@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {

    @EJB
    private ReviewEntityControllerLocal reviewEntityControllerLocal;

    @EJB
    private SaleTransactionEntityControllerLocal saleTransactionEntityControllerLocal;

    @EJB
    private OrderEntityControllerLocal orderEntityControllerLocal;

    @EJB
    private MenuEntityControllerLocal menuEntityControllerLocal;

    @EJB
    private CategoryEntityControllerLocal categoryEntityControllerLocal;

    @EJB
    private CustomerEntityControllerLocal customerEntityControllerLocal;

    @EJB
    private MenuItemEntityControllerLocal menuItemEntityControllerLocal;

    @EJB
    private VendorEntityControllerLocal vendorEntityControllerLocal;

    @EJB
    private FoodCourtEntityControllerLocal foodCourtEntityControllerLocal;

    @EJB
    private AdminEntityControllerLocal adminEntityControllerLocal;
    

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")
    private EntityManager em;

    public DataInitializationSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            adminEntityControllerLocal.retrieveAdminByUsername("yxsoong");
        } catch (AdminNotFoundException ex) {
            initializeDate();
        }
    }

    private void initializeDate() {
        try {

            // Initialize admin entities
            adminEntityControllerLocal.createAdmin(new AdminEntity("Yi Xuan", "Soong", "yxsoong", "password"));
            adminEntityControllerLocal.createAdmin(new AdminEntity("Zhu Zhi", "Kerk", "kzhuzhi", "password"));
            adminEntityControllerLocal.createAdmin(new AdminEntity("Sylvia", "Swee", "sylvia", "password"));
            adminEntityControllerLocal.createAdmin(new AdminEntity("Rui Jia", "Low", "lruijia", "password"));
            
            CustomerEntity customerEntity = customerEntityControllerLocal.createCustomer(new CustomerEntity("customer", "first", "98765432", "abc street", "customer@gmail.com", "password"));

            // Initialize FoodCourt Entity
            Date calendarStart = new Date(0, 0, 0, 8, 0);
            Date calendarEnd = new Date(0, 0, 0, 22, 0);
   
            FoodCourtEntity foodCourtEntity = foodCourtEntityControllerLocal.createFoodCourt(new FoodCourtEntity("ABC Food Court", "BEST in SG", "ABC Road", "123123", calendarStart, calendarEnd, "ABCFoodCourt", "password", "fc1.png"));
            foodCourtEntityControllerLocal.createFoodCourt(new FoodCourtEntity("Royal Food Court", "Best in Kent Ridge", "Kent Ridge Drive 123", "117417", calendarStart, calendarEnd, "royalfoodcourt", "password", "fc1.png"));
            foodCourtEntityControllerLocal.createFoodCourt(new FoodCourtEntity("Grandfather's Food Court", "Best in Ang Mo Kio", "AMK Drive 666", "143245", calendarStart, calendarEnd, "amkfoodcourt", "password", "fc1.png"));
            foodCourtEntityControllerLocal.createFoodCourt(new FoodCourtEntity("KorKor Food Court", "Best in Changi", "Changi Drive 888", "555555", calendarStart, calendarEnd, "changifoodcourt", "password", "fc1.png"));
            foodCourtEntityControllerLocal.createFoodCourt(new FoodCourtEntity("JieJie Food Court", "Best in Redhill", "Redhill Drive 555", "777654", calendarStart, calendarEnd, "redhillfoodcourt", "password", "fc1.png"));

            VendorEntity chinese = vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Singapore Chinese Food", "Chinese", 0, "Best Chicken rice in KR!", calendarStart, calendarEnd, BigDecimal.ZERO, "chinese", "password", "chicken_rice.png"), foodCourtEntity);
            vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Minah's Malay Food", "Halal",0, "Best Halal store in SG!", calendarStart, calendarEnd, BigDecimal.ZERO, "malay", "password", "chicken_rice.png"), foodCourtEntity);
            vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Uncle Charlie's Western", "Western", 0, "Taste of USA in KR!", calendarStart, calendarEnd, BigDecimal.ZERO, "western", "password", "chicken_rice.png"), foodCourtEntity);
            vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Ah Seng Drink Stores", "Beverages", 0, "Thirsty no more!", calendarStart, calendarEnd, BigDecimal.ZERO, "drink", "password", "chicken_rice.png"), foodCourtEntity);
            vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Best Fruit Store", "Fruits", 0, "Eat me and be healthy", calendarEnd, calendarEnd, BigDecimal.ZERO, "fruit", "password", "chicken_rice.png"), foodCourtEntity);
            vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Muthu Curry", "Indian", 0, "Cheapest prata in SG!", calendarEnd, calendarEnd, BigDecimal.ZERO, "indian", "password", "chicken_rice.png"), foodCourtEntity);
              
            MenuItemEntity chickenRice = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Chicken Rice", "Roasted or white", new BigDecimal("2.90"), "chicken_rice.png"), chinese);
            MenuItemEntity duckRice = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Roasted Duck Rice", "Authentic HK taste", new BigDecimal("2.90"), "duck_rice.png"), chinese);
            MenuItemEntity wantonMee = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Wanton Mee", "Authentic HK taste", new BigDecimal("3.90"), "wanton_mee.png"), chinese);            
            MenuItemEntity beefhorfun = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Beef Hor Fun", "Best beef hor fun", new BigDecimal("7.90"), "beef_hor_fun.png"), chinese);

            MenuEntity menuEntity = menuEntityControllerLocal.createMenu(new MenuEntity("Menu 1", Boolean.TRUE), chinese);
            
            CategoryEntity categoryEntity = categoryEntityControllerLocal.createCategory(new CategoryEntity("Main"), menuEntity);
            categoryEntity.getMenuItemEntities().add(chickenRice);
            categoryEntity.getMenuItemEntities().add(duckRice);
            
//            BigDecimal totalAmt1 = checkoutControllerLocal.addItem(chickenRice, 1);
//            BigDecimal totalAmt2 = checkoutControllerLocal.addItem(duckRice, 1);
//            SaleTransactionEntity newSaleTransactionEntity = checkoutControllerLocal.doCheckout();
//            newSaleTransactionEntity.setCustomerEntity(customerEntity);
//            customerEntity.getSaleTransactionEntities().add(newSaleTransactionEntity);
            
            
            
            List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities = new ArrayList<>();
            Integer totalLineItem = 2;  // used to be 1
            Integer totalQuantity = 2;  // used to be 1
            Integer quantity = 1;
            BigDecimal totalAmount = new BigDecimal("0.00");
            
            ++totalLineItem;
            BigDecimal subTotal = chickenRice.getPrice().multiply(new BigDecimal(quantity));
            SaleTransactionLineItemEntity abc = new SaleTransactionLineItemEntity(1, quantity, chickenRice.getPrice(), subTotal, "");
            saleTransactionLineItemEntities.add(abc);
            SaleTransactionEntity newSaleTransactionEntity = saleTransactionEntityControllerLocal.createSaleTransaction(new SaleTransactionEntity(totalLineItem, totalQuantity, totalAmount, calendarStart, Boolean.FALSE, Boolean.FALSE));
            newSaleTransactionEntity.getSaleTransactionLineItemEntities().add(abc);
            abc.setSaleTransactionEntity(newSaleTransactionEntity);
            System.err.println("IRON MAN");
            
            for (SaleTransactionLineItemEntity xyz : newSaleTransactionEntity.getSaleTransactionLineItemEntities()) {
                VendorEntity vendor = xyz.getMenuItemEntity().getVendorEntity();
                OrderEntity oe = orderEntityControllerLocal.createOrder(new OrderEntity(calendarEnd, xyz.getSubTotal(), Boolean.FALSE));
                oe.getSaleTransactionLineItemEntities().add(xyz);
                xyz.setOrderEntity(oe);
                oe.setVendorEntity(vendor);
                vendor.getOrderEntities().add(oe);
                oe.setCustomerEntity(customerEntity);
                customerEntity.getOrderEntities().add(oe);
                System.err.println("WOO LA LA LA LA");
                
            }
            System.err.println("WAKANDA");
            newSaleTransactionEntity.setCustomerEntity(customerEntity);
            customerEntity.getSaleTransactionEntities().add(newSaleTransactionEntity);
            System.err.println("KABOOOOMM KABOOOOW");
            
            ReviewEntity review  = reviewEntityControllerLocal.createReview(new ReviewEntity("thissnkandksiasd ihfiafhiahis adjsiadafhifhn kbkfbssjda   hsiahi h aisdhaiodao ihao ihaoi sahoi ahshidhihiwiwdhwidhidhiiishiiihq    p oihoidhoah oihis dha", 1));
            ReviewEntity review2 = reviewEntityControllerLocal.createReview(new ReviewEntity("Rendang is very crispy, bagus", 5));
            System.err.println("SPIDERMAN");
            review.setCustomerEntity(customerEntity);
            customerEntity.getReviewEntities().add(review);
            review.setVendorEntity(chinese);
            chinese.getReviewEntities().add(review);
            review2.setCustomerEntity(customerEntity);
            customerEntity.getReviewEntities().add(review2);
            review2.setVendorEntity(chinese);
            chinese.getReviewEntities().add(review2);
            chinese.setRating(reviewEntityControllerLocal.averageReviewScore(chinese));
            System.err.println("FRIED CHICKENNNNNN");
            
        
        } catch (Exception ex) {
            System.err.println("********** DataInitializationSessionBean.initializeData(): An error has occurred while loading initial test data: " + ex.getMessage());
        }
    }
    
}
