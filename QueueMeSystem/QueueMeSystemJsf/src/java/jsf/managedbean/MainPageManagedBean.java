/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;

/**
 *
 * @author User
 */
@Named(value = "mainPageManagedBean")
@ViewScoped
public class MainPageManagedBean implements Serializable{

    /**
     * Creates a new instance of MainPageManagedBean
     */
    public MainPageManagedBean() {
    }
    
    public void logout(ActionEvent event) throws IOException{
        ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
    }
}
