package com.example.booksys;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.booksys.Utils.FastJsonUtils;
import com.example.booksys.Utils.GsonUtils;
import com.example.booksys.Utils.NetEnum;
import com.example.booksys.Utils.NetUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Register extends AppCompatActivity{

    String signUrlServlet = NetEnum.signServletUrl;     //注册servlet的url

    String response;        //返回值

    private EditText register_NickName, register_Password, register_QQ, register_ID, register_Name;
    private Button register, back;
    String registerNickName, registerPassword, registerQQ, registerID, registerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
    }

    private void init() {
        register_NickName = findViewById(R.id.register_nickname);
        register_Password = findViewById(R.id.register_password);
        register_QQ = findViewById(R.id.register_qq);
        register_ID = findViewById(R.id.register_id);
        register_Name = findViewById(R.id.register_name);

        register = findViewById(R.id.register);
        back = findViewById(R.id.register_back);


        /**
         * 按钮监听事件
         */
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                if (TextUtils.isEmpty(registerNickName)) {
                    Toast.makeText(Register.this, "请输入昵称", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(registerPassword)) {
                    Toast.makeText(Register.this, "请输入密码", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(registerQQ)) {
                    Toast.makeText(Register.this, "请输入QQ", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(registerID)) {
                    Toast.makeText(Register.this, "请输入ID", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(registerName)) {
                    Toast.makeText(Register.this, "请输入姓名", Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        String data = "identification=" + registerNickName
                                    +"&password="+ registerPassword
                                    +"&QQ="+registerQQ
                                    +"&rdId="+registerID
                                    +"&rdName="+URLEncoder.encode(registerName,"utf-8");

                        NetUtil netUtil = new NetUtil(data, signUrlServlet,"POST");
                        netUtil.start();
                        netUtil.join();//线程同步
                        response = netUtil.getResponse();

                        Map<String, Object> map = GsonUtils.strToJavaBeanMaps(response);

                        Integer integer = GsonUtils.strToJavaBean(map.get("status").toString(), Integer.class);

                        if(6 == integer){
                            Intent intent = new Intent(Register.this,Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);      //会清除该进程空间的所有Activity
                            startActivity(intent);
                            Toast.makeText(Register.this, "注册成功！", Toast.LENGTH_SHORT).show();
                        }else if(7 == integer){
                            register_NickName.setText("");
                            Toast.makeText(Register.this, "用户名被注册过！", Toast.LENGTH_SHORT).show();
                        }else if(8 == integer){
                            register_ID.setText("");
                            Toast.makeText(Register.this, "学号已被注册！", Toast.LENGTH_SHORT).show();
                        }else if(9 == integer){
                            register_ID.setText("");
                            register_Name.setText("");
                            Toast.makeText(Register.this, "学号与姓名不匹配！", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);        //会清除该进程空间的所有Activity
                startActivity(intent);
                finish();
            }
        });

    }

    private void getData() {
        registerNickName = register_NickName.getText().toString().trim();
        registerPassword = register_Password.getText().toString().trim();
        registerQQ = register_QQ.getText().toString().trim();
        registerID = register_ID.getText().toString().trim();
        registerName = register_Name.getText().toString().trim();
    }


}
