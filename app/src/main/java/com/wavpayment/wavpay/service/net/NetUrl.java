package com.wavpayment.wavpay.service.net;

/**
 * 接口
 * Created by Administrator on 2018/2/10.
 */

public class NetUrl {
    public static final String KEY = "appUserLogin/key.html";//获取公密 post
    public static final String CODE = "codes/code.html";//获取验证码 post

    public static final String REGISTER = "appUserLogin/register.html";//注册 post
    public static final String ACCOUNTS = "appUserLogin/accounts.html";//取验账户 get
    public static final String VERIFYCODEFORREGISTER = "appUserLogin/verifyCodeForRegister.html";//注册-手机验证码验证 get

    public static final String LOGIN = "appUserLogin/login.html";//登录 post
    public static final String LOGINOUT = "appUserLogin/login.html";//退出 get
    public static final String PASSWORD = "appUserHome/password.html";//修改密码 post
    public static final String INFO = "appUserHome/info.html";//获取用户信息 get
    public static final String UPDATE = "appUserHome/update.html";//更新用户信息 post
    public static final String PHOTO = "appUserHome/photo.html";//更新头像 post

    public static final String ORGLIST = "topupOrg/orgList.html";//获取缴费单位、机构列表 get
    public static final String ORG = "topupOrg/org.html";//称获取公司详情信息 get

    public static final String UNIFIEDORDER = "bills/unifiedOrder.html";//支付 post
    public static final String AUTHORIZATION = "bills/authorization.html";//支付授权 post
    public static final String COLLECTIONQRCODE = "bills/collectionQrCode.html";//获取收钱码 get
    public static final String PAYMENTQRCODE = "bills/paymentQrCode.html";//获取付款码 get
    public static final String QUERYBILLS = "bills/queryBills.html";//查询历史订单 post
    public static final String QUERYBILLSBYID = "bills/queryBillsById.html";//根据订单id查账单 get
    public static final String RECENTTRANSFER = "bills/recentTransfer.html";//获取最近转账记录 get
    public static final String DELRECENTTRANSFER = "bills/delRecentTransfer.html";//删除单条最近转账记录 get


    public static final String BANKCODES = "banks/bankCodes.html";//获取银行代码列表 get
    public static final String PHONECODE = "banks/phoneCode.html";//校验银行预留手机验证码 get
    public static final String ADDCARD = "banks/addCard.html";//添加银行卡 post
    public static final String CARDS = "banks/cards.html";//获取用户银行卡列表 get
    public static final String CARD = "banks/card.html";//获取用户单张银行卡信息 get
    public static final String DELCARD = "banks/delCard.html";//删除银行卡 post
    public static final String VERIFYCARDANDNAME = "banks/verifyCardAndName.html";//校验银行卡和姓名是否匹配 post


    public static final String NOREADBILLS = "message/noReadBills.html";//加载个人未读账单 post
    public static final String UPDATEBILLREADSTATUS = "message/updateBillReadStatus.html";//标记账单 post
    public static final String APPVERSION = "message/appVersion.html";//标记账单 get

    public static final String SENDDEVICEID = "push/sendDeviceId.html";//发送推送配置信息 post



}
