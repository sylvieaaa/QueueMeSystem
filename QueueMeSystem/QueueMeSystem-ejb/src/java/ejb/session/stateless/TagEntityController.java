/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TagEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author User
 */
@Stateless
public class TagEntityController implements TagEntityControllerLocal {

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")
    private EntityManager em;

    @Override
    public TagEntity createTagEntity(TagEntity tagEntity) {
        em.persist(tagEntity);
        em.flush();
        em.refresh(tagEntity);
        
        return tagEntity;
    }

    @Override
    public List<TagEntity> retrieveAllTags() {
        Query query = em.createQuery("SELECT t FROM TagEntity t");
        
        return query.getResultList();
    }
}
