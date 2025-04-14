package com.bank.movement.application.util;


import java.util.Random;

public class Utils {

     public static String generateAccountNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            int randomDigit = random.nextInt(10);
            sb.append(randomDigit);
        }

        return sb.toString();
    }
}
