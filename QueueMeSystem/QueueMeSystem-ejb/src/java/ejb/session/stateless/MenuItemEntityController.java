/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MenuItemEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.MenuItemNotFoundException;

/**
 *
 * @author User
 */
@Stateless
public class MenuItemEntityController implements MenuItemEntityControllerLocal {

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")
    private EntityManager em;

    @Override
    public MenuItemEntity createMenuItem(MenuItemEntity menuItemEntity) {
        em.persist(menuItemEntity);
        em.flush();
        em.refresh(menuItemEntity);

        return menuItemEntity;
    }

    @Override
    public MenuItemEntity retrieveMenuItemById(Long menuItemId) throws MenuItemNotFoundException {
        MenuItemEntity menuItemEntity = em.find(MenuItemEntity.class, menuItemId);

        if (menuItemEntity != null) {
            return menuItemEntity;
        } else {
            throw new MenuItemNotFoundException("Menu Item ID: " + menuItemId + " does not exist");
        }
    }

    @Override
    public List<MenuItemEntity> retrieveAllMenuItemEntity() throws MenuItemNotFoundException {
        Query query = em.createQuery("SELECT * FROM MenuItemEntity");
        List<MenuItemEntity> menuItemEntities = query.getResultList();

        if (menuItemEntities.isEmpty()) {
            throw new MenuItemNotFoundException("No menu items available.");
        } else {
            return menuItemEntities;
        }
    }

    @Override
    public void updateMenuItem(MenuItemEntity menuItemEntity) {
        em.merge(menuItemEntity);
    }

    @Override
    public void deleteMenuItem(Long menuItemId) {
        try {
            MenuItemEntity menuItemEntity = retrieveMenuItemById(menuItemId);
            em.remove(menuItemEntity);
        } catch (MenuItemNotFoundException ex) {

        }
    }
}
