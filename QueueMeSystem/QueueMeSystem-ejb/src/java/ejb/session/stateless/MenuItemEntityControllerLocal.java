/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MenuItemEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.MenuItemNotFoundException;

/**
 *
 * @author User
 */
@Local
public interface MenuItemEntityControllerLocal {
    public MenuItemEntity createMenuItem(MenuItemEntity menuItemEntity);

    public MenuItemEntity retrieveMenuItemById(Long menuItemId) throws MenuItemNotFoundException;

    public List<MenuItemEntity> retrieveAllMenuItemEntity() throws MenuItemNotFoundException;

    public void updateMenuItem(MenuItemEntity menuItemEntity);

    public void deleteMenuItem(Long menuItemId);
}
