/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import java.io.File;
import javax.ejb.Local;
import util.exception.BusinessEntityNotFoundException;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author User
 */
@Local
public interface EmailControllerLocal {

    public void forgetPasswordEmail(String toEmail) throws BusinessEntityNotFoundException;

    public void sendReceipt(File receiptFile, CustomerEntity customerEntity) throws CustomerNotFoundException;
    
}
