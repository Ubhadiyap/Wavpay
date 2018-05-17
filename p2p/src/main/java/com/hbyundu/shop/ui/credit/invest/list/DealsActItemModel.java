package com.hbyundu.shop.ui.credit.invest.list;

import android.content.Context;
import android.text.TextUtils;

import java.io.Serializable;
import java.text.DecimalFormat;

import com.hbyundu.shop.R;
import com.hbyundu.shop.application.App;
import com.hbyundu.shop.ui.credit.constant.Constant.DealStatus;
import com.hbyundu.shop.ui.credit.constant.Constant.DealStatusString;
import com.hbyundu.shop.ui.credit.constant.Constant.LoanType;
import com.hbyundu.shop.ui.credit.constant.Constant.LoanTypeString;
import com.hbyundu.shop.ui.credit.constant.Constant.RiskRank;
import com.hbyundu.shop.ui.credit.constant.Constant.RiskRankString;

public class DealsActItemModel implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id = null; // 借款编号
	private String user_id = null;
	private String deal_status = null; // 状态
	private String name = null; // 名称
	private String sub_name = null; // 名称(短)
	private String borrow_amount_format = null; // 借款金额已经格式化

	private String rate = null; // 年利率
	private String progress_point = null; // 进度
	private String progress_point_format = null;

	private String repay_time = null; // 借款期限
	private String repay_time_type = null; // 0：天 ，其他的：月
	private String repay_time_type_format = null;
	private String risk_rank = null; // 风险等级 0:低 1：中 2：高
	private String risk_rank_format = null;
	private String rate_foramt = null; // 年利率已经格式化
	private String min_loan_money = null; // 起投金额
	private String min_loan_money_format = null; // 起投金额
	private String loantype = null; // 还款方式 0:等额本息 1:付息还本 2:到期还本息
	private String loantype_format = null;
	private String is_faved = null; // 大于0为已关注
	private String remain_time_format = null; // 剩余时间
	private String need_money = null; // 可以投金额
	private String canUse = null;

	private String app_url = null;

	public int isDelete;
	public String deleteMsg;
	public int publishWait;
	public int customStatus;

	// ================add
	private boolean isSelect = false;

	private String deal_status_format_string = null;
	private boolean isLoaning = false; // 是否是借款中
	private String dealId;

	public boolean isLoaning()
	{
		return isLoaning;
	}

	public void setLoaning(boolean isLoaning)
	{
		this.isLoaning = isLoaning;
	}

	public String getDeal_status_format_string()
	{
		return deal_status_format_string;
	}

	public void setDeal_status_format_string(String deal_status_format_string)
	{
		this.deal_status_format_string = deal_status_format_string;
	}

	public boolean isSelect()
	{
		return isSelect;
	}

	public void setSelect(boolean isSelect)
	{
		this.isSelect = isSelect;
	}

	public String getProgress_point_format()
	{
		return progress_point_format;
	}

	public void setProgress_point_format(String progress_point_format)
	{
		this.progress_point_format = progress_point_format;
	}

	public String getRisk_rank_format()
	{
		return risk_rank_format;
	}

	public void setRisk_rank_format(String risk_rank_format)
	{
		this.risk_rank_format = risk_rank_format;
	}

	public String getLoantype_format()
	{
		return loantype_format;
	}

	public void setLoantype_format(String loantype_format)
	{
		this.loantype_format = loantype_format;
	}

	public String getMin_loan_money_format()
	{
		return min_loan_money_format;
	}

	public void setMin_loan_money_format(String min_loan_money_format)
	{
		this.min_loan_money_format = min_loan_money_format;
	}

	public String getRepay_time_type_format()
	{
		return repay_time_type_format;
	}

	public void setRepay_time_type_format(String repay_time_type_format)
	{
		this.repay_time_type_format = repay_time_type_format;
	}

	public String getUser_id()
	{
		return user_id;
	}

	public void setUser_id(String user_id)
	{
		this.user_id = user_id;
	}

	public String getApp_url()
	{
		return app_url;
	}

	public void setApp_url(String app_url)
	{
		this.app_url = app_url;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getDeal_status()
	{
		return deal_status;
	}

	public void setDeal_status(Context context, String deal_status)
	{
		this.deal_status = deal_status;
		if (!TextUtils.isEmpty(deal_status))
		{
			if (deal_status.equals(DealStatus.FAIL))
			{
				this.deal_status_format_string = context.getString(R.string.deal_status_fail);// DealStatusString.FAIL;
			} else if (deal_status.equals(DealStatus.FULL))
			{
				this.deal_status_format_string = context.getString(R.string.deal_status_full);//DealStatusString.FULL;
			} else if (deal_status.equals(DealStatus.LOANING)) // 借款中
			{
				this.deal_status_format_string = context.getString(R.string.deal_status_loading);//DealStatusString.LOANING;
				this.isLoaning = true;
			} else if (deal_status.equals(DealStatus.REPAYMENTED))
			{
				this.deal_status_format_string = context.getString(R.string.deal_status_repaymented);//DealStatusString.REPAYMENTED;
			} else if (deal_status.equals(DealStatus.REPAYMENTING))
			{
				this.deal_status_format_string = context.getString(R.string.deal_status_repaymenting);//DealStatusString.REPAYMENTING;
			} else if (deal_status.equals(DealStatus.WAIT))
			{
				this.deal_status_format_string = context.getString(R.string.deal_status_wait);//DealStatusString.WAIT;
			} else
			{
				this.deal_status_format_string = App.getApplication().getString(R.string.not_found);
			}
		}else 
		{
			this.deal_status_format_string = App.getApplication().getString(R.string.not_found);
		}
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

	public String getProgress_point()
	{
		return progress_point;
	}

	public void setProgress_point(String progress_point)
	{
		this.progress_point = progress_point;
		if (progress_point != null)
		{
			try
			{
				this.progress_point_format = new DecimalFormat("#0.00").format(Double.valueOf(progress_point)) + "%";
			} catch (Exception e)
			{
			}
		}
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

	public void setRepay_time_type(Context context, String repay_time_type)
	{
		this.repay_time_type = repay_time_type;
		if (repay_time_type != null)
		{
			if (repay_time_type.equals("0"))
			{
				this.repay_time_type_format = context.getString(R.string.days);
			} else
			{
				this.repay_time_type_format = context.getString(R.string.months);
			}
		}

	}

	public String getRisk_rank()
	{
		return risk_rank;
	}

	public void setRisk_rank(String risk_rank)
	{
		this.risk_rank = risk_rank;
		if (risk_rank != null)
		{
			if (risk_rank.equals(RiskRank.ZERO))
			{
				this.risk_rank_format = RiskRankString.ZERO;
			} else if (risk_rank.equals(RiskRank.ONE))
			{
				this.risk_rank_format = RiskRankString.ONE;
			} else if (risk_rank.equals(RiskRank.TWO))
			{
				this.risk_rank_format = RiskRankString.TWO;
			}
		}
	}

	public String getRate_foramt()
	{
		return rate_foramt;
	}

	public void setRate_foramt(String rate_foramt)
	{
		this.rate_foramt = rate_foramt;
		if (rate_foramt != null)
		{
			this.rate_foramt = rate_foramt + "%";
		}

	}

	public String getMin_loan_money()
	{
		return min_loan_money;
	}

	public void setMin_loan_money(String min_loan_money)
	{
		this.min_loan_money = min_loan_money;
		if (this.min_loan_money_format == null && !TextUtils.isEmpty(min_loan_money))
		{
			min_loan_money_format = "￥" + min_loan_money;
		}
	}

	public String getLoantype()
	{
		return loantype;
	}

	public void setLoantype(Context context, String loantype)
	{
		this.loantype = loantype;
		if (loantype != null)
		{
			if (loantype.equals(LoanType.ZERO))
			{
				this.loantype_format = context.getString(R.string.principal_and_interest);// LoanTypeString.ZERO;
			} else if (loantype.equals(LoanType.ONE))
			{
				this.loantype_format = context.getString(R.string.interest_bearing);//LoanTypeString.ONE;
			} else if (loantype.equals(LoanType.TWO))
			{
				this.loantype_format = context.getString(R.string.due_principal_and_interest_due);//LoanTypeString.TWO;
			}
		}
	}

	public String getIs_faved()
	{
		return is_faved;
	}

	public void setIs_faved(String is_faved)
	{
		this.is_faved = is_faved;
	}

	public String getRemain_time_format()
	{
		return remain_time_format;
	}

	public void setRemain_time_format(String remain_time_format)
	{
		this.remain_time_format = remain_time_format;
	}

	public String getNeed_money()
	{
		return need_money;
	}

	public void setNeed_money(String need_money)
	{
		this.need_money = need_money;
	}


	public String getCanUse()
	{
		return canUse;
	}

	public void setCanUse(String canUse)
	{
		this.canUse = canUse;
	}

	public void setDealId(String dealId) {
		this.dealId = dealId;
	}

	public String getDealId() {
		return dealId;
	}
}
