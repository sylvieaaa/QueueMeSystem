/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.FoodCourtEntityControllerLocal;
import entity.FoodCourtEntity;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author SYLVIA
 */
@Named(value = "createNewFoodCourtManagedBean")
@RequestScoped
public class CreateNewFoodCourtManagedBean {

    @EJB(name = "FoodCourtEntityControllerLocal")
    private FoodCourtEntityControllerLocal foodCourtEntityControllerLocal;

    private FoodCourtEntity newFoodCourt;
    /**
     * Creates a new instance of CreateNewFoodCourtManagedBean
     */
    
    public CreateNewFoodCourtManagedBean() {
        newFoodCourt = new FoodCourtEntity();
    }

    public FoodCourtEntity getNewFoodCourt() {
        return newFoodCourt;
    }

    public void setNewFoodCourt(FoodCourtEntity newFoodCourt) {
        this.newFoodCourt = newFoodCourt;
    }
    
        public void createNewFoodCourt()
    {
       
        FoodCourtEntity fc = foodCourtEntityControllerLocal.createFoodCourt(newFoodCourt);
        newFoodCourt = new FoodCourtEntity();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Food Court created successfully (Postal Code: " + fc.getPostalCode() + ")", null));
    }
    
}
