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
import util.exception.TagAlreadyExistException;

/**
 *
 * @author User
 */
@Stateless
public class TagEntityController implements TagEntityControllerLocal {

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")
    private EntityManager em;

    @Override
    public TagEntity createTagEntity(TagEntity tagEntity) throws TagAlreadyExistException {
        String tag = tagEntity.getType();
        tag = tag.substring(0, 1).toUpperCase() + tag.substring(1);
        Query query = em.createQuery("SELECT t FROM TagEntity t WHERE t.type=:inTagType");
        query.setParameter("inTagType", tag);

        if (query.getResultList().isEmpty()) {
            em.persist(tagEntity);
            em.flush();
            em.refresh(tagEntity);

            return tagEntity;
        } else {
            throw new TagAlreadyExistException("Tag has already been created");
        }

    }

    @Override
    public List<TagEntity> retrieveAllTags() {
        Query query = em.createQuery("SELECT t FROM TagEntity t");

        return query.getResultList();
    }
}
