package com.hbyundu.shop.vendor.util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by apple on 16/7/13.
 */
public class MoneyUtils {

    public static String formatMoney(double money, Locale locale) {
        NumberFormat formatter = NumberFormat.getNumberInstance(locale);
        formatter.setMaximumFractionDigits(2);
        return "RM " + formatter.format(money);
    }
}
