/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AdminEntityControllerLocal;
import ejb.session.stateless.FoodCourtEntityControllerLocal;
import ejb.session.stateless.VendorEntityControllerLocal;
import entity.AdminEntity;
import entity.FoodCourtEntity;
import entity.VendorEntity;
import java.math.BigDecimal;
import java.util.Calendar;
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
           
            //Initialize Vendor Entity
            vendorEntityControllerLocal.createVendorEntity(new VendorEntity("No Drunk, No Go Home Drink Stall", "Drinks", new BigDecimal("4.50"), "From coffee to coffin, we sell anything in between! (P.S coffin i.e. cigarettes)", calendarStart, calendarEnd, BigDecimal.ZERO));
            vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Wo Ai Wanton Mee", "Noodles", new BigDecimal("3.50"), "Only sell wanton mee", calendarStart, calendarEnd, BigDecimal.ZERO));
            vendorEntityControllerLocal.createVendorEntity(new VendorEntity("Bok Bok Chicken Rice", "Rice", new BigDecimal("4.00"), "Only chicken in Singapore with bird flu!", calendarStart, calendarEnd, BigDecimal.ZERO));
            vendorEntityControllerLocal.createVendorEntity(new VendorEntity("BAAAAK Kut Teh", "Rice", new BigDecimal("2.00"), "ONLY HALAL PORK IN THE WHOLE WORLD", calendarStart, calendarEnd, BigDecimal.ZERO));
        } catch (Exception ex) {
            System.err.println("********** DataInitializationSessionBean.initializeData(): An error has occurred while loading initial test data: " + ex.getMessage());
        }
    }

}
