/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.converter;

import entity.TagEntity;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author User
 */
@FacesConverter(value = "tagEntityConverter", forClass = TagEntity.class)
public class TagEntityConverter implements Converter{

    /**
     * Creates a new instance of TagEntityConverter
     */
    public TagEntityConverter() {
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        System.err.println(" value:  " + value);
        if (value == null || value.trim().isEmpty() || value.equals("null")) {
            return null;
        }
        try {
            List<TagEntity> tagEntities = (List<TagEntity>) context.getExternalContext().getSessionMap().get("TagEntityConverter.tagEntities");
            for(TagEntity tagEntity: tagEntities) {
                if(tagEntity.getTagId().toString().equals(value)) {
                     System.err.println("returned");
                    return tagEntity;
                }
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException("Please select a valid value");
        }

        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }

        if (value instanceof String) {
            return "";
        }

        if (value instanceof TagEntity) {
            try {
                TagEntity tagEntity = (TagEntity) value;
                if (tagEntity.getTagId()== null) {
                    return "";
                }
                System.err.println(tagEntity + " " + value);
                return tagEntity.getTagId().toString();
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid value");
            }
        } else {
            throw new IllegalArgumentException("Invalid value");
        }
    }
    
}
