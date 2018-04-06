/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author SYLVIA
 */
@Named(value = "dateConvertManagedBean")
@RequestScoped
public class DateConvertManagedBean {

    /**
     * Creates a new instance of DateConvertManagedBean
     */
    public DateConvertManagedBean() {
    }
    
    public String dateConvert(Date operatingTime){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String converted = sdf.format(operatingTime);
        return converted;
        
    }
}
