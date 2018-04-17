/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AdminEntityControllerLocal;
import ejb.session.stateless.CategoryEntityControllerLocal;
import ejb.session.stateless.CreditCardEntityControllerLocal;
import ejb.session.stateless.CustomerEntityControllerLocal;
import ejb.session.stateless.FoodCourtEntityControllerLocal;
import ejb.session.stateless.MenuEntityControllerLocal;
import ejb.session.stateless.MenuItemEntityControllerLocal;
import ejb.session.stateless.OrderEntityControllerLocal;
import ejb.session.stateless.ReviewEntityControllerLocal;
import ejb.session.stateless.SaleTransactionEntityControllerLocal;
import ejb.session.stateless.TagEntityControllerLocal;
import ejb.session.stateless.VendorEntityControllerLocal;
import entity.AdminEntity;
import entity.CategoryEntity;
import entity.CreditCardEntity;
import entity.CustomerEntity;
import entity.FoodCourtEntity;
import entity.MenuEntity;
import entity.MenuItemEntity;
import entity.ReviewEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
import entity.TagEntity;
import entity.VendorEntity;
import java.io.File;
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
import util.exception.TagAlreadyExistException;

@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {

    @EJB
    private CreditCardEntityControllerLocal creditCardEntityControllerLocal;

    @EJB
    private TagEntityControllerLocal tagEntityControllerLocal;

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
            String newFilePath = System.getProperty("user.dir").replaceAll("config", "docroot").replaceFirst("docroot", "config") + System.getProperty("file.separator") + "queueme-uploads";
            System.err.println(newFilePath);
        } catch (AdminNotFoundException ex) {
            initializeDate();
        }
    }

    private void initializeDate() {
        try {
            //create folders
            String filePath = System.getProperty("user.dir").replaceAll("config", "docroot").replaceFirst("docroot", "config") + System.getProperty("file.separator") + "queueme-uploads" + System.getProperty("file.separator");
            String[] directory = {"foodPhotos", "vendorLogos", "foodCourtLogos"};
            for (int i = 0; i < directory.length; i++) {
                new File(filePath + directory[i]).mkdir();
            }

            // Initialize admin entities
            adminEntityControllerLocal.createAdmin(new AdminEntity("Yi Xuan", "Soong", "yxsoong", "password"));
            adminEntityControllerLocal.createAdmin(new AdminEntity("Zhu Zhi", "Kerk", "kzhuzhi", "password"));
            adminEntityControllerLocal.createAdmin(new AdminEntity("Sylvia", "Swee", "sylvia", "password"));
            adminEntityControllerLocal.createAdmin(new AdminEntity("Rui Jia", "Low", "lruijia", "password"));

            CustomerEntity customerEntity = customerEntityControllerLocal.createCustomer(new CustomerEntity("customer", "first", "98765432", "abc street", "customer@gmail.com", "password"));

            Date calendarStart = new Date(0, 0, 0, 8, 0);
            Date calendarEnd = new Date(0, 0, 0, 22, 0);

            FoodCourtEntity foodCourtEntity = foodCourtEntityControllerLocal.createFoodCourt(new FoodCourtEntity("ABC Food Court", "BEST in SG", "ABC Road", "123123", calendarStart, calendarEnd, "ABCFoodCourt", "password", "fc1.png"));
            foodCourtEntityControllerLocal.createFoodCourt(new FoodCourtEntity("Royal Food Court", "Best in Kent Ridge", "Kent Ridge Drive 123", "117417", calendarStart, calendarEnd, "royalfoodcourt", "password", "fc2.png"));
            foodCourtEntityControllerLocal.createFoodCourt(new FoodCourtEntity("Grandfather's Food Court", "Best in Ang Mo Kio", "AMK Drive 666", "143245", calendarStart, calendarEnd, "amkfoodcourt", "password", "fc3.png"));
            foodCourtEntityControllerLocal.createFoodCourt(new FoodCourtEntity("KorKor Food Court", "Best in Changi", "Changi Drive 888", "555555", calendarStart, calendarEnd, "changifoodcourt", "password", "fc4.png"));
            foodCourtEntityControllerLocal.createFoodCourt(new FoodCourtEntity("JieJie Food Court", "Best in Redhill", "Redhill Drive 555", "777654", calendarStart, calendarEnd, "redhillfoodcourt", "password", "fc5.png"));


            CreditCardEntity creditCard = creditCardEntityControllerLocal.createCreditCard("1234-5678-9999-0000", "customer", customerEntity);
//            customerEntity.getCreditCardEntities().add(creditCard);
//            creditCard.setCustomerEntity(customerEntity);

            // Initialize FoodCourt Entity
            VendorEntity chinese = vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Singapore Chinese Food", "Chinese", 0, "Best Chicken rice in KR!", calendarStart, calendarEnd, BigDecimal.ZERO, "chinese", "password", "chinese.png"), foodCourtEntity);
            VendorEntity malay = vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Minah's Malay Food", "Halal", 1, "Best Halal store in SG!", calendarStart, calendarEnd, BigDecimal.ZERO, "malay", "password", "malay.png"), foodCourtEntity);
            
            vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Uncle Charlie's Western", "Western", 2, "Taste of USA in KR!", calendarStart, calendarEnd, BigDecimal.ZERO, "western", "password", "usa.png"), foodCourtEntity);
            VendorEntity drinks = vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Ah Seng Drink Stores", "Beverages", 3, "Thirsty no more!", calendarStart, calendarEnd, BigDecimal.ZERO, "drink", "password", "Seng.png"), foodCourtEntity);
            vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Best Fruit Store", "Fruits", 4, "Eat me and be healthy", calendarEnd, calendarEnd, BigDecimal.ZERO, "fruit", "password", "fruit.png"), foodCourtEntity);
            vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Muthu Curry", "Indian", 5, "Cheapest prata in SG!", calendarEnd, calendarEnd, BigDecimal.ZERO, "indian", "password", "muthu.png"), foodCourtEntity);

            MenuItemEntity chickenRice = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Chicken Rice", "Roasted or white", new BigDecimal("2.90"), "chicken_rice.png"), chinese);
            MenuItemEntity duckRice = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Roasted Duck Rice", "Authentic HK taste", new BigDecimal("2.90"), "duck_rice.png"), chinese);
            MenuItemEntity wantonMee = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Wanton Mee", "Authentic HK taste", new BigDecimal("3.90"), "wanton_mee.png"), chinese);
            MenuItemEntity beefhorfun = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Beef Hor Fun", "Best beef hor fun", new BigDecimal("7.90"), "beef_hor_fun.png"), chinese);
            MenuItemEntity satay = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Satay", "best satay", new BigDecimal("1.20"),"satay.png"), malay);
            MenuItemEntity milo = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("milo", "ice milo", new BigDecimal("1.00"), "milo.png"), drinks);
            
            MenuEntity menuEntity = menuEntityControllerLocal.createMenu(new MenuEntity("Menu 1", Boolean.TRUE), chinese);
            MenuEntity menuEntity2 = menuEntityControllerLocal.createMenu(new MenuEntity("Menu 2", Boolean.TRUE), malay);
            MenuEntity menuEntity3 = menuEntityControllerLocal.createMenu(new MenuEntity("Menu 3", Boolean.TRUE), drinks);
            

            CategoryEntity categoryEntity = categoryEntityControllerLocal.createCategory(new CategoryEntity("Main"), menuEntity);
            CategoryEntity categoryEntity2 = categoryEntityControllerLocal.createCategory(new CategoryEntity("Main"), menuEntity2);
            CategoryEntity categoryEntity3 = categoryEntityControllerLocal.createCategory(new CategoryEntity("Drinks"), menuEntity3);
            categoryEntity.getMenuItemEntities().add(chickenRice);
            categoryEntity.getMenuItemEntities().add(duckRice);
            categoryEntity2.getMenuItemEntities().add(satay);
            categoryEntity3.getMenuItemEntities().add(milo);
            

            ReviewEntity review = reviewEntityControllerLocal.createReview(new ReviewEntity("thissnkandksiasd ihfiafhiahis adjsiadafhifhn kbkfbssjda   hsiahi h aisdhaiodao ihao ihaoi sahoi ahshidhihiwiwdhwidhidhiiishiiihq    p oihoidhoah oihis dha", 1));
            ReviewEntity review2 = reviewEntityControllerLocal.createReview(new ReviewEntity("Rendang is very crispy, bagus", 5));
            ReviewEntity review3 = reviewEntityControllerLocal.createReview(new ReviewEntity("very good", 4));
            review.setCustomerEntity(customerEntity);
            customerEntity.getReviewEntities().add(review);
            review.setVendorEntity(chinese);
            chinese.getReviewEntities().add(review);
            review2.setCustomerEntity(customerEntity);
            customerEntity.getReviewEntities().add(review2);
            review2.setVendorEntity(chinese);
            chinese.getReviewEntities().add(review2);
            chinese.setRating(reviewEntityControllerLocal.averageReviewScore(chinese));
            review3.setCustomerEntity(customerEntity);
            customerEntity.getReviewEntities().add(review3);
            review.setVendorEntity(malay);
            malay.getReviewEntities().add(review3);
            malay.setRating(reviewEntityControllerLocal.averageReviewScore(malay));
            
            List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities = new ArrayList<>();
            Integer totalLineItem = 0;
            Integer totalQuantity = 0;
            Integer totalQuantity2 = 0;
            Integer totalQuantity3 = 0;
            BigDecimal totalAmount = BigDecimal.ZERO;
            Integer quantity = 0;
            Boolean isTakeaway = null;
            String paymentType = null;
            Integer menuItemQuantity = 1;
            String specialRequest = "";
            
            BigDecimal subTotal = chickenRice.getPrice().multiply(new BigDecimal(menuItemQuantity));
            ++totalLineItem;
            saleTransactionLineItemEntities.add(new SaleTransactionLineItemEntity(totalLineItem, menuItemQuantity, chickenRice.getPrice(), subTotal, specialRequest, chickenRice));
            totalQuantity += menuItemQuantity;
            totalAmount = totalAmount.add(subTotal);
            
            BigDecimal subTotal2 = satay.getPrice().multiply(new BigDecimal(menuItemQuantity));
            ++totalLineItem;
            saleTransactionLineItemEntities.add(new SaleTransactionLineItemEntity(totalLineItem, menuItemQuantity, satay.getPrice(), subTotal2, specialRequest, satay));
            totalQuantity += menuItemQuantity;
            totalAmount = totalAmount.add(subTotal2);
            
            BigDecimal subTotal3 = milo.getPrice().multiply(new BigDecimal(menuItemQuantity));
            ++totalLineItem;
            saleTransactionLineItemEntities.add(new SaleTransactionLineItemEntity(totalLineItem, menuItemQuantity, milo.getPrice(), subTotal3, specialRequest, milo));
            totalQuantity += menuItemQuantity;
            totalAmount = totalAmount.add(subTotal3);
            
            BigDecimal subTotal4 = duckRice.getPrice().multiply(new BigDecimal(menuItemQuantity));
            ++totalLineItem;
            saleTransactionLineItemEntities.add(new SaleTransactionLineItemEntity(totalLineItem, menuItemQuantity, duckRice.getPrice(), subTotal4, specialRequest, duckRice));
            totalQuantity += menuItemQuantity;
            totalAmount = totalAmount.add(subTotal4);
            
 
            SaleTransactionEntity saleTransactionEntity = saleTransactionEntityControllerLocal.createSaleTransaction(new SaleTransactionEntity(totalLineItem, totalQuantity, totalAmount, Calendar.getInstance().getTime(), Boolean.FALSE, isTakeaway, null, saleTransactionLineItemEntities));
            saleTransactionEntity.setCustomerEntity(customerEntity);
            customerEntity.getSaleTransactionEntities().add(saleTransactionEntity);

            String[] tags = {"Rice", "Noodles", "Curry", "Chicken", "Fish", "Pasta", "Meat", "Soup", "Salad", "Dim Sum", "Spicy", "Sweets", "Curry", "Finger Foods", "Fruits"};
            for (String tag : tags) {
                try {
                    tagEntityControllerLocal.createTagEntity(new TagEntity(tag));
                } catch (TagAlreadyExistException ex) {

                }
            }

        } catch (Exception ex) {
            System.err.println("********** DataInitializationSessionBean.initializeData(): An error has occurred while loading initial test data: " + ex.getMessage());
        }
    }

}
