/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AdminEntityControllerLocal;
import ejb.session.stateless.FoodCourtEntityControllerLocal;
import ejb.session.stateless.MenuItemEntityControllerLocal;
import ejb.session.stateless.VendorEntityControllerLocal;
import ejb.session.stateless.VendorStaffEntityControllerLocal;
import entity.AdminEntity;
import entity.FoodCourtEntity;
import entity.MenuItemEntity;
import entity.VendorEntity;
import entity.VendorStaffEntity;
import java.math.BigDecimal;
import java.util.Calendar;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.AdminNotFoundException;


@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {  

    @EJB(name = "VendorStaffEntityControllerLocal")
    private VendorStaffEntityControllerLocal vendorStaffEntityControllerLocal;

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
          
            foodCourtEntityControllerLocal.createFoodCourt(new FoodCourtEntity("ABC Food Court", "BEST in SG", "ABC Road", "123123", new BigDecimal("4.00"), calendarStart, calendarEnd, "ABCFoodCourt", "password"));
            foodCourtEntityControllerLocal.createFoodCourt(new FoodCourtEntity("Royal Food Court", "Best in Kent Ridge", "Kent Ridge Drive 123", "117417", new BigDecimal("5.00"), calendarStart, calendarEnd, "royalfoodcourt", "password"));
            
            vendorStaffEntityControllerLocal.createVendorStaff(new VendorStaffEntity("John", "Doe", EmployeeAccessRightEnum.CASHIER, "johndoe", "password"));

            
            vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Singapore Chinese Food", "Chinese", new BigDecimal("4.70"), "Best Chicken rice in KR!", calendarStart, calendarEnd, BigDecimal.ZERO));
            vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Minah's Malay Food", "Halal", new BigDecimal("2.50"), "Best Halal store in SG!", calendarStart, calendarEnd, BigDecimal.ZERO));
            vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Uncle Charlie's Western", "Western", new BigDecimal("4.80"), "Taste of USA in KR!", calendarStart, calendarEnd, BigDecimal.ZERO));
            vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Ah Seng Drink Stores", "Beverages", new BigDecimal("4.90"), "Thirsty no more!", calendarStart, calendarEnd, BigDecimal.ZERO));
            vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Best Fruit Store", "Fruits", new BigDecimal("4.89"), "Eat me and be healthy", calendarEnd, calendarEnd, BigDecimal.ZERO));
            vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Muthu Curry", "Indian", new BigDecimal("1.89"), "Cheapest prata in SG!", calendarEnd, calendarEnd, BigDecimal.ZERO));
            
            
            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Roti Prata", "Indian Pan Cake", new BigDecimal("0.90"), "www.pratapic.com"));
            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Fish n Chips", "Authentic Fish n Chips", new BigDecimal("5.90"), "www.fishnchippic.com"));
            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Chicken Chop", "With pasta on the side", new BigDecimal("5.90"), "www.chickenchoppic.com"));
            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Bologniese", "Authtentic Italy pasta", new BigDecimal("4.90"), "www.bologoniesepic.com"));
            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Chicken Rice", "Roasted or white", new BigDecimal("2.90"), "www.chickricepic.com"));
            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Roasted Duck Rice", "Authentic HK taste", new BigDecimal("2.90"), "www.duckricepic.com"));
            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Wanton Mee", "Authentic HK taste", new BigDecimal("3.90"), "www.1tonmeepic.com"));
            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Kopi O", "No sugar", new BigDecimal("0.90"), "www.kopiopic.com"));
            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Bandung", "Pink and sweet", new BigDecimal("1.40"), "www.bandungpic.com"));
            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Ice Lemon Tea", "Real lemon", new BigDecimal("1.40"), "www.lemonteapic.com"));
            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Nasi Lemak", "Power nasi lemak", new BigDecimal("3.90"), "www.nlpic.com"));
            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Nasi Briyani", "Bagus rice", new BigDecimal("3.90"), "www.nbpic.com"));
            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Honeydew", "one slice", new BigDecimal("0.50"), "www.honeydewpic.com"));
            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Papaya", "one slice", new BigDecimal("0.50"), "www.papayapic.com"));
            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Watermelon", "one slice", new BigDecimal("0.50"), "www.wmpic.com"));
            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Claypot Tofu", "Sizzling hot", new BigDecimal("7.90"), "www.cptofupic.com"));
            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Fried Rice", "Best in town", new BigDecimal("7.90"), "www.flyricepic.com"));
            menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Beef Hor Fun", "Best beef hor fun", new BigDecimal("7.90"), "www.beefhorfun.com"));
        
        } catch (Exception ex) {
            System.err.println("********** DataInitializationSessionBean.initializeData(): An error has occurred while loading initial test data: " + ex.getMessage());
        }
    }

}
