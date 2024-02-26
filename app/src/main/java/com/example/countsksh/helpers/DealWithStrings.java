package com.example.countsksh.helpers;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DealWithStrings {

    /**
     * convert numbers into money text
     * @param value double
     * @return String
     */
    public static String formatMoney(Double value){


        DecimalFormat formatter=(DecimalFormat) NumberFormat.getCurrencyInstance(new Locale("ar","YE"));
        return formatter.format(value);
    }

    /**
     * convert numeric text to numbers
     * @param value String
     * @return double
     */
    public static double formatDouble(String value){
        try {

        char firstChar=value.charAt(0);
        if(firstChar>=0 && firstChar<=9){


        String f =value.replaceAll("[^\\d.]","");

        return Double.parseDouble(f);}
        else {
            NumberFormat format=NumberFormat.getNumberInstance(new Locale("ar","YE"));
            Number number=format.parse(value);
            return number.doubleValue();
        }
        }catch (Exception e){

        }
        return 0.0;
    }


    /**
     * get the date in "yyyy.MM.dd" format
     * no params
     * @return String
     */
    public static String formatDate(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy.MM.dd");
         Calendar calendar=Calendar.getInstance();
         String date=simpleDateFormat.format(calendar.getTime()).toString();
        return date;
    }

}
