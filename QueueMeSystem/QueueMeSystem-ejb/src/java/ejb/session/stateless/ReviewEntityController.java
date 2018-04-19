/*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.ReviewEntity;
import entity.VendorEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CustomerNotFoundException;
import util.exception.VendorNotFoundException;

/**
 *
 * @author User
 */
@Stateless
public class ReviewEntityController implements ReviewEntityControllerLocal {

    @EJB
    private VendorEntityControllerLocal vendorEntityControllerLocal;

    @EJB
    private CustomerEntityControllerLocal customerEntityControllerLocal;

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")
    private EntityManager em;

    @Override
    public ReviewEntity createReview(ReviewEntity reviewEntity, CustomerEntity customerEntity, VendorEntity vendorEntity) throws VendorNotFoundException, CustomerNotFoundException {

        customerEntity = customerEntityControllerLocal.retrieveCustomerByUsername(customerEntity.getUsername());
        vendorEntity = vendorEntityControllerLocal.retrieveVendorById(vendorEntity.getBusinessId());
        em.persist(reviewEntity);
        reviewEntity.setCustomerEntity(customerEntity);
        reviewEntity.setVendorEntity(vendorEntity);
        customerEntity.getReviewEntities().add(reviewEntity);
        vendorEntity.getReviewEntities().add(reviewEntity);
        em.flush();
        em.refresh(reviewEntity);
        averageReviewScore(vendorEntity);

        return reviewEntity;
    }

    @Override
    public List<ReviewEntity> retrieveAllReviews(VendorEntity vendorEntity) {
        return vendorEntity.getReviewEntities();
    }

    @Override
    public int averageReviewScore(VendorEntity vendorEntity) {
        int score = 0;
        int size = vendorEntity.getReviewEntities().size();
        if (size == 0) {
            return 0;
        } else {
            for (ReviewEntity review : vendorEntity.getReviewEntities()) {
                score += review.getRating();
            }
            return (score / size);
        }
    }

    public void updateReview(VendorEntity vendorEntity) {
        VendorEntity vendor = em.find(VendorEntity.class, vendorEntity.getBusinessId());
    }

}
