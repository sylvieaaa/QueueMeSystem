/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.common;

import java.security.SecureRandom;

/**
 *
 * @author User
 */
public class RandomGenerator {

    private SecureRandom random = new SecureRandom();

    private final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    private final String NUMERIC = "0123456789";
    private final String SPECIAL_CHARS = "!@#$%^&*_=+-/";
    
    private int length = 8;

    public String generatePassword() {
        String dic = ALPHA_CAPS + ALPHA + NUMERIC + SPECIAL_CHARS;
        String result = "";
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(dic.length());
            result += dic.charAt(index);
        }
        return result;
    }
}
