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
public class DuplicateEmailUserException extends Exception {

    /**
     * Creates a new instance of <code>DuplicateEmailUserException</code>
     * without detail message.
     */
    public DuplicateEmailUserException() {
    }

    /**
     * Constructs an instance of <code>DuplicateEmailUserException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public DuplicateEmailUserException(String msg) {
        super(msg);
    }
}
