package com.example.booksys.android.user.lend;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.TypeReference;
import com.example.booksys.R;
import com.example.booksys.Utils.AlertDialogUtils;
import com.example.booksys.Utils.FastJsonUtils;
import com.example.booksys.Utils.NetEnum;
import com.example.booksys.Utils.NetUtil;
import com.example.booksys.pojo.Book;
import com.example.booksys.pojo.Borrow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LendList_Item extends AppCompatActivity {
    TextView bookName, bookPress, bookAuthor, bkPrice, bookNum;
    Button bookReturn, bookBack;

    String response;
    Book book ;
    SharedPreferences person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_book_information);
        init();
        setData();
    }

    private void init() {
        bookName = findViewById(R.id.book_name);
        bookAuthor = findViewById(R.id.book_author);
        bookPress = findViewById(R.id.book_Press);
        bkPrice = findViewById(R.id.book_Price);
        bookNum = findViewById(R.id.book_Num);
        person = getSharedPreferences("data", MODE_PRIVATE);

        bookReturn = findViewById(R.id.book_lend);
        bookBack = findViewById(R.id.book_back);

        bookReturn.setText("还书");

        bookReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "确定将 " + book.getBkName() + " 归还吗？";

                AlertDialogUtils.showDialog(view.getContext(), "还 书", message, "取消",
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
                                 * 还书
                                 */

                                List<Integer> bkIds = new ArrayList<>();
                                bkIds.add(book.getBkId());

                                String json = FastJsonUtils.listToJsonString(bkIds);

                                String data;
                                try {
                                    data = "rdId=" + person.getInt("rdId", 0)
                                            + "&bkIds=" + json;
                                    NetUtil netUtil = new NetUtil(data, NetEnum.ReturnBookServletUrl, "POST");
                                    netUtil.start();
                                    netUtil.join();//线程同步
                                    response = netUtil.getResponse();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {

                                    Map<String, Object> map = FastJsonUtils.strToJavaBeanMap(response,
                                            new TypeReference<Map<String, Object>>() {
                                            });

                                    int status = (int) map.get("status");

                                    if (status == 3) {
                                        List<Boolean> flag = (List<Boolean>) map.get("data");
                                        if (flag.get(0)) {
                                            Toast.makeText(LendList_Item.this, "还书成功", Toast.LENGTH_SHORT).show();

                                        } else if (!flag.get(0)) {
                                            Toast.makeText(LendList_Item.this, "还书失败", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(LendList_Item.this, "异常", Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (status == 4) {
                                        Toast.makeText(LendList_Item.this, "json格式错误", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(LendList_Item.this, "异常", Toast.LENGTH_SHORT).show();

                                    }
                                } catch (Exception e) {
                                    System.out.println(e);
                                }

                                dialogInterface.dismiss();
                                finish();
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


        bookName.setText(book.getBkName());
        bookAuthor.setText(book.getBkAuthor());
        bookPress.setText(book.getBkPress());
        bkPrice.setText(book.getBkPrice()+"");

    }

}
