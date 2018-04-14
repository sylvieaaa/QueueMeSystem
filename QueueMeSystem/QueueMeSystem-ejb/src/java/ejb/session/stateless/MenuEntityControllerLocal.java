/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
import entity.MenuEntity;
import entity.VendorEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CategoryNotFoundException;
import util.exception.MenuNotFoundException;
import util.exception.VendorNotFoundException;

/**
 *
 * @author User
 */
@Local
public interface MenuEntityControllerLocal {

    public MenuEntity createMenu(MenuEntity menuEntity, VendorEntity vendorEntity)throws VendorNotFoundException;

    public List<MenuEntity> retrieveMenusByVendor(VendorEntity vendorEntity);
    
    public MenuEntity retrieveMenuById(Long menuId) throws MenuNotFoundException;

    public void selectDefaultMenu(MenuEntity menuEntity, VendorEntity vendorEntity);

    public void removeMenuEntity(MenuEntity menuEntity, VendorEntity vendorEntity) throws MenuNotFoundException, VendorNotFoundException;

    public void removeCategoryFromMenu(MenuEntity menuEntity, CategoryEntity categoryEntity) throws CategoryNotFoundException, MenuNotFoundException;

    public MenuEntity retrieveDisplayMenu(VendorEntity vendorEntity) throws MenuNotFoundException;

    public MenuEntity retrieveDefaultMenuByVendorId(Long vendorId) throws MenuNotFoundException;
}
