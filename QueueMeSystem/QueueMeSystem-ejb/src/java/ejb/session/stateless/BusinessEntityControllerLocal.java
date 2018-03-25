/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BusinessEntity;
import javax.ejb.Local;
import util.exception.BusinessEntityNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author User
 */
@Local
public interface BusinessEntityControllerLocal {

    public BusinessEntity retrieveBusinessEntityByUsername(String username) throws BusinessEntityNotFoundException;

    public BusinessEntity login(String username, String password) throws InvalidLoginCredentialException;

    public String generateRandomPassword(String username) throws BusinessEntityNotFoundException;
    
}
