/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ReviewEntityControllerLocal;
import entity.ReviewEntity;
import entity.VendorEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author KERK
 */
@Named(value = "manageReviewManagedBean")
@ViewScoped
public class ManageReviewManagedBean implements Serializable {

    @EJB(name = "ReviewEntityControllerLocal")
    private ReviewEntityControllerLocal reviewEntityControllerLocal;
    private List<ReviewEntity> reviewEntities;
    private ReviewEntity review;
    private Integer avgReviewScore;

    /**
     * Creates a new instance of ManageReviewManagedBean
     */
    public ManageReviewManagedBean() {
        review = new ReviewEntity();
        reviewEntities = new ArrayList<>();
    }
    
    @PostConstruct
    public void postConstruct()
    {     
        VendorEntity vendorEntity = (VendorEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("businessEntity");
        reviewEntities = reviewEntityControllerLocal.retrieveAllReviews(vendorEntity);
        avgReviewScore = reviewEntityControllerLocal.averageReviewScore(vendorEntity);
   
    }

    public int getAvgReviewScore() {
        return avgReviewScore;
    }

    public void setAvgReviewScore(int avgReviewScore) {
        this.avgReviewScore = avgReviewScore;
    }

    public ReviewEntity getReview() {
        return review;
    }

    public void setReview(ReviewEntity review) {
        this.review = review;
    }

    public List<ReviewEntity> getReviewEntities() {
        return reviewEntities;
    }

    public void setReviewEntities(List<ReviewEntity> reviewEntities) {
        this.reviewEntities = reviewEntities;
    }
}
