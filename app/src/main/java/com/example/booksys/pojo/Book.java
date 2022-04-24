package com.example.booksys.pojo;

import java.io.Serializable;

public class Book implements Serializable {


    private int bkId;
    private String bkName;
    private String bkAuthor;
    private String bkPress;
    private double bkPrice;
    private int bkNum;
    private byte bkState;
    private String bkImageUrl;




    public boolean isCheck;  //该属性主要标志CheckBox是否选中

    public int getBkId() {
        return bkId;
    }

    public void setBkId(int bkId) {
        this.bkId = bkId;
    }

    public String getBkName() {
        return bkName;
    }

    public void setBkName(String bkName) {
        this.bkName = bkName;
    }

    public String getBkAuthor() {
        return bkAuthor;
    }

    public void setBkAuthor(String bkAuthor) {
        this.bkAuthor = bkAuthor;
    }

    public String getBkPress() {
        return bkPress;
    }

    public void setBkPress(String bkPress) {
        this.bkPress = bkPress;
    }

    public double getBkPrice() {
        return bkPrice;
    }

    public void setBkPrice(double bkPrice) {
        this.bkPrice = bkPrice;
    }

    public int getBkNum() {
        return bkNum;
    }

    public void setBkNum(int bkNum) {
        this.bkNum = bkNum;
    }

    public byte getBkState() {
        return bkState;
    }

    public void setBkState(byte bkState) {
        this.bkState = bkState;
    }

    public String getBkImageUrl() {
        return bkImageUrl;
    }

    public void setBkImageUrl(String bkImageUrl) {
        this.bkImageUrl = bkImageUrl;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bkId=" + bkId +
                ", bkName='" + bkName + '\'' +
                ", bkAuthor='" + bkAuthor + '\'' +
                ", bkPress='" + bkPress + '\'' +
                ", bkPrice=" + bkPrice +
                ", bkNum=" + bkNum +
                ", bkState=" + bkState +
                ", bkImageUrl='" + bkImageUrl + '\'' +
                ", isCheck=" + isCheck +
                '}';
    }
}
