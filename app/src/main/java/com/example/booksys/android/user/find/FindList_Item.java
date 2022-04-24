package com.example.booksys.android.user.find;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.booksys.Login;
import com.example.booksys.R;
import com.example.booksys.Utils.AlertDialogUtils;
import com.example.booksys.Utils.FastJsonUtils;
import com.example.booksys.Utils.GsonUtils;
import com.example.booksys.Utils.NetEnum;
import com.example.booksys.Utils.NetUtil;
import com.example.booksys.android.user.UserActivity;
import com.example.booksys.android.user.lend.LendList_Item;
import com.example.booksys.pojo.Book;
import com.example.booksys.pojo.Borrow;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FindList_Item extends AppCompatActivity {


    ImageView bookImage;
    TextView bookName, bookPress, bookAuthor, bkPrice, bookNum;
    Button bookLend, bookBack;

    Book book;

    SharedPreferences person;

    String response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_user_book_information);
        init();
        setData();
    }

    private void init() {
        bookImage = findViewById(R.id.book_image);
        bookName = findViewById(R.id.book_name);
        bookPress = findViewById(R.id.book_author);
        bookAuthor = findViewById(R.id.book_Press);
        bkPrice = findViewById(R.id.book_Price);
        bookNum = findViewById(R.id.book_Num);

        bookLend = findViewById(R.id.book_lend);
        bookBack = findViewById(R.id.book_back);

        person = getSharedPreferences("data", MODE_PRIVATE);

        bookLend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(book.getBkNum() <= 0){
                    Toast.makeText(FindList_Item.this, "库存不足，暂不可借阅", Toast.LENGTH_SHORT).show();
                    return;
                }

                String message = "确定将 " + book.getBkName() + " 借阅吗？";

                AlertDialogUtils.showDialog(view.getContext(), "借 书", message, "取消",
                        "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                /**
                                 * 借书
                                 */

                                List<Integer> rdIds = new ArrayList<>();
                                rdIds.add(book.getBkId());

                                String json = FastJsonUtils.listToJsonString(rdIds);

                                String data;
                                try {
                                    data = "rdId=" + person.getInt("rdId",0)
                                            + "&bkIds=" + json
                                            + "&readerTypeDayNum=" + person.getInt("CanLendNumDay", 0);

                                    NetUtil netUtil = new NetUtil(data, NetEnum.LendBookServletUrl,"POST");
                                    netUtil.start();
                                    netUtil.join();//线程同步
                                    response = netUtil.getResponse();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                Map<String,Object> map = FastJsonUtils.strToJavaBeanMap(response,
                                        new TypeReference<Map<String,Object>>(){});

                                int status =(int) map.get("status");

                                if(status == 0){
                                    List<Boolean> flag = FastJsonUtils.strToJavaBeanList(map.get("data").toString(),
                                            new TypeReference<List<Boolean>>(){}) ;
                                    if (flag.get(0)) {
                                        Toast.makeText(FindList_Item.this, "借书成功", Toast.LENGTH_SHORT).show();

                                    } else if(!flag.get(0)){
                                        Toast.makeText(FindList_Item.this, "借书失败", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(FindList_Item.this, "异常", Toast.LENGTH_SHORT).show();
                                    }

                                    Intent intent = new Intent(FindList_Item.this, UserActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else if(status == 1){
                                    Toast.makeText(FindList_Item.this, "json格式错误", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(FindList_Item.this, "异常", Toast.LENGTH_SHORT).show();

                                }
                                dialogInterface.dismiss();
                            }
                        });
            }
        });

        bookBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void setData() {

        Intent intent = getIntent();
        book = FastJsonUtils.strToJavaBean(intent.getStringExtra("book"),new TypeReference<Book>(){});

        NetUtil netUtil = new NetUtil(book.getBkImageUrl());
        netUtil.start();
        try {
            netUtil.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = netUtil.getImage();

        bookImage.setImageBitmap(bitmap);

        bookName.setText(book.getBkName());
        bookPress.setText(book.getBkPress());
        bookAuthor.setText(book.getBkAuthor());
        bkPrice.setText(book.getBkPrice()+"");
        bookNum.setText("数    量："+book.getBkNum()+"");

    }

}