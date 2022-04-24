package com.example.booksys.Utils;

public class NetEnum {


    /**
     * 服务器ip
     */
    public static final String serverUrl = "http://192.168.43.50:8080";



    /**
     * Account的servlet
     */
    public static final String loginServletUrl = "/LoginController";        //登录
    public static final String signServletUrl = "/SignController";          //注册
    public static final String LogoffServletUrl = "/LogoffController";      //注销


    /**
     * Book的servlet
     */
    public static final String QueryBookServletUrl = "/QueryBookController";        //查寻
    public static final String LendBookServletUrl = "/LendBookController";          //借书
    public static final String QueryRdBookServletUrl = "/QueryRdBookController";    //查询未还书籍
    public static final String ReturnBookServletUrl = "/ReturnBookController";      //还书


    /**
     * My的修改个人信息
     */
    public static final String ChangeAccountServletUrl = "/ChangeAccountController";   //查寻


    /**
     * My界面的GitHub跳转
     */
    public static final String GitHubUrl = "https://github.com/Eiard/ytz_booksys";



}
