package com.example.booksys.pojo;


/**
 * # -*- coding:utf-8 -*- #
 * 作者:30671
 * 日期:2022年03月01日23时15分
 */

public class Borrow {
    private int rdId;
    private int bkId;
    private String dateBorrow;
    private String dateLendPlan;
    private String dateLendAct;
    private byte overdue;
    private byte isReturn;

    public int getRdId() {
        return rdId;
    }

    public void setRdId(int rdId) {
        this.rdId = rdId;
    }

    public int getBkId() {
        return bkId;
    }

    public void setBkId(int bkId) {
        this.bkId = bkId;
    }

    public String getDateBorrow() {
        return dateBorrow;
    }

    public void setDateBorrow(String dateBorrow) {
        this.dateBorrow = dateBorrow;
    }

    public String getDateLendPlan() {
        return dateLendPlan;
    }

    public void setDateLendPlan(String dateLendPlan) {
        this.dateLendPlan = dateLendPlan;
    }

    public String getDateLendAct() {
        return dateLendAct;
    }

    public void setDateLendAct(String dateLendAct) {
        this.dateLendAct = dateLendAct;
    }

    public byte getOverdue() {
        return overdue;
    }

    public void setOverdue(byte overdue) {
        this.overdue = overdue;
    }

    public byte getIsReturn() {
        return isReturn;
    }

    public void setIsReturn(byte isReturn) {
        this.isReturn = isReturn;
    }
}
