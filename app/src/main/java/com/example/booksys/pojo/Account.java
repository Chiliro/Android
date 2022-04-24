package com.example.booksys.pojo;

public class Account {
    private String identification;
    private String password;
    private byte root;
    private String QQ;
    private int rdId;

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte getRoot() {
        return root;
    }

    public void setRoot(byte root) {
        this.root = root;
    }

    public String getQQ() {
        return QQ;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    public int getRdId() {
        return rdId;
    }

    public void setRdId(int rdId) {
        this.rdId = rdId;
    }
}
