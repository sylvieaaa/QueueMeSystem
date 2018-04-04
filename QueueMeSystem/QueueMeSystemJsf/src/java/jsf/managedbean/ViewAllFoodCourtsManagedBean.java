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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@Named(value = "viewAllFoodCourtsManagedBean")
@ViewScoped
public class ViewAllFoodCourtsManagedBean implements Serializable {

    @EJB(name = "FoodCourtEntityControllerLocal")
    private FoodCourtEntityControllerLocal foodCourtEntityControllerLocal;

    private List<FoodCourtEntity> foodCourts;
    private List<FoodCourtEntity> filteredFoodCourts;
    private FoodCourtEntity foodCourt;
    private FoodCourtEntity foodCourtToView;
    private FoodCourtEntity foodCourtToUpdate;

    /**
     * Creates a new instance of ViewAllFoodCourtsManagedBean
     */
    public ViewAllFoodCourtsManagedBean() {
        foodCourts = new ArrayList<>();
        filteredFoodCourts = new ArrayList<>();
        foodCourt = new FoodCourtEntity();
    }

    @PostConstruct
    public void postConstruct() {
        setFoodCourts(foodCourtEntityControllerLocal.retrieveAllFoodCourts());
        filteredFoodCourts = this.foodCourts;

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

//    public void deleteProduct(ActionEvent event)
//    {
//        try
//        {
//            FoodCourtEntity foodCourtToDelete = (FoodCourtEntity)event.getComponent().getAttributes().get("productEntityToDelete");
//            foodCourtEntityControllerLocal.deleteProduct(foodCourtToDelete.getBusinessId());
//            
//            foodCourts.remove(foodCourtToDelete);
//            filteredFoodCourts.remove(foodCourtToDelete);
//
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product deleted successfully", null));
//        }
//        catch(ProductNotFoundException | DeleteProductException ex)
//        {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting product: " + ex.getMessage(), null));
//        }
//        catch(Exception ex)
//        {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
//        }
//    }
//
//    
    public List<FoodCourtEntity> getFoodCourts() {
        return foodCourts;
    }

    public void setFoodCourts(List<FoodCourtEntity> foodCourts) {
        this.foodCourts = foodCourts;
    }

    public List<FoodCourtEntity> getFilteredFoodCourts() {
        return filteredFoodCourts;
    }

    public void setFilteredFoodCourts(List<FoodCourtEntity> filteredFoodCourts) {
        this.filteredFoodCourts = filteredFoodCourts;
    }

    public FoodCourtEntity getFoodCourt() {
        return foodCourt;
    }

    public void setFoodCourt(FoodCourtEntity foodCourt) {
        this.foodCourt = foodCourt;
    }

    public FoodCourtEntity getFoodCourtToView() {
        return foodCourtToView;
    }

    public void setFoodCourtToView(FoodCourtEntity foodCourtToView) {
        this.foodCourtToView = foodCourtToView;
    }

    public FoodCourtEntity getFoodCourtToUpdate() {
        return foodCourtToUpdate;
    }

    public void setFoodCourtToUpdate(FoodCourtEntity foodCourtToUpdate) {
        this.foodCourtToUpdate = foodCourtToUpdate;
    }

        
    public void viewFoodCourtDetails(ActionEvent event) throws IOException
    {
        Long foodCourtIdToView = (Long)event.getComponent().getAttributes().get("foodCourtId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("foodCourtIdToView", foodCourtIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewFoodCourtDetails.xhtml");
    }


}
