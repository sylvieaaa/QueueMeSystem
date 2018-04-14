/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.datamodel;

import entity.MenuEntity;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "menuEntityRsp", propOrder = {
    "menuEntity"
})
public class MenuEntityRsp {
    private MenuEntity menuEntity;

    public MenuEntityRsp() {
    }

    public MenuEntityRsp(MenuEntity menuEntity) {
        this.menuEntity = menuEntity;
    }

    public MenuEntity getMenuEntity() {
        return menuEntity;
    }

    public void setMenuEntity(MenuEntity menuEntity) {
        this.menuEntity = menuEntity;
    }
    
}
