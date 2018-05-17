package com.hbyundu.shop.ui.credit.constant;

public class Constant
{

	public static final String DOWN_LOAD_DIR_NAME = "fanwe";

	public static final class RequestDataType
	{
		public static final int BASE64 = 0;
		public static final int REQUEST = 1;
	}

	public static final class ResponseDataType
	{
		public static final int BASE64 = 0;
		public static final int JSON = 1;
		public static final int ARRAY = 2;
	}

	public static final class DealStatus
	{
		/** 等待材料 */
		public static final String WAIT = "0";
		/** 借款中 */
		public static final String LOANING = "1";
		/** 满标 */
		public static final String FULL = "2";
		/** 流标 */
		public static final String FAIL = "3";
		/** 还款中 */
		public static final String REPAYMENTING = "4";
		/** 已还清 */
		public static final String REPAYMENTED = "5";
	}

	public static final class DealStatusString
	{
		/** 等待材料 */
		public static final String WAIT = "等待材料";
		/** 借款中 */
		public static final String LOANING = "借款中";
		/** 满标 */
		public static final String FULL = "满标";
		/** 流标 */
		public static final String FAIL = "流标";
		/** 还款中 */
		public static final String REPAYMENTING = "还款中";
		/** 已还清 */
		public static final String REPAYMENTED = "已还清";
	}

	public static final class DealsActSearchConditionCid
	{
		/** 全部借款列表 */
		public static final String ALL = "0";
	}

	public static final class DealsActSearchConditionDealStatus
	{
		/** 等待材料 */
		public static final String ALL = "0";
		/** 借款中 */
		public static final String LOANING = "1";
		/** 满标 */
		public static final String FULL = "2";
		/** 流标 */
		public static final String FAIL = "3";
		/** 还款中 */
		public static final String REPAYMENTING = "4";
		/** 已还清 */
		public static final String REPAYMENTED = "5";
	}

	public static final class DealsActSearchConditionLevel
	{
		/** 全部等级 */
		public static final String ALL = "0";
		/** E等级 */
		public static final String E = "2";
		/** D等级 */
		public static final String D = "3";
		/** C等级 */
		public static final String C = "4";
		/** B等级 */
		public static final String B = "5";

	}

	public static final class DealsActSearchConditionInterest
	{
		/** 全部利率 */
		public static final String ALL = "0";
		/** 10%以上 */
		public static final String TEN = "10";
		/** 12%以上 */
		public static final String TWELVE = "12";
		/** 15%以上 */
		public static final String FIFTEEN = "15";
		/** 18%以上 */
		public static final String EIGHTEEN = "18";
	}

	public static final class DealsActSearchConditionMonths
	{
		/** 全部期限 */
		public static final String ALL = "0";
		/** 12个月内 */
		public static final String TWELVE = "12";
		/** 18个月内 */
		public static final String EIGHTEEN = "18";
	}

	public static final class DealsActSearchConditionLeftTime
	{
		/** 全部剩余时间 */
		public static final String ALL = "0";
		/** 1天内 */
		public static final String ONE = "1";
		/** 3天内 */
		public static final String THREE = "3";
	}

	public static final class TransferActSearchConditionCid
	{
		/** 全部借款列表 */
		public static final String ALL = "0";
		/** 最近成功借款 */
		public static final String LAST = "last";
	}

	public static final class TransferActSearchConditionLevel
	{
		/** 全部等级 */
		public static final String ALL = "0";
		/** E等级 */
		public static final String E = "2";
		/** D等级 */
		public static final String D = "3";
		/** C等级 */
		public static final String C = "4";
		/** B等级 */
		public static final String B = "5";

	}

	public static final class TransferActSearchConditionInterest
	{
		/** 全部利率 */
		public static final String ALL = "0";
		/** 10%以上 */
		public static final String TEN = "10";
		/** 12%以上 */
		public static final String TWELVE = "12";
		/** 15%以上 */
		public static final String FIFTEEN = "15";
		/** 18%以上 */
		public static final String EIGHTEEN = "18";
	}

	public static final class TransferActSearchConditionMonths
	{
		/** 全部期限 */
		public static final String ALL = "0";
		/** 12个月内 */
		public static final String TWELVE = "12";
		/** 18个月内 */
		public static final String EIGHTEEN = "18";
	}

	public static final class TransferActSearchConditionLeftTime
	{
		/** 全部剩余时间 */
		public static final String ALL = "0";
		/** 1天内 */
		public static final String ONE = "1";
		/** 3天内 */
		public static final String THREE = "3";
	}

	/**
	 * 还款方式
	 * 
	 */
	public static final class LoanType
	{
		/** 等额本息 */
		public static final String ZERO = "0";
		/** 付息还本 */
		public static final String ONE = "1";
		/** 到期还本息 */
		public static final String TWO = "2";
	}

	/**
	 * 还款方式字符串
	 * 
	 */
	public static final class LoanTypeString
	{
		/** 等额本息 */
		public static final String ZERO = "等额本息";
		/** 付息还本 */
		public static final String ONE = "付息还本";
		/** 到期还本息 */
		public static final String TWO = "到期还本息";
	}

	/**
	 * 信用等级
	 * 
	 */
	public static final class RiskRank
	{
		/** 低 */
		public static final String ZERO = "0";
		/** 中 */
		public static final String ONE = "1";
		/** 高 */
		public static final String TWO = "2";
	}

	/**
	 * 信用等级字符串
	 * 
	 */
	public static final class RiskRankString
	{
		/** 等额本息 */
		public static final String ZERO = "低";
		/** 付息还本 */
		public static final String ONE = "中";
		/** 到期还本息 */
		public static final String TWO = "高";
	}

	public static final class SlidingMenuItem
	{
		public static final int MY_ACCOUNT = 0;
		public static final int HOME = 1;
		public static final int BORROW_INVEST = 2;
		public static final int BOND_TRANSFER = 3;
		public static final int ARTICLE = 4;
		public static final int MORE_SETTING = 5;
	}

	/** 债券转让（0：已撤销 1：转让中 null：可转让） */
	public static final class Tras_Status
	{

		public static final int TRAS_STATUS_0 = 0;

		public static final int TRAS_STATUS_1 = 1;
	}

	/** tras_status_op 0:无操作; 1:转让;2:还款完毕,无法转让;3:逾期还款,无法转让;4:重转让;5:详情;6:撤销 */
	public static final class Tras_Status_Op
	{
		public static final int Tras_Status_Op_0 = 0;

		public static final int Tras_Status_Op_1 = 1;

		public static final int Tras_Status_Op_2 = 2;

		public static final int Tras_Status_Op_3 = 3;

		public static final int Tras_Status_Op_4 = 4;

		public static final int Tras_Status_Op_5 = 5;

		public static final int Tras_Status_Op_6 = 6;
	}

	public static final class MyInvestConditionMode
	{
		/** 我的投资 */
		public static final String INDEX = "index";
		/** 招标中借款 */
		public static final String INVITE = "invite";
		/** 回收中借款 */
		public static final String ING = "ing";
		/** 已回收借款 */
		public static final String OVER = "over";
		/** 我的坏账 */
		public static final String BAD = "bad";
	}

	/**
	 * [type] => (1:文章 2:url连接址) [data] => (type = 1:文章ID;type =2:url链接)
	 * [open_url_type] =>(type = 2时，有效；0:使用webview;1:使用系统浏览器)
	 */
	public static final class Init_Adv_List
	{
		public static final int Init_Adv_List_Type_1 = 1;

		public static final int Init_Adv_List_Type_2 = 2;

		public static final int Init_Adv_List_Open_Url_Type_0 = 0;

		public static final int Init_Adv_List_Open_Url_Type_1 = 1;
	}

	public static final class PushType
	{
		public static final int NORMAL = 1;
		public static final int PROJECT_ID = 2;
		public static final int ARTICLE_ID = 3;
		public static final int URL = 4;
	}

}
