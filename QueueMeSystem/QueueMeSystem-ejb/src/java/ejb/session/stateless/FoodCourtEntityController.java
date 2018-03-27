/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FoodCourtEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author User
 */
@Stateless
public class FoodCourtEntityController implements FoodCourtEntityControllerLocal {

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")
    private EntityManager em;

    @Override
    public FoodCourtEntity createFoodCourt(FoodCourtEntity foodCourtEntity) {
        em.persist(foodCourtEntity);
        em.flush();
        em.refresh(foodCourtEntity);
        
        return foodCourtEntity;
    }
    
    @Override
    public FoodCourtEntity retrieveFoodCourtById(Long foodCourtId) {
        FoodCourtEntity foodCourtEntity = em.find(FoodCourtEntity.class, foodCourtId);
        
        if(foodCourtEntity != null) {
            return foodCourtEntity;
        } else {
            return null;
        }
    }
}
