/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReviewEntity;
import entity.VendorEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author User
 */
@Stateless
public class ReviewEntityController implements ReviewEntityControllerLocal {

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")
    private EntityManager em;

    @Override
    public ReviewEntity createReview(ReviewEntity reviewEntity) {
        em.persist(reviewEntity);
        em.flush();
        em.refresh(reviewEntity);
        
        return reviewEntity;
    }
    
    @Override
    public List<ReviewEntity> retrieveAllReviews()
    {
        Query query = em.createQuery("SELECT r FROM ReviewEntity r ORDER BY r.reviewId ASC");
 
        return query.getResultList();
    }
    
    @Override
    public int averageReviewScore(VendorEntity vendorEntity) {
        int score = 0;
        int size = vendorEntity.getReviewEntities().size();
        for (ReviewEntity review: vendorEntity.getReviewEntities()) {
            score += review.getRating();
        }
        return (score / size);
    }
}
