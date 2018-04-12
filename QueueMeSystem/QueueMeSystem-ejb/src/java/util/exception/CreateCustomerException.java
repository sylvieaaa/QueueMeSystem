/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author lowru
 */
public class CreateCustomerException extends Exception {

    /**
     * Creates a new instance of <code>CreateCustomerException</code> without
     * detail message.
     */
    public CreateCustomerException() {
    }

    /**
     * Constructs an instance of <code>CreateCustomerException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateCustomerException(String msg) {
        super(msg);
    }
}
