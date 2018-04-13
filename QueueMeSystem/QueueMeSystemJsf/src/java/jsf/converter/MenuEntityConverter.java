/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.converter;

import entity.MenuEntity;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author User
 */
@FacesConverter(value = "menuEntityConverter", forClass = MenuEntity.class)
public class MenuEntityConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty() || value.equals("null")) {
            return null;
        }
        try {
            List<MenuEntity> menuEntities = (List<MenuEntity>) context.getExternalContext().getSessionMap().get("MenuEntityConverter.menuEntities");
            for (MenuEntity menuEntity : menuEntities) {
                if (menuEntity.getMenuId().toString().equals(value)) {
                    return menuEntity;
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

        if (value instanceof MenuEntity) {
            try {
                MenuEntity menuEntity = (MenuEntity) value;
                if (menuEntity.getMenuId() == null) {
                    return "";
                }
                return menuEntity.getMenuId().toString();
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid value");
            }
        } else {
            throw new IllegalArgumentException("Invalid value");
        }
    }
}
