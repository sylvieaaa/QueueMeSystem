/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import util.email.EmailManager;
import util.exception.BusinessEntityNotFoundException;

/**
 *
 * @author User
 */
@Stateless
public class EmailController implements EmailControllerLocal {

    @EJB
    private BusinessEntityControllerLocal businessEntityControllerLocal;

    @Override
    public void forgetPasswordEmail(String toEmail) throws BusinessEntityNotFoundException {
        String newPassword = businessEntityControllerLocal.generateRandomPassword(toEmail);
        EmailManager emailManager = new EmailManager("<REPLACE_WITH_YOUR_UNIX_USERNAME>@sunfire.comp.nus.edu.sg", "<REPLACE_WITH_YOUR_UNIX_PASSWORD>");
        emailManager.forgetPasswordEmail("your email address", toEmail, newPassword);
    }
}
