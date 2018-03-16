/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AdminEntity;
import javax.ejb.Local;
import util.exception.AdminNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author User
 */
@Local
public interface AdminEntityControllerLocal {

    public AdminEntity createAdmin(AdminEntity adminEntity);

    public AdminEntity retrieveAdminByUsername(String username) throws AdminNotFoundException;

    public AdminEntity adminLogin(String username, String password) throws InvalidLoginCredentialException;
    
}
