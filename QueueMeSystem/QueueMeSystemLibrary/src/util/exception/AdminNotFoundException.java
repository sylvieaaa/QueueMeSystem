/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author User
 */
public class AdminNotFoundException extends Exception{

    public AdminNotFoundException() {
    }

    public AdminNotFoundException(String message) {
        super(message);
    }
    
}
