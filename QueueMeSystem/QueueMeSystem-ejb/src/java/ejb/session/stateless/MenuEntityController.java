/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
import entity.MenuEntity;
import entity.VendorEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author User
 */
@Stateless
public class MenuEntityController implements MenuEntityControllerLocal {

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")
    private EntityManager em;

    @Override
    public MenuEntity createMenu(MenuEntity menuEntity) {
        em.persist(menuEntity);
        em.flush();
        em.refresh(menuEntity);
        
        return menuEntity;
    }
    
    @Override
    public MenuEntity retrieveMenuByVendor(VendorEntity vendorEntity) {
        Query query = em.createQuery("SELECT m FROM MenuEntity m WHERE m.vendorEntity=:inVendorEntity");
        query.setParameter("inVendorEntity", vendorEntity);
        
        try{
            MenuEntity menuEntity = (MenuEntity) query.getSingleResult();
            menuEntity.getCategoryEntities().size();
            for(CategoryEntity categoryEntity: menuEntity.getCategoryEntities()) {
                categoryEntity.getMenuItemEntities().size();
                System.err.println(categoryEntity.getMenuItemEntities().size());
            }
            
            return menuEntity;
        } catch (NonUniqueResultException | NoResultException ex) {
            
        }
        return null;
    }
}
