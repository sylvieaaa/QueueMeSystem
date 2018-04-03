/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.FoodCourtEntityControllerLocal;
import entity.FoodCourtEntity;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.FoodCourtNotFoundException;

/**
 *
 * @author SYLVIA
 */
@Named(value = "updateFoodCourtManagedBean")
@ViewScoped
public class UpdateFoodCourtManagedBean implements Serializable {

    @EJB(name = "FoodCourtEntityControllerLocal")
    private FoodCourtEntityControllerLocal foodCourtEntityControllerLocal;

    private Long foodCourtIdToUpdate;
    private FoodCourtEntity foodCourtToUpdate;

    /**
     * Creates a new instance of UpdateFoodCourtManagedBean
     */
    public UpdateFoodCourtManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {

        foodCourtIdToUpdate = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("foodCourtIdToUpdate");

        try {
            foodCourtToUpdate = foodCourtEntityControllerLocal.retrieveFoodCourtById(foodCourtIdToUpdate);
        } catch (FoodCourtNotFoundException ex) {
            foodCourtToUpdate = new FoodCourtEntity();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the product details: " + ex.getMessage(), null));
        } catch (Exception ex) {
            foodCourtToUpdate = new FoodCourtEntity();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void cancel(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("foodCourtToUpdate", foodCourtToUpdate.getBusinessId());
        FacesContext.getCurrentInstance().getExternalContext().redirect("adminMainPage.xhtml");
    }

    public void updateFoodCourt(ActionEvent event) {
        try {
            foodCourtEntityControllerLocal.updateFoodCourt(foodCourtToUpdate);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Food Court updated successfully", null));
        } catch (FoodCourtNotFoundException ex) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating Food Court: " + ex.getMessage(), null));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + e.getMessage(), null));
        }
    }

    public FoodCourtEntity getFoodCourtToUpdate() {
        return foodCourtToUpdate;
    }

    public void setFoodCourtToUpdate(FoodCourtEntity foodCourtToUpdate) {
        this.foodCourtToUpdate = foodCourtToUpdate;
    }

    public void back(ActionEvent event) throws IOException {
        Long foodCourtIdToView = (Long)event.getComponent().getAttributes().get("foodCourtId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("foodCourtIdToView", foodCourtIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewFoodCourtDetails.xhtml");

    }

}
