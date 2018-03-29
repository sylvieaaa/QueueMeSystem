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
import ejb.session.stateless.SaleTransactionLineItemEntityControllerLocal;
import ejb.session.stateless.VendorEntityControllerLocal;
import entity.AdminEntity;
import entity.CategoryEntity;
import entity.CustomerEntity;
import entity.FoodCourtEntity;
import entity.MenuEntity;
import entity.MenuItemEntity;
import entity.OrderEntity;
import entity.SaleTransactionLineItemEntity;
import entity.VendorEntity;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    private SaleTransactionLineItemEntityControllerLocal saleTransactionLineItemEntityControllerLocal;

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

            // Initialize FoodCourt Entity
            Calendar calendarStart = Calendar.getInstance();
            Calendar calendarEnd = Calendar.getInstance();
            calendarStart.set(Calendar.HOUR_OF_DAY, 8);
            calendarStart.set(Calendar.MINUTE, 0);
            calendarStart.set(Calendar.SECOND, 0);
            calendarEnd.set(Calendar.HOUR_OF_DAY, 22);
            calendarEnd.set(Calendar.MINUTE, 0);
            calendarEnd.set(Calendar.SECOND, 0);
            
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            Date date = new Date();
            
            
            FoodCourtEntity foodCourtEntity = foodCourtEntityControllerLocal.createFoodCourt(new FoodCourtEntity("ABC Food Court", "BEST in SG", "ABC Road", "123123", new BigDecimal("4.00"), date, date, "ABCFoodCourt", "password"));
            foodCourtEntityControllerLocal.createFoodCourt(new FoodCourtEntity("Royal Food Court", "Best in Kent Ridge", "Kent Ridge Drive 123", "117417", new BigDecimal("5.00"), date, date, "royalfoodcourt", "password"));
            foodCourtEntityControllerLocal.createFoodCourt(new FoodCourtEntity("Grandfather's Food Court", "Best in Ang Mo Kio", "AMK Drive 666", "143245", new BigDecimal("5.00"), date, date, "amkfoodcourt", "password"));
            foodCourtEntityControllerLocal.createFoodCourt(new FoodCourtEntity("KorKor Food Court", "Best in Changi", "Changi Drive 888", "555555", new BigDecimal("5.00"), date, date, "changifoodcourt", "password"));
            foodCourtEntityControllerLocal.createFoodCourt(new FoodCourtEntity("JieJie Food Court", "Best in Redhill", "Redhill Drive 555", "777654", new BigDecimal("5.00"), date, date, "redhillfoodcourt", "password"));

            VendorEntity chinese = vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Singapore Chinese Food", "Chinese", new BigDecimal("4.70"), "Best Chicken rice in KR!", calendarStart, calendarEnd, BigDecimal.ZERO, "chinese", "password"), foodCourtEntity);
            vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Minah's Malay Food", "Halal", new BigDecimal("2.50"), "Best Halal store in SG!", calendarStart, calendarEnd, BigDecimal.ZERO, "malay", "password"), foodCourtEntity);
            vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Uncle Charlie's Western", "Western", new BigDecimal("4.80"), "Taste of USA in KR!", calendarStart, calendarEnd, BigDecimal.ZERO, "western", "password"), foodCourtEntity);
            vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Ah Seng Drink Stores", "Beverages", new BigDecimal("4.90"), "Thirsty no more!", calendarStart, calendarEnd, BigDecimal.ZERO, "drink", "password"), foodCourtEntity);
            vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Best Fruit Store", "Fruits", new BigDecimal("4.89"), "Eat me and be healthy", calendarEnd, calendarEnd, BigDecimal.ZERO, "fruit", "password"), foodCourtEntity);
            vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Muthu Curry", "Indian", new BigDecimal("1.89"), "Cheapest prata in SG!", calendarEnd, calendarEnd, BigDecimal.ZERO, "indian", "password"), foodCourtEntity);
            
            //chinese.getMenuEntity().getCategoryEntities().add(new CategoryEntity("Main"));
//            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Roti Prata", "Indian Pan Cake", new BigDecimal("0.90"), "www.pratapic.com"));
//            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Fish n Chips", "Authentic Fish n Chips", new BigDecimal("5.90"), "www.fishnchippic.com"));
//            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Chicken Chop", "With pasta on the side", new BigDecimal("5.90"), "www.chickenchoppic.com"));
            MenuItemEntity chickenRice = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Chicken Rice", "Roasted or white", new BigDecimal("2.90"), "chicken_rice.png"), chinese);
            MenuItemEntity duckRice = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Roasted Duck Rice", "Authentic HK taste", new BigDecimal("2.90"), "duck_rice.png"), chinese);
            MenuItemEntity wantonMee = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Wanton Mee", "Authentic HK taste", new BigDecimal("3.90"), "wanton_mee.png"), chinese);
//            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Kopi O", "No sugar", new BigDecimal("0.90"), "www.kopiopic.com"));
//            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Bandung", "Pink and sweet", new BigDecimal("1.40"), "www.bandungpic.com"));
//            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Ice Lemon Tea", "Real lemon", new BigDecimal("1.40"), "www.lemonteapic.com"));
//            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Nasi Lemak", "Power nasi lemak", new BigDecimal("3.90"), "www.nlpic.com"));
//            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Nasi Briyani", "Bagus rice", new BigDecimal("3.90"), "www.nbpic.com"));
//            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Honeydew", "one slice", new BigDecimal("0.50"), "www.honeydewpic.com"));
//            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Papaya", "one slice", new BigDecimal("0.50"), "www.papayapic.com"));
//            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Watermelon", "one slice", new BigDecimal("0.50"), "www.wmpic.com"));
//            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Claypot Tofu", "Sizzling hot", new BigDecimal("7.90"), "www.cptofupic.com"));
//            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Fried Rice", "Best in town", new BigDecimal("7.90"), "www.flyricepic.com"));
            MenuItemEntity beefhorfun = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Beef Hor Fun", "Best beef hor fun", new BigDecimal("7.90"), "beef_hor_fun.png"), chinese);

            MenuEntity menuEntity = menuEntityControllerLocal.createMenu(new MenuEntity("Menu 1", Boolean.TRUE), chinese);
            CategoryEntity categoryEntity = categoryEntityControllerLocal.createCategory(new CategoryEntity("Main"), menuEntity);
            categoryEntity.getMenuItemEntities().add(chickenRice);
            categoryEntity.getMenuItemEntities().add(duckRice);

            OrderEntity orderEntity = orderEntityControllerLocal.createOrder(new OrderEntity(calendarStart));
            SaleTransactionLineItemEntity lineItem1 = saleTransactionLineItemEntityControllerLocal.createSaleTransactionLineItemEntity(new SaleTransactionLineItemEntity(1, 2, new BigDecimal("7.90"), new BigDecimal("16.80"), false, 0));
            SaleTransactionLineItemEntity lineItem2 = saleTransactionLineItemEntityControllerLocal.createSaleTransactionLineItemEntity(new SaleTransactionLineItemEntity(1, 2, new BigDecimal("5.00"), new BigDecimal("10"), false, 0));
            lineItem1.setMenuItemEntity(chickenRice);
            lineItem2.setMenuItemEntity(duckRice);
            orderEntity.getSaleTransactionLineItemEntities().add(lineItem1);
            orderEntity.getSaleTransactionLineItemEntities().add(lineItem2);
            lineItem1.setOrderEntity(orderEntity);
            lineItem2.setOrderEntity(orderEntity);
            orderEntity.setVendorEntity(chinese);

            customerEntityControllerLocal.createCustomer(new CustomerEntity("customer", "first", "98765432", "abc street", "customer@gmail.com", "password"));
        } catch (Exception ex) {
            System.err.println("********** DataInitializationSessionBean.initializeData(): An error has occurred while loading initial test data: " + ex.getMessage());
        }
    }

}
