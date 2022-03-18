package com.barnacle.travel.util;

import java.text.NumberFormat;
import java.util.Locale;

public class Formatter {

    public static <T extends Number> String formatAsCurrency(T a) {
        try {
            NumberFormat cf = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
            cf.setMaximumFractionDigits(0);
            return cf.format(a);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(a);
            System.out.println("CANNOT FORMAT");
            return String.valueOf(a);
        }
    }
}
