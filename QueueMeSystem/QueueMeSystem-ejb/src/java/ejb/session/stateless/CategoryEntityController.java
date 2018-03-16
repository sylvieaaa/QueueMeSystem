/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author User
 */
@Stateless
public class CategoryEntityController implements CategoryEntityControllerLocal {

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")
    private EntityManager em;

    @Override
    public CategoryEntity createCategory(CategoryEntity categoryEntity) {
        em.persist(categoryEntity);
        em.flush();
        em.refresh(categoryEntity);

        return categoryEntity;
    }
}
