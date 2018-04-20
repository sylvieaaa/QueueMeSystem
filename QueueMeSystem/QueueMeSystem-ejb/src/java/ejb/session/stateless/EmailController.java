/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import java.io.File;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import util.email.EmailManager;
import util.exception.BusinessEntityNotFoundException;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author User
 */
@Stateless
public class EmailController implements EmailControllerLocal {

    @EJB
    private CustomerEntityControllerLocal customerEntityControllerLocal;

    @EJB
    private BusinessEntityControllerLocal businessEntityControllerLocal;
    
    private String yourAccId = "yxsoong";
    private String password = "";
    
    @Override
    public void forgetPasswordEmail(String toEmail) throws BusinessEntityNotFoundException {
        String newPassword = businessEntityControllerLocal.generateRandomPassword(toEmail);
        EmailManager emailManager = new EmailManager(yourAccId + "@sunfire.comp.nus.edu.sg", password);
        emailManager.forgetPasswordEmail(yourAccId + "@sunfire.comp.nus.edu.sg", toEmail, newPassword);
    }
    
    @Override
    public void sendReceipt(File receiptFile, CustomerEntity customerEntity) throws CustomerNotFoundException {
        customerEntity = customerEntityControllerLocal.retrieveCustomerByUsername(customerEntity.getUsername());
        EmailManager emailManager = new EmailManager(yourAccId + "@sunfire.comp.nus.edu.sg", password);
        emailManager.receiptEmail(yourAccId + "@sunfire.comp.nus.edu.sg", customerEntity.getUsername(), receiptFile);
    }
}
