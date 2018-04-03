/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author SYLVIA
 */
public class DeleteFoodCourtException extends Exception {

    /**
     * Creates a new instance of <code>DeleteFoodCourtException</code> without
     * detail message.
     */
    public DeleteFoodCourtException() {
    }

    /**
     * Constructs an instance of <code>DeleteFoodCourtException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteFoodCourtException(String msg) {
        super(msg);
    }
}
