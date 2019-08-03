package com.koohpar.eram.tools;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahab on 6/24/2017.
 */

public class NumberFormatter {

    private static final String[] persianNumbers = { "۰" , "۱" , "۲" , "۳" , "۴" , "۵" ,"۶" ,"۷" , "۸", "۹" };
    private static int digitCount = 0;

    public static String format(long number){
        digitCount = 0 ;
        String result = "" ;
        while(number != 0){
            result = persianNumbers[(int) (number%10)] + result;
            number /= 10 ;
            digitCount++ ;
        }
        result = separator(result);
        return result;
    }


    public static String separator(String number) {
        StringBuilder result = new StringBuilder(number) ;
        int count = 0 ;
        for(int i = number.length()-1 ; i > 0 ; i--){
            count++ ;
            if(count % 3 == 0) {
                result.insert(i , ",");
            }
        }
        return result.toString() ;
    }

}
