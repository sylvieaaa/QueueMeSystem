/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FoodCourtEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.DeleteFoodCourtException;
import util.exception.FoodCourtNotFoundException;

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
    public FoodCourtEntity retrieveFoodCourtById(Long foodCourtId) throws FoodCourtNotFoundException {
        FoodCourtEntity foodCourtEntity = em.find(FoodCourtEntity.class, foodCourtId);
        
        if(foodCourtEntity != null) {
            return foodCourtEntity;
        } else {
            throw new FoodCourtNotFoundException("Food Court Id " + foodCourtId + " does not exist");
        }
    }
    
    @Override
    public List<FoodCourtEntity> retrieveAllFoodCourts(){
        Query query = em.createQuery("SELECT f FROM FoodCourtEntity f ORDER BY f.name ASC");
        return query.getResultList();
    }
    
    @Override
    public void updateFoodCourt(FoodCourtEntity foodCourt) throws FoodCourtNotFoundException{
        if (foodCourt.getBusinessId() != null)
        {
            FoodCourtEntity foodCourtToUpdate = retrieveFoodCourtById(foodCourt.getBusinessId());
     
                foodCourtToUpdate.setName(foodCourt.getName());
                foodCourtToUpdate.setAddress(foodCourt.getAddress());
                foodCourtToUpdate.setDescription(foodCourt.getDescription());
                foodCourtToUpdate.setRatings(foodCourt.getRatings());
                foodCourtToUpdate.setPostalCode(foodCourt.getPostalCode());
                foodCourtToUpdate.setStartTime(foodCourt.getStartTime());
                foodCourtToUpdate.setEndTime(foodCourt.getEndTime());
                foodCourtToUpdate.setUsername(foodCourt.getUsername());
           
        }
        else{
            throw new FoodCourtNotFoundException("Id not provided for FoodCourt to be updated");
        }
    }
    
    public void deleteFoodCourt(Long foodCourtId) throws FoodCourtNotFoundException, DeleteFoodCourtException{
        FoodCourtEntity foodCourtToDelete = retrieveFoodCourtById(foodCourtId);
        
        em.merge(foodCourtToDelete);
       
    }
}
