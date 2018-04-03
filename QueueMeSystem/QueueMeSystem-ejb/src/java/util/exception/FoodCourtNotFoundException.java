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
public class FoodCourtNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>FoodCourtNotFoundException</code> without
     * detail message.
     */
    public FoodCourtNotFoundException() {
    }

    /**
     * Constructs an instance of <code>FoodCourtNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public FoodCourtNotFoundException(String msg) {
        super(msg);
    }
}
