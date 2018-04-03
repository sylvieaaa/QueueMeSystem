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
@Named(value = "viewFoodCourtDetailsManagedBean")
@ViewScoped
public class ViewFoodCourtDetailsManagedBean implements Serializable {

    @EJB(name = "FoodCourtEntityControllerLocal")
    private FoodCourtEntityControllerLocal foodCourtEntityControllerLocal;

    private Long foodCourtIdToView;
    private FoodCourtEntity foodCourtToView;

    /**
     * Creates a new instance of ViewFoodCourtDetailsManagedBean
     */
    public ViewFoodCourtDetailsManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        foodCourtIdToView = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("foodCourtIdToView");

        try {
            foodCourtToView = foodCourtEntityControllerLocal.retrieveFoodCourtById(foodCourtIdToView);
        } catch (FoodCourtNotFoundException ex) {
            foodCourtToView = new FoodCourtEntity();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the product details: " + ex.getMessage(), null));
        } catch (Exception ex) {
            foodCourtToView = new FoodCourtEntity();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void update(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("foodCourtIdToUpdate", foodCourtToView.getBusinessId());
        FacesContext.getCurrentInstance().getExternalContext().redirect("updateFoodCourt.xhtml");
    }

    public void viewVendor(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("vendorsToView", foodCourtToView.getBusinessId());
        FacesContext.getCurrentInstance().getExternalContext().redirect("foodCourtMainPage.xhtml");

    }

    public FoodCourtEntity getFoodCourtToView() {
        return foodCourtToView;
    }

    public void setFoodCourtToView(FoodCourtEntity foodCourtToView) {
        this.foodCourtToView = foodCourtToView;
    }

}
