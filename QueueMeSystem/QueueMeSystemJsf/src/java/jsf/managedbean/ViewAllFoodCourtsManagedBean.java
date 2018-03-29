/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.FoodCourtEntityControllerLocal;
import entity.FoodCourtEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;

/**
 *
 * @author SYLVIA
 */
@Named(value = "viewAllFoodCourtsManagedBean")
@Dependent
public class ViewAllFoodCourtsManagedBean implements Serializable {

    @EJB(name = "FoodCourtEntityControllerLocal")
    private FoodCourtEntityControllerLocal foodCourtEntityControllerLocal;

    private List<FoodCourtEntity> foodCourts;

    /**
     * Creates a new instance of ViewAllFoodCourtsManagedBean
     */
    public ViewAllFoodCourtsManagedBean() {
        foodCourts = new ArrayList<>();
    }

    @PostConstruct
    public void postConstruct() {
        setFoodCourts(foodCourtEntityControllerLocal.retrieveAllFoodCourts());
     
    }
    
    


    public List<FoodCourtEntity> getFoodCourts() {
        return foodCourts;
    }

    public void setFoodCourts(List<FoodCourtEntity> foodCourts) {
        this.foodCourts = foodCourts;
    }

}
