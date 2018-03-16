/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
import javax.ejb.Local;

/**
 *
 * @author User
 */
@Local
public interface CategoryEntityControllerLocal {

    public CategoryEntity createCategory(CategoryEntity categoryEntity);
    
}
