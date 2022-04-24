package com.example.booksys;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.TypeReference;
import com.example.booksys.Utils.FastJsonUtils;
import com.example.booksys.Utils.GsonUtils;
import com.example.booksys.Utils.NetEnum;

import com.example.booksys.Utils.NetUtil;
import com.example.booksys.android.user.UserActivity;
import com.example.booksys.pojo.Account;
import com.example.booksys.pojo.Reader;
import com.example.booksys.pojo.ReaderType;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Objects;

public class Login extends AppCompatActivity {

    //使用SharedPreferences进行读取
    private SharedPreferences pref;
    //使用SharedPreferences.Editor进行存储
    private SharedPreferences.Editor editor;

    private EditText log_NickName, log_Password;
    private Button login, register,back;

    private CheckBox rememberPassword;
    private String logNickName, logPassword;


    String loginUrlServlet = NetEnum.loginServletUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

    }

    private void init() {

        /**
         * 存储信息的的SharedPreferences初始化
         */
        pref= getSharedPreferences("data",MODE_PRIVATE);
        editor=pref.edit();




        log_NickName = findViewById(R.id.log_name);
        log_Password = findViewById(R.id.log_password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.log_register);
        back = findViewById(R.id.log_back);
        rememberPassword = findViewById(R.id.remember);


        /**
         * 如果存储密码，则输出到 EditText
         * 没有则不管
         */

        String name = pref.getString("name", "");
        String password = pref.getString("password", "");
        if(!"".equals(name) && !"".equals(password)) {

            log_NickName.setText(name);
            log_Password.setText(password);
            rememberPassword.setChecked(true);
        }


        /**
         * 登录按钮添加监听
         */
        login.setOnClickListener(new View.OnClickListener() {
            String response;
            @Override
            public void onClick(View view) {
                getEditData();
                if (TextUtils.isEmpty(logNickName)) {
                    Toast.makeText(Login.this, "请输入昵称", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(logPassword)) {
                    Toast.makeText(Login.this, "请输入密码", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String data = "identification=" + logNickName
                                +"&password="+URLEncoder.encode(logPassword, "UTF-8");

                        NetUtil netUtil = new NetUtil(data, loginUrlServlet,"POST");
                        netUtil.start();
                        netUtil.join();//线程同步
                        response = netUtil.getResponse();

                        Map<String, Object> map = FastJsonUtils.strToJavaBeanMap(response,
                                new TypeReference<Map<String, Object>>(){});

                        int integer = (int)map.get("status");
                        //登录成功
                        if (1 == integer) {

                            /**
                             * 是否选择记住密码
                             */
                            if(rememberPassword.isChecked()){
                                /**
                                 * 先清空缓存，重新缓存
                                 */
                                editor.clear();
                                editor.putString("name",logNickName);
                                editor.putString("password",logPassword);
                            }else{
                                editor.remove("name");
                                editor.remove("password");
                            }

                            /**
                             * 存储获取的数据到手机上
                             */
                            storageResponseData(map);
                            editor.commit();

                            Intent intent = new Intent(Login.this, UserActivity.class);
                            startActivity(intent);
                            Toast.makeText(Login.this, "登录成功", Toast.LENGTH_SHORT).show();
                            return;
                        }else if(0 == integer){
                            Toast.makeText(Login.this, "管理员请在后台服务器登录", Toast.LENGTH_SHORT).show();
                        }else if(2 == integer){
                            Toast.makeText(Login.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                        }else if(15 == integer){
                            Toast.makeText(Login.this, "异常", Toast.LENGTH_SHORT).show();
                        }
                        /**
                         * 登录失败，清除缓存
                         */
                        log_NickName.setText("");
                        log_Password.setText("");
                        editor.clear();
                        editor.commit();
                    } catch (Exception  e) {
                        e.printStackTrace();
                    }
                }
            }

        });


        /**
         * 注册按钮添加监听注册
         */
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        /**
         * 设置按钮监听back
         */
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void getEditData() {
        logNickName = log_NickName.getText().toString().trim();
        logPassword = log_Password.getText().toString().trim();
    }

    private void storageResponseData(Map<String,Object> map){

        //使得对象不为空，使用Objects.requireNonNull( )方法对对象进行判断
        ReaderType readerType = FastJsonUtils.strToJavaBean(map.get("readerType").toString(),new TypeReference<ReaderType>(){});

        Reader reader = FastJsonUtils.strToJavaBean(map.get("reader").toString(),new TypeReference<Reader>(){});

        Account account = FastJsonUtils.strToJavaBean(map.get("data").toString(),new TypeReference<Account>(){}) ;

        editor.putString("identification",logNickName);                             //用户名
        editor.putString("logPassword",logPassword);                                //密码(给修改用户信息时使用)

        editor.putInt("rdId",reader.getRdId());                                     //读者id
        editor.putString("readerName",reader.getRdName());                          //读者姓名
        editor.putInt("alreadyLendBookNum",reader.getRdBorrowQty());                //已借数量
        editor.putString("rdDept",reader.getRdDept());                              //读者学院

        editor.putString("readerTypeName",readerType.getRdTypeName());              //读者类别名
        editor.putInt("totalCanLendNum",readerType.getCanLendQty());                //总共可借书数量
        editor.putInt("CanLendNumDay",readerType.getCanLendDay());                  //可接天数

        editor.putString("QQ",account.getQQ());                                     //可接天数



    }



}