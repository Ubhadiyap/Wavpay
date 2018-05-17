package com.hbyundu.shop.vendor.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class SDFormatUtil
{

	public static String formatMoneyChina(double money)
	{
		return formatMoneyChina(new BigDecimal(money));
	}

	public static String formatMoneyChina(String money)
	{
		return formatMoneyChina(new BigDecimal(money));
	}

	public static String formatMoneyChina(BigDecimal bigDecimal)
	{
		if (bigDecimal != null)
		{
			try
			{
				return NumberFormat.getCurrencyInstance(Locale.CHINA).format(bigDecimal);
			} catch (Exception e)
			{
				return "";
			}
		} else
		{
			return "";
		}
	}

	public static String formatPercentChina(double number, int decimalNumber)
	{
		return formatPercentChina(new BigDecimal(number), decimalNumber);
	}

	public static String formatPercentChina(String number, int decimalNumber)
	{
		return formatPercentChina(new BigDecimal(number), decimalNumber);
	}

	public static String formatPercentChina(BigDecimal bigDecimal, int decimalNumber)
	{
		if (bigDecimal != null)
		{
			try
			{
				NumberFormat percent = NumberFormat.getPercentInstance(Locale.CHINA);
				percent.setMaximumFractionDigits(decimalNumber);
				return percent.format(bigDecimal);
			} catch (Exception e)
			{
				return "";
			}
		} else
		{
			return "";
		}
	}

	public static String formatNumberString(String formatString, int number)
	{
		if (formatString != null && formatString.length() > 0)
		{
			NumberFormat format = NumberFormat.getNumberInstance();
			format.setMaximumFractionDigits(number);
			try
			{
				return format.format(Double.valueOf(formatString));
			} catch (Exception e)
			{
				return null;
			}
			
		} else
		{
			return formatString;
		}
	}
	
	public static String formatNumberDouble(double formatDouble, int number)
	{
		return formatNumberString(String.valueOf(formatDouble), number);
	}

}
