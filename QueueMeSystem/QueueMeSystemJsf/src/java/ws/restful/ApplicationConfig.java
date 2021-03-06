/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author User
 */
@javax.ws.rs.ApplicationPath("Resources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(ws.restful.CheckoutResource.class);
        resources.add(ws.restful.CreditCardResource.class);
        resources.add(ws.restful.CustomerResource.class);
        resources.add(ws.restful.FoodCourtResource.class);
        resources.add(ws.restful.MenuResource.class);
        resources.add(ws.restful.OrderResource.class);
        resources.add(ws.restful.ReviewResource.class);
        resources.add(ws.restful.VendorResource.class);
    }
    
}
