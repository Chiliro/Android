package com.example.booksys.pojo;

public class ReaderType {

    private int rdType;
    private String rdTypeName;
    private int canLendQty;
    private int canLendDay;



    public String getRdTypeName() {
        return rdTypeName;
    }

    public void setRdTypeName(String rdTypeName) {
        this.rdTypeName = rdTypeName;
    }

    public int getRdType() {
        return rdType;
    }

    public void setRdType(int rdType) {
        this.rdType = rdType;
    }

    public int getCanLendQty() {
        return canLendQty;
    }

    public void setCanLendQty(int canLendQty) {
        this.canLendQty = canLendQty;
    }

    public int getCanLendDay() {
        return canLendDay;
    }

    public void setCanLendDay(int canLendDay) {
        this.canLendDay = canLendDay;
    }

    @Override
    public String toString() {
        return "ReaderType{" +
                "rdType=" + rdType +
                ", rdTypeName='" + rdTypeName + '\'' +
                ", canLendQty=" + canLendQty +
                ", canLendDay=" + canLendDay +
                '}';
    }
}
