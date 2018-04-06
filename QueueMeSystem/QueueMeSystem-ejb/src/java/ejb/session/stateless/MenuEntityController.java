/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
import entity.MenuEntity;
import entity.MenuItemEntity;
import entity.VendorEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CategoryNotFoundException;
import util.exception.MenuNotFoundException;
import util.exception.VendorNotFoundException;

/**
 *
 * @author User
 */
@Stateless
public class MenuEntityController implements MenuEntityControllerLocal {

    @EJB
    private CategoryEntityControllerLocal categoryEntityControllerLocal;

    @EJB
    private VendorEntityControllerLocal vendorEntityControllerLocal;

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")
    private EntityManager em;

    @Override
    public MenuEntity createMenu(MenuEntity menuEntity, VendorEntity vendorEntity) throws VendorNotFoundException {
        vendorEntity = vendorEntityControllerLocal.retrieveVendorById(vendorEntity.getBusinessId());
        if(vendorEntity.getMenuEntities().isEmpty()) {
            menuEntity.setSelected(Boolean.TRUE);
        }
        menuEntity.setVendorEntity(vendorEntity);
        em.persist(menuEntity);
        vendorEntity.getMenuEntities().add(menuEntity);
        em.flush();
        em.refresh(menuEntity);

        return menuEntity;
    }

    @Override
    public List<MenuEntity> retrieveMenusByVendor(VendorEntity vendorEntity) {
        Query query = em.createQuery("SELECT m FROM MenuEntity m WHERE m.vendorEntity=:inVendorEntity");
        query.setParameter("inVendorEntity", vendorEntity);

        try {
            List<MenuEntity> menuEntities = query.getResultList();

            for (MenuEntity menuEntity : menuEntities) {
                for (CategoryEntity categoryEntity : menuEntity.getCategoryEntities()) {
                    categoryEntity.getMenuItemEntities().size();
                }
            }

            return menuEntities;
        } catch (NonUniqueResultException | NoResultException ex) {

        }
        return null;
    }

    @Override
    public MenuEntity retrieveMenuById(Long menuId) throws MenuNotFoundException {
        MenuEntity menuEntity = em.find(MenuEntity.class, menuId);

        if (menuEntity != null) {
            return menuEntity;
        } else {
            throw new MenuNotFoundException("Menu ID: " + menuId + " does not exist.");
        }
    }

    @Override
    public void selectDefaultMenu(MenuEntity menuEntity, VendorEntity vendorEntity) {
        List<MenuEntity> menuEntities = retrieveMenusByVendor(vendorEntity);
        for (MenuEntity me : menuEntities) {
            if (me.equals(menuEntity)) {
                me.setSelected(Boolean.TRUE);
            } else {
                me.setSelected(Boolean.FALSE);
            }
        }
    }

    @Override
    public void removeMenuEntity(MenuEntity menuEntity, VendorEntity vendorEntity) throws MenuNotFoundException, VendorNotFoundException {
        menuEntity = retrieveMenuById(menuEntity.getMenuId());
        vendorEntity = vendorEntityControllerLocal.retrieveVendorById(vendorEntity.getBusinessId());
        vendorEntity.getMenuEntities().remove(menuEntity);

        List<CategoryEntity> categoryEntities = menuEntity.getCategoryEntities();

        for (CategoryEntity categoryEntity : categoryEntities) {
            for (MenuItemEntity menuItemEntity : categoryEntity.getMenuItemEntities()) {
                menuItemEntity.getCategoryEntities().remove(categoryEntity);
            }
            //categoryEntity.setMenuEntity(null);
        }
        
        em.remove(menuEntity);
    }

    @Override
    public void removeCategoryFromMenu(MenuEntity menuEntity, CategoryEntity categoryEntity) throws CategoryNotFoundException, MenuNotFoundException {
        menuEntity = retrieveMenuById(menuEntity.getMenuId());
        categoryEntity = categoryEntityControllerLocal.retrieveCategoryById(categoryEntity.getCategoryId());
        
        for(MenuItemEntity menuItemEntity: categoryEntity.getMenuItemEntities()) {
            menuItemEntity.getCategoryEntities().remove(categoryEntity);
        }
        
        menuEntity.getCategoryEntities().remove(categoryEntity);
        em.remove(categoryEntity);
    }
    
    @Override
    public MenuEntity retrieveDisplayMenu(VendorEntity vendorEntity) throws MenuNotFoundException {
        Query query = em.createQuery("SELECT m FROM MenuEntity m WHERE m.selected=true AND m.vendorEntity=:inVendorEntity");
        query.setParameter("inVendorEntity", vendorEntity);
        
        try {
            MenuEntity menuEntity = (MenuEntity) query.getSingleResult();
            for(CategoryEntity categoryEntity: menuEntity.getCategoryEntities()) {
                categoryEntity.getMenuItemEntities().size();
            }
            
            return menuEntity;
        } catch (NonUniqueResultException | NoResultException ex) {
            throw new MenuNotFoundException("No menu has been selected for display");
        }
    }
}
