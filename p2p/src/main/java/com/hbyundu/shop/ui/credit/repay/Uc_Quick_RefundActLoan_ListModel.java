package com.hbyundu.shop.ui.credit.repay;

public class Uc_Quick_RefundActLoan_ListModel {
    private String id = null;
    private String impose_day = null;
    private String status = null;
    private String repay_day = null;
    private String month_repay_money = null;
    private String true_repay_time = null;
    private String month_has_repay_money = null;
    private String month_manage_money = null;
    private String has_repay = null; // 1:已还款; 0:未还款
    private String impose_money = null;
    private String month_has_repay_money_all = null;
    private String month_need_all_repay_money = null;
    private String repay_day_format = null; // 还款日
    private String month_has_repay_money_all_format = null; // 已还金额
    private String month_need_all_repay_money_format = null; // 待还金额
    private String month_repay_money_format = null; // 待还本息
    private String month_manage_money_format = null; // 管理费
    private String impose_money_format = null; // 预期费
    private String status_format = null;

    private boolean isSelect = false;

    private boolean isCanSelect = false;

    public String getImpose_day() {
        return impose_day;
    }

    public void setImpose_day(String impose_day) {
        this.impose_day = impose_day;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRepay_day() {
        return repay_day;
    }

    public void setRepay_day(String repay_day) {
        this.repay_day = repay_day;
    }

    public String getMonth_repay_money() {
        return month_repay_money;
    }

    public void setMonth_repay_money(String month_repay_money) {
        this.month_repay_money = month_repay_money;
    }

    public String getTrue_repay_time() {
        return true_repay_time;
    }

    public void setTrue_repay_time(String true_repay_time) {
        this.true_repay_time = true_repay_time;
    }

    public String getMonth_has_repay_money() {
        return month_has_repay_money;
    }

    public void setMonth_has_repay_money(String month_has_repay_money) {
        this.month_has_repay_money = month_has_repay_money;
    }

    public String getMonth_manage_money() {
        return month_manage_money;
    }

    public void setMonth_manage_money(String month_manage_money) {
        this.month_manage_money = month_manage_money;
    }

    public String getHas_repay() {
        return has_repay;
    }

    public void setHas_repay(String has_repay) {
        this.has_repay = has_repay;
        if (has_repay != null) {
            if (has_repay.equals("0")) // 未还款
            {
                this.isCanSelect = true;
            } else {
                this.isCanSelect = false;
            }
        }
    }

    public String getImpose_money() {
        return impose_money;
    }

    public void setImpose_money(String impose_money) {
        this.impose_money = impose_money;
    }

    public String getMonth_has_repay_money_all() {
        return month_has_repay_money_all;
    }

    public void setMonth_has_repay_money_all(String month_has_repay_money_all) {
        this.month_has_repay_money_all = month_has_repay_money_all;
    }

    public String getMonth_need_all_repay_money() {
        return month_need_all_repay_money;
    }

    public void setMonth_need_all_repay_money(String month_need_all_repay_money) {
        this.month_need_all_repay_money = month_need_all_repay_money;
    }

    public String getRepay_day_format() {
        return repay_day_format;
    }

    public void setRepay_day_format(String repay_day_format) {
        this.repay_day_format = repay_day_format;
    }

    public String getMonth_has_repay_money_all_format() {
        return month_has_repay_money_all_format;
    }

    public void setMonth_has_repay_money_all_format(String month_has_repay_money_all_format) {
        this.month_has_repay_money_all_format = month_has_repay_money_all_format;
    }

    public String getMonth_need_all_repay_money_format() {
        return month_need_all_repay_money_format;
    }

    public void setMonth_need_all_repay_money_format(String month_need_all_repay_money_format) {
        this.month_need_all_repay_money_format = month_need_all_repay_money_format;
    }

    public String getMonth_repay_money_format() {
        return month_repay_money_format;
    }

    public void setMonth_repay_money_format(String month_repay_money_format) {
        this.month_repay_money_format = month_repay_money_format;
    }

    public String getMonth_manage_money_format() {
        return month_manage_money_format;
    }

    public void setMonth_manage_money_format(String month_manage_money_format) {
        this.month_manage_money_format = month_manage_money_format;
    }

    public String getImpose_money_format() {
        return impose_money_format;
    }

    public void setImpose_money_format(String impose_money_format) {
        this.impose_money_format = impose_money_format;
    }

    public String getStatus_format() {
        return status_format;
    }

    public void setStatus_format(String status_format) {
        this.status_format = status_format;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public boolean isCanSelect() {
        return isCanSelect;
    }

    public void setCanSelect(boolean isCanSelect) {
        this.isCanSelect = isCanSelect;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
