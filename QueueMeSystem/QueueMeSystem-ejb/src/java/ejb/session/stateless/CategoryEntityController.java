/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
import entity.MenuEntity;
import entity.MenuItemEntity;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CategoryNotFoundException;
import util.exception.MenuItemNotFoundException;
import util.exception.MenuNotFoundException;

/**
 *
 * @author User
 */
@Stateless
public class CategoryEntityController implements CategoryEntityControllerLocal {

    @EJB
    private MenuItemEntityControllerLocal menuItemEntityControllerLocal;

    @EJB
    private MenuEntityControllerLocal menuEntityControllerLocal;

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")
    private EntityManager em;

    @Override
    public CategoryEntity createCategory(CategoryEntity categoryEntity, MenuEntity menuEntity) throws MenuNotFoundException {
        menuEntity = menuEntityControllerLocal.retrieveMenyById(menuEntity.getMenuId());
        categoryEntity.setMenuEntity(menuEntity);
        em.persist(categoryEntity);
        menuEntity.getCategoryEntities().add(categoryEntity);
        em.flush();
        em.refresh(categoryEntity);

        return categoryEntity;
    }
    
    @Override
    public CategoryEntity retrieveCategoryById (Long categoryId) throws CategoryNotFoundException {
        CategoryEntity categoryEntity = em.find(CategoryEntity.class, categoryId);
        
        if(categoryEntity != null) {
            return categoryEntity;
        } else {
            throw new CategoryNotFoundException("Category ID: " + categoryId + " does not exist.");
        }
    }
    
    @Override
    public void addMenuItem(CategoryEntity categoryEntity, MenuItemEntity menuItemEntity) throws CategoryNotFoundException, MenuItemNotFoundException{
        categoryEntity = retrieveCategoryById(categoryEntity.getCategoryId());
        menuItemEntity = menuItemEntityControllerLocal.retrieveMenuItemById(menuItemEntity.getMenuItemId());
        
        categoryEntity.getMenuItemEntities().add(menuItemEntity);
        menuItemEntity.getCategoryEntities().add(categoryEntity);
    }
}
