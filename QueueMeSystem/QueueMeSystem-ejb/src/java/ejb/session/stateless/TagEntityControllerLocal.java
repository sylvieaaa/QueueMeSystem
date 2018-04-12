/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TagEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author User
 */
@Local
public interface TagEntityControllerLocal {

    public TagEntity createTagEntity(TagEntity tagEntity);

    public List<TagEntity> retrieveAllTags();
    
}
