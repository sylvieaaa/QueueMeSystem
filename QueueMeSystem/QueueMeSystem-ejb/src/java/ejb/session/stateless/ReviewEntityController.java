/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReviewEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
