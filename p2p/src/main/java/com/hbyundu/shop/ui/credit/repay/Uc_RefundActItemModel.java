package com.hbyundu.shop.ui.credit.repay;

import com.hbyundu.shop.vendor.util.SDFormatUtil;

public class Uc_RefundActItemModel
{

	private String id = null;
	private String name = null;// 标题
	private String sub_name = null;// 小标题
	private String borrow_amount = null;// 借款金额
	private String borrow_amount_format = null;
	private String rate = null;// 利率
	private String rate_foramt = null;
	private String repay_time = null; // 期限
	private String repay_time_type = null; // 0：天 ，其他的：月
	private String next_repay_time_format = null; // 下次还款日
	private String true_month_repay_money = null;// 月还款额
	private String need_money = null;// 罚息
	private String publis_time_format = null;// 已还清日期
	private String url = null;
	private String app_url = null;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getSub_name()
	{
		return sub_name;
	}

	public void setSub_name(String sub_name)
	{
		this.sub_name = sub_name;
	}

	public String getBorrow_amount()
	{
		return borrow_amount;
	}

	public void setBorrow_amount(String borrow_amount)
	{
		this.borrow_amount = borrow_amount;
	}

	public String getBorrow_amount_format()
	{
		return borrow_amount_format;
	}

	public void setBorrow_amount_format(String borrow_amount_format)
	{
		this.borrow_amount_format = borrow_amount_format;
	}

	public String getRate()
	{
		return rate;
	}

	public void setRate(String rate)
	{
		this.rate = rate;
	}

	public String getRate_foramt()
	{
		return rate_foramt;
	}

	public void setRate_foramt(String rate_foramt)
	{
		this.rate_foramt = rate_foramt;
	}

	public String getRepay_time()
	{
		return repay_time;
	}

	public void setRepay_time(String repay_time)
	{
		this.repay_time = repay_time;
	}

	public String getRepay_time_type()
	{
		return repay_time_type;
	}

	public void setRepay_time_type(String repay_time_type)
	{
		this.repay_time_type = repay_time_type;
	}

	public String getNext_repay_time_format()
	{
		return next_repay_time_format;
	}

	public void setNext_repay_time_format(String next_repay_time_format)
	{
		this.next_repay_time_format = next_repay_time_format;
	}

	public String getTrue_month_repay_money()
	{
		return true_month_repay_money;
	}

	public void setTrue_month_repay_money(String true_month_repay_money)
	{
		this.true_month_repay_money = true_month_repay_money;
	}

	public String getNeed_money()
	{
		return need_money;
	}

	public void setNeed_money(String need_money)
	{
		this.need_money = need_money;
	}

	public String getPublis_time_format()
	{
		return publis_time_format;
	}

	public void setPublis_time_format(String publis_time_format)
	{
		this.publis_time_format = publis_time_format;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getApp_url()
	{
		return app_url;
	}

	public void setApp_url(String app_url)
	{
		this.app_url = app_url;
	}

}
