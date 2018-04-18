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
import util.enumeration.VendorTypeEnum;
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
            adminEntityControllerLocal.createAdmin(new AdminEntity("Zhu Zhi", "Kerk", "kzhuzhi@gmail.com", "password"));
            adminEntityControllerLocal.createAdmin(new AdminEntity("Sylvia", "Swee", "sylvia@gmail.com", "password"));
            adminEntityControllerLocal.createAdmin(new AdminEntity("Rui Jia", "Low", "lruijia@gmail.com", "password"));

            CustomerEntity customerEntity = customerEntityControllerLocal.createCustomer(new CustomerEntity("Doe", "John", "98765432", "abc street", "yxsoong@gmail.com", "password"));

            Date calendarStart = new Date(0, 0, 0, 8, 0);
            Date calendarEnd = new Date(0, 0, 0, 22, 0);

            FoodCourtEntity vivoFC = foodCourtEntityControllerLocal.createFoodCourt(new FoodCourtEntity("Vivo City Food Republic", "A variety of Local Food!", "1 Harbourfront Walk", "098585", calendarStart, calendarEnd, "vivocityfoodrepublic@gmail.com", "password", "fc1.png"));
            FoodCourtEntity somersetFC = foodCourtEntityControllerLocal.createFoodCourt(new FoodCourtEntity("313 Food Republic", "Town's most affordable!", "313 Orchard Road", "238895", calendarStart, calendarEnd, "313foodrepublic@gmail.com", "password", "fc2.png"));
            //FoodCourtEntity nexFC = foodCourtEntityControllerLocal.createFoodCourt(new FoodCourtEntity("Nex Food Republic", "A variety of Local Food!", "23 Serangoon Central", "556083", calendarStart, calendarEnd, "nexfoodrepublic@gmail.com", "password", "fc3.png"));
//            foodCourtEntityControllerLocal.createFoodCourt(new FoodCourtEntity("KorKor Food Court", "Best in Changi", "Changi Drive 888", "555555", calendarStart, calendarEnd, "changifoodcourt", "password", "fc4.png"));
//            foodCourtEntityControllerLocal.createFoodCourt(new FoodCourtEntity("JieJie Food Court", "Best in Redhill", "Redhill Drive 555", "777654", calendarStart, calendarEnd, "redhillfoodcourt", "password", "fc5.png"));

            CreditCardEntity creditCard = creditCardEntityControllerLocal.createCreditCard("1234-5678-9999-0000", "John Doe", customerEntity);
//            customerEntity.getCreditCardEntities().add(creditCard);
//            creditCard.setCustomerEntity(customerEntity);
            // Initialize FoodCourt Entity
            VendorEntity chinese = vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Best Chicken and Duck Rice", VendorTypeEnum.CHINESE.toString(), 5, "Best Chicken and Duck rice in Vivocity!", calendarStart, calendarEnd, BigDecimal.ZERO, "tiantian@gmail.com", "password", "chinese.png"), vivoFC);
            VendorEntity chinese2 = vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Authentic Chicken and Duck Rice", VendorTypeEnum.CHINESE.toString(), 5, "Best Chicken and Duck rice in 313!", calendarStart, calendarEnd, BigDecimal.ZERO, "authenticchinese@gmail.com", "password", "chinese.png"), somersetFC);
            VendorEntity malay = vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Ahmad Satay", VendorTypeEnum.MALAY.toString(), 1, "Best satay in SG!", calendarStart, calendarEnd, BigDecimal.ZERO, "ahmadsatay@gmail.com", "password", "malay.png"), vivoFC);
            VendorEntity malay2 = vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Bagus Satay", VendorTypeEnum.MALAY.toString(), 1, "Best satay in Nex!", calendarStart, calendarEnd, BigDecimal.ZERO, "bagussatay@gmail.com", "password", "malay.png"), somersetFC);
            VendorEntity western = vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Uncle Charlie Western", VendorTypeEnum.WESTERN.toString(), 2, "Taste of USA!", calendarStart, calendarEnd, BigDecimal.ZERO, "unclecharlie@gmail.com", "password", "usa.png"), vivoFC);
            VendorEntity western2 = vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Western Delight", VendorTypeEnum.WESTERN.toString(), 2, "Taste of USA!", calendarStart, calendarEnd, BigDecimal.ZERO, "westerndelight@gmail.com", "password", "usa.png"), somersetFC);
            VendorEntity drinks = vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Drinks & Beverages", VendorTypeEnum.DRINKS.toString(), 3, "Thirsty no more!", calendarStart, calendarEnd, BigDecimal.ZERO, "vivodrinks@gmail.com", "password", "Seng.png"), vivoFC);
            VendorEntity drinks2 = vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Drinks Hub", VendorTypeEnum.DRINKS.toString(), 3, "Thirsty no more!", calendarStart, calendarEnd, BigDecimal.ZERO, "drinkshub@gmail.com", "password", "Seng.png"), somersetFC);
            VendorEntity fruitStore = vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Best Fruit Store", VendorTypeEnum.FRUITS.toString(), 4, "Eat me and be healthy", calendarEnd, calendarEnd, BigDecimal.ZERO, "vivofruit@gmail.com", "password", "fruit.png"), vivoFC);
            VendorEntity indian = vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Muthu Curry", VendorTypeEnum.INDIAN.toString(), 5, "Cheapest prata in SG!", calendarEnd, calendarEnd, BigDecimal.ZERO, "muthuprata@gmail.com", "password", "muthu.png"), vivoFC);

            MenuItemEntity chickenRice = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Chicken Rice", "Roasted & white", new BigDecimal("2.90"), "chicken_rice.png"), chinese);
            MenuItemEntity chickenRice2 = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Chicken Rice", "Roasted & white", new BigDecimal("2.90"), "chicken_rice.png"), chinese2);
            MenuItemEntity duckRice = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Roasted Duck Rice", "Authentic HK taste", new BigDecimal("2.90"), "duck_rice.png"), chinese);
            MenuItemEntity duckRice2 = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Roasted Duck Rice", "Authentic HK taste", new BigDecimal("2.90"), "duck_rice.png"), chinese2);
            MenuItemEntity wantonMee = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Wanton Mee", "Authentic HK taste", new BigDecimal("3.90"), "wanton_mee.png"), chinese);
            MenuItemEntity beefhorfun = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Beef Hor Fun", "Best beef hor fun", new BigDecimal("7.90"), "beef_hor_fun.png"), chinese);

            MenuItemEntity satay = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Chicken Satay", "Best satay", new BigDecimal("1.20"), "satay.png"), malay);
            MenuItemEntity satay2 = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Chicken Satay", "Best satay", new BigDecimal("1.20"), "satay.png"), malay2);

            MenuItemEntity chickenChop = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Chicken Chop", "Tasty", new BigDecimal("5.90"), "chicken_chop.png"), western);
            MenuItemEntity steak = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Steak", "Best steak!", new BigDecimal("8.90"), "steak.png"), western);
            MenuItemEntity chickenChop2 = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Chicken Chop", "Tasty", new BigDecimal("5.90"), "chicken_chop.png"), western2);
            MenuItemEntity steak2 = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Steak", "Best steak!", new BigDecimal("8.90"), "steak.png"), western2);
            MenuItemEntity fries = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("French Fries", "Crispy fries!", new BigDecimal("2.50"), "french_fries.png"), western);

            MenuItemEntity milo = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Milo", "Ice milo", new BigDecimal("1.00"), "iced_milo.png"), drinks);
            MenuItemEntity milo2 = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Milo", "Ice milo", new BigDecimal("1.00"), "iced_milo.png"), drinks2);

            MenuItemEntity prata = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Roti Prata", "Crispy Prata", new BigDecimal("0.90"), "prata.png"), indian);
            MenuItemEntity curry = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Chicken Curry", "Best chicken curry!", new BigDecimal("1.50"), "curry_chicken.png"), indian);

            MenuItemEntity papaya = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Papaya", "Papaya fruit", new BigDecimal("1.00"), "papaya.png"), fruitStore);
            MenuItemEntity watermelon = menuItemEntityControllerLocal.createMenuItem(new MenuItemEntity("Watermelon", "Watermelon fruit", new BigDecimal("1.00"), "watermelon.png"), fruitStore);

            MenuEntity menuEntity = menuEntityControllerLocal.createMenu(new MenuEntity("Menu 1", Boolean.TRUE), chinese);
            MenuEntity menuEntity2 = menuEntityControllerLocal.createMenu(new MenuEntity("Menu 2", Boolean.TRUE), malay);
            MenuEntity menuEntity3 = menuEntityControllerLocal.createMenu(new MenuEntity("Menu 3", Boolean.TRUE), drinks);
            MenuEntity menuEntity4 = menuEntityControllerLocal.createMenu(new MenuEntity("Menu 4", Boolean.TRUE), indian);
            MenuEntity menuEntity5 = menuEntityControllerLocal.createMenu(new MenuEntity("Menu 5", Boolean.TRUE), western);
            MenuEntity menuEntity6 = menuEntityControllerLocal.createMenu(new MenuEntity("Menu 6", Boolean.TRUE), fruitStore);
            MenuEntity menuEntity7 = menuEntityControllerLocal.createMenu(new MenuEntity("Menu 7", Boolean.TRUE), chinese2);
            MenuEntity menuEntity8 = menuEntityControllerLocal.createMenu(new MenuEntity("Menu 8", Boolean.TRUE), malay2);
            MenuEntity menuEntity9 = menuEntityControllerLocal.createMenu(new MenuEntity("Menu 9", Boolean.TRUE), western2);
            MenuEntity menuEntity10 = menuEntityControllerLocal.createMenu(new MenuEntity("Menu 10", Boolean.TRUE), drinks2);

            CategoryEntity categoryEntity = categoryEntityControllerLocal.createCategory(new CategoryEntity("Main"), menuEntity);
            CategoryEntity categoryEntity2 = categoryEntityControllerLocal.createCategory(new CategoryEntity("Main"), menuEntity2);
            CategoryEntity categoryEntity3 = categoryEntityControllerLocal.createCategory(new CategoryEntity("Drinks"), menuEntity3);
            CategoryEntity categoryEntity4 = categoryEntityControllerLocal.createCategory(new CategoryEntity("Main"), menuEntity4);
            CategoryEntity categoryEntity5 = categoryEntityControllerLocal.createCategory(new CategoryEntity("Main"), menuEntity5);
            CategoryEntity categoryEntity6 = categoryEntityControllerLocal.createCategory(new CategoryEntity("Main"), menuEntity6);
            CategoryEntity categoryEntity7 = categoryEntityControllerLocal.createCategory(new CategoryEntity("Main"), menuEntity7);
            CategoryEntity categoryEntity8 = categoryEntityControllerLocal.createCategory(new CategoryEntity("Main"), menuEntity8);
            CategoryEntity categoryEntity9 = categoryEntityControllerLocal.createCategory(new CategoryEntity("Main"), menuEntity9);
            CategoryEntity categoryEntity10 = categoryEntityControllerLocal.createCategory(new CategoryEntity("Main"), menuEntity10);

            categoryEntity.getMenuItemEntities().add(chickenRice);
            categoryEntity.getMenuItemEntities().add(duckRice);
            categoryEntity2.getMenuItemEntities().add(satay);
            categoryEntity3.getMenuItemEntities().add(milo);
            categoryEntity4.getMenuItemEntities().add(prata);
            categoryEntity4.getMenuItemEntities().add(curry);
            categoryEntity5.getMenuItemEntities().add(chickenChop);
            categoryEntity5.getMenuItemEntities().add(steak);
            categoryEntity5.getMenuItemEntities().add(fries);
            categoryEntity6.getMenuItemEntities().add(papaya);
            categoryEntity6.getMenuItemEntities().add(watermelon);
            categoryEntity7.getMenuItemEntities().add(wantonMee);
            categoryEntity7.getMenuItemEntities().add(beefhorfun);
            categoryEntity8.getMenuItemEntities().add(satay2);
            categoryEntity9.getMenuItemEntities().add(chickenChop2);
            categoryEntity9.getMenuItemEntities().add(steak);
            categoryEntity10.getMenuItemEntities().add(milo2);

            ReviewEntity review = reviewEntityControllerLocal.createReview(new ReviewEntity("Food does not taste good.", 1));
            ReviewEntity review2 = reviewEntityControllerLocal.createReview(new ReviewEntity("Food and service is top notch!", 5));
            ReviewEntity review3 = reviewEntityControllerLocal.createReview(new ReviewEntity("Food is great!", 4));
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
            System.err.println("****** DataInitializationSessionBean.initializeData(): An error has occurred while loading initial test data: " + ex.getMessage());
        }
    }

}
