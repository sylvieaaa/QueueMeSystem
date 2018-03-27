/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
import entity.MenuEntity;
import entity.MenuItemEntity;
import javax.ejb.Local;
import util.exception.CategoryNotFoundException;
import util.exception.MenuItemNotFoundException;
import util.exception.MenuNotFoundException;

/**
 *
 * @author User
 */
@Local
public interface CategoryEntityControllerLocal {

    public CategoryEntity createCategory(CategoryEntity categoryEntity, MenuEntity menuEntity) throws MenuNotFoundException;

    public CategoryEntity retrieveCategoryById(Long categoryId) throws CategoryNotFoundException;

    public void addMenuItem(CategoryEntity categoryEntity, MenuItemEntity menuItemEntity) throws CategoryNotFoundException, MenuItemNotFoundException;
    
}
