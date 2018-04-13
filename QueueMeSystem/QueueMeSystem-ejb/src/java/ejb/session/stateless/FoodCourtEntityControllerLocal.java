/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FoodCourtEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteFoodCourtException;
import util.exception.DuplicateEmailUserException;
import util.exception.FoodCourtNotFoundException;

/**
 *
 * @author User
 */
@Local
public interface FoodCourtEntityControllerLocal {

    public FoodCourtEntity createFoodCourt(FoodCourtEntity foodCourtEntity)throws DuplicateEmailUserException;

    public FoodCourtEntity retrieveFoodCourtById(Long foodCourtId)throws FoodCourtNotFoundException;

    public List<FoodCourtEntity> retrieveAllFoodCourts();

    public void updateFoodCourt(FoodCourtEntity foodCourt) throws FoodCourtNotFoundException;

    public void disableFoodCourt(Long foodCourtId) throws FoodCourtNotFoundException;

    public FoodCourtEntity retrieveFoodCourtByUsername(String username) throws FoodCourtNotFoundException;

    public void updatePassword(String username, String password);
    
}
