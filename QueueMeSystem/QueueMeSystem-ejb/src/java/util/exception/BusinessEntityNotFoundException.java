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
public class BusinessEntityNotFoundException extends Exception {

    public BusinessEntityNotFoundException() {
    }

    public BusinessEntityNotFoundException(String message) {
        super(message);
    }
    
}
