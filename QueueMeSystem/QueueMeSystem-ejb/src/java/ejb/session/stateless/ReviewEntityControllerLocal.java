/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReviewEntity;
import entity.VendorEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author User
 */
@Local
public interface ReviewEntityControllerLocal {

    public ReviewEntity createReview(ReviewEntity reviewEntity);
    
    public List<ReviewEntity> retrieveAllReviews(VendorEntity vendorEntity);
    
    public int averageReviewScore(VendorEntity vendorEntity);
    
}

