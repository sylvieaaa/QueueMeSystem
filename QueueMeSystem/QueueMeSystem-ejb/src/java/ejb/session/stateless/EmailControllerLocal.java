/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import javax.ejb.Local;
import util.exception.BusinessEntityNotFoundException;

/**
 *
 * @author User
 */
@Local
public interface EmailControllerLocal {

    public void forgetPasswordEmail(String toEmail) throws BusinessEntityNotFoundException;
    
}
