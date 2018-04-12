/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MenuItemEntity;
import entity.VendorEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.MenuItemNotFoundException;
import util.exception.VendorNotFoundException;

/**
 *
 * @author User
 */
@Stateless
public class MenuItemEntityController implements MenuItemEntityControllerLocal {

    @EJB
    private VendorEntityControllerLocal vendorEntityControllerLocal;

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")
    private EntityManager em;

    @Override
    public MenuItemEntity createMenuItem(MenuItemEntity menuItemEntity, VendorEntity vendorEntity) throws VendorNotFoundException {
        try {
            vendorEntity = vendorEntityControllerLocal.retrieveVendorById(vendorEntity.getBusinessId());
            menuItemEntity.setVendorEntity(vendorEntity);
            em.persist(menuItemEntity);
            vendorEntity.getMenuItemEntities().add(menuItemEntity);
            em.flush();
            em.refresh(menuItemEntity);

            return menuItemEntity;
        } catch (VendorNotFoundException ex) {
            throw ex;
        }

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
    public List<MenuItemEntity> retrieveAllMenuItemEntityByVendor(VendorEntity vendorEntity) {
        Query query = em.createQuery("SELECT m FROM MenuItemEntity m WHERE m.vendorEntity=:inVendorEntity");
        query.setParameter("inVendorEntity", vendorEntity);

        List<MenuItemEntity> menuItemEntities = query.getResultList();
        for(MenuItemEntity menuItemEntity: menuItemEntities) {
            menuItemEntity.getTagEntities().size();
        }

        return menuItemEntities;

    }

    @Override
    public void updateMenuItem(MenuItemEntity menuItemEntityToUpdate) throws MenuItemNotFoundException {
        System.err.println(menuItemEntityToUpdate);
        MenuItemEntity menuItemEntity = retrieveMenuItemById(menuItemEntityToUpdate.getMenuItemId());
        
        menuItemEntity.setMenuItemName(menuItemEntityToUpdate.getMenuItemName());
        menuItemEntity.setDescription(menuItemEntityToUpdate.getDescription());
        menuItemEntity.setPrice(menuItemEntityToUpdate.getPrice());
        menuItemEntity.setPhotoURL(menuItemEntityToUpdate.getPhotoURL());
        menuItemEntity.setTagEntities(menuItemEntityToUpdate.getTagEntities());
    }

    @Override
    public void deleteMenuItem(Long menuItemId) {
        try {
            MenuItemEntity menuItemEntity = retrieveMenuItemById(menuItemId);
            VendorEntity vendorEntity = menuItemEntity.getVendorEntity();
            vendorEntity.getMenuItemEntities().remove(menuItemEntity);
            em.remove(menuItemEntity);
        } catch (MenuItemNotFoundException ex) {

        }
    }
}
