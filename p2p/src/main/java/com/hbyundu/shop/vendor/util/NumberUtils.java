package com.hbyundu.shop.vendor.util;

/**
 * Created by apple on 16/7/13.
 */
public class NumberUtils {

    public static String formatRate(double rate) {
        return new java.text.DecimalFormat("#0.00").format(rate);
    }
}
