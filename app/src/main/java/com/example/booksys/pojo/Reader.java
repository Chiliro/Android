package com.example.booksys.pojo;

public class Reader {
    private int rdId;
    private int rdType;
    private String rdName;
    private String rdDept;
    private int rdBorrowQty;
    private byte rdState;

    public int getRdId() {
        return rdId;
    }

    public void setRdId(int rdId) {
        this.rdId = rdId;
    }

    public int getRdType() {
        return rdType;
    }

    public void setRdType(int rdType) {
        this.rdType = rdType;
    }

    public String getRdName() {
        return rdName;
    }

    public void setRdName(String rdName) {
        this.rdName = rdName;
    }

    public String getRdDept() {
        return rdDept;
    }

    public void setRdDept(String rdDept) {
        this.rdDept = rdDept;
    }

    public int getRdBorrowQty() {
        return rdBorrowQty;
    }

    public void setRdBorrowQty(int rdBorrowQty) {
        this.rdBorrowQty = rdBorrowQty;
    }

    public byte getRdState() {
        return rdState;
    }

    public void setRdState(byte rdState) {
        this.rdState = rdState;
    }
}
