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
import util.exception.MenuNotFoundException;
import util.exception.VendorNotFoundException;

/**
 *
 * @author User
 */
@Stateless
public class MenuEntityController implements MenuEntityControllerLocal {

    @EJB
    private VendorEntityControllerLocal vendorEntityControllerLocal;

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")
    private EntityManager em;

    @Override
    public MenuEntity createMenu(MenuEntity menuEntity, VendorEntity vendorEntity) throws VendorNotFoundException {
        vendorEntity = vendorEntityControllerLocal.retrieveVendorStaffById(vendorEntity.getBusinessId());
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
            
            for(MenuEntity menuEntity: menuEntities) {
                for(CategoryEntity categoryEntity: menuEntity.getCategoryEntities()) {
                    categoryEntity.getMenuItemEntities().size();
                }
            }

            return menuEntities;
        } catch (NonUniqueResultException | NoResultException ex) {

        }
        return null;
    }

    @Override
    public MenuEntity retrieveMenyById(Long menuId) throws MenuNotFoundException {
        MenuEntity menuEntity = em.find(MenuEntity.class, menuId);
        
        if(menuEntity != null) {
            return menuEntity;
        } else {
            throw new MenuNotFoundException("Menu ID: " + menuId + " does not exist.");
        }
    }
}
