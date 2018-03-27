/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author SYLVIA
 */
@Named(value = "updatePasswordManagedBean")
@ViewScoped
public class UpdatePasswordManagedBean implements Serializable{

    /**
     * Creates a new instance of UpdatePasswordManagedBean
     */
    public UpdatePasswordManagedBean() {
    }
    
}
