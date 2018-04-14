/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AdminEntity;
import entity.CustomerEntity;
import entity.FoodCourtEntity;
import entity.OrderEntity;
import entity.ReviewEntity;
import entity.SaleTransactionEntity;
import entity.VendorEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AdminNotFoundException;
import util.exception.DeleteFoodCourtException;
import util.exception.DuplicateEmailUserException;
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
    public FoodCourtEntity createFoodCourt(FoodCourtEntity foodCourtEntity) throws DuplicateEmailUserException {
        Query query = em.createQuery("SELECT f FROM FoodCourtEntity f WHERE f.username=:inUsername");
        query.setParameter("inUsername", foodCourtEntity.getUsername());

        try {
            FoodCourtEntity check = (FoodCourtEntity) query.getSingleResult();
            throw new DuplicateEmailUserException("Email is not unique");

        } catch (NoResultException exc) {
            em.persist(foodCourtEntity);
            em.flush();
            em.refresh(foodCourtEntity);
        }

        return foodCourtEntity;
    }

    @Override
    public FoodCourtEntity retrieveFoodCourtById(Long foodCourtId) throws FoodCourtNotFoundException {
        FoodCourtEntity foodCourtEntity = em.find(FoodCourtEntity.class, foodCourtId);

        if (foodCourtEntity != null) {
            return foodCourtEntity;
        } else {
            throw new FoodCourtNotFoundException("Food Court Id " + foodCourtId + " does not exist");
        }
    }

    @Override
    public List<FoodCourtEntity> retrieveAllFoodCourts() {
        Query query = em.createQuery("SELECT f FROM FoodCourtEntity f WHERE f.enable = TRUE ORDER BY f.name ASC");
        return query.getResultList();
    }

    @Override
    public void updateFoodCourt(FoodCourtEntity foodCourt) throws FoodCourtNotFoundException {
        if (foodCourt.getBusinessId() != null) {
            FoodCourtEntity foodCourtToUpdate = retrieveFoodCourtById(foodCourt.getBusinessId());

            foodCourtToUpdate.setName(foodCourt.getName());
            foodCourtToUpdate.setAddress(foodCourt.getAddress());
            foodCourtToUpdate.setDescription(foodCourt.getDescription());
            foodCourtToUpdate.setPostalCode(foodCourt.getPostalCode());
            foodCourtToUpdate.setStartTime(foodCourt.getStartTime());
            foodCourtToUpdate.setEndTime(foodCourt.getEndTime());

        } else {
            throw new FoodCourtNotFoundException("Id not provided for FoodCourt to be updated");
        }
    }

    @Override
    public void disableFoodCourt(Long foodCourtId) throws FoodCourtNotFoundException {
        if (foodCourtId != null) {
            FoodCourtEntity foodCourtToDisable = retrieveFoodCourtById(foodCourtId);
            foodCourtToDisable.setEnable(Boolean.FALSE);
        } else {
            throw new FoodCourtNotFoundException("Id not provided for FoodCourt to be disabled");
        }
    }

    @Override
    public FoodCourtEntity retrieveFoodCourtByUsername(String username) throws FoodCourtNotFoundException {
        Query query = em.createQuery("SELECT f FROM FoodCourtEntity f WHERE f.username=:inUsername");
        query.setParameter("inUsername", username);

        try {
            return (FoodCourtEntity) query.getSingleResult();
        } catch (NonUniqueResultException | NoResultException ex) {
            throw new FoodCourtNotFoundException("Food Court " + username + " does not exists!");
        }
    }

    @Override
    public void updatePassword(String username, String password) {
        try {
            FoodCourtEntity foodCourtToUpdate = retrieveFoodCourtByUsername(username);
            foodCourtToUpdate.setPassword(password);
            em.merge(foodCourtToUpdate);
        } catch (FoodCourtNotFoundException ex) {

        }
    }

    @Override
    public void updateFileUrl(Long foodCourtId, String url) {
        try {
            FoodCourtEntity foodCourtToUpdate = retrieveFoodCourtById(foodCourtId);
            foodCourtToUpdate.setFileURL(url);
            em.merge(foodCourtToUpdate);
        } catch (FoodCourtNotFoundException ex) {

        }
    }
}
