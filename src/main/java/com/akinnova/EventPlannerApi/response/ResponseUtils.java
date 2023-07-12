package com.akinnova.EventPlannerApi.response;

import java.util.Random;

public class ResponseUtils {
    public static final int OK = 200;
    public static final String OK_MESSAGE = "Request is valid.";
    public static final int CREATED = 201;
    public static final String CREATED_MESSAGE = "%s has been created.";
    public static final int ACCEPTED = 202;
    public static final String REQUEST_ACCEPTED = "Request has been accepted.";
    public static final int FOUND = 302;
    public static final String FOUND_MESSAGE = "%s was found!";

    //Method to generate unique identifier for objects
    public static String generateUniqueIdentifier(int len, String username){
        String invoiceCode = ""; //newly generated invoice-code will be stored in this variable
        char[] numChar = new char[len]; //Character array that will hold a maximum of 'len' characters
        Random random = new Random();
        int x = 0; //x will contain new random number generated

        for (int i = 0; i < len; i++){
            x = random.nextInt(1, 6); //random numbers generated will be from (1 - 6)
            numChar[i] = Integer.toString(x).toCharArray()[0];
        }
        //Invoice will be a combination of the first 3-characters of username with randomly generated digits
        invoiceCode = username.substring(0, 2) + new String(numChar);
        return invoiceCode;
    }
}
