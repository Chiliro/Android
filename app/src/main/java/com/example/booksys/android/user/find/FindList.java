package com.example.booksys.android.user.find;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.booksys.R;
import com.example.booksys.Utils.AlertDialogUtils;
import com.example.booksys.Utils.FastJsonUtils;
import com.example.booksys.Utils.NetEnum;
import com.example.booksys.Utils.NetUtil;
import com.example.booksys.android.user.adapter.FindRecycleView;
import com.example.booksys.pojo.Book;
import com.example.booksys.pojo.Borrow;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FindList extends Fragment {

    SharedPreferences person;
    SharedPreferences.Editor editor;

    int rdID, canLendDay;
    String  response,searchText = "";

    List<Book> bookList;

    RecyclerView listView;
    FindRecycleView findAdapter;
    LinearLayoutManager layoutManager;

    SwipeRefreshLayout swipeRefreshLayout;

    ImageButton editButton;
    Button search, lend;

    EditText edit_search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_find, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setData();
        /**
         * RecyclerView设置
         */
        listView = getView().findViewById(R.id.user_finding_recyclerView);

        //item固定长度
        listView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false);
        //给RecyclerView设置布局管理器
        listView.setLayoutManager(layoutManager);
        findAdapter = new FindRecycleView(bookList);

        findAdapter.setRecyclerViewOnItemClickListener(new FindRecycleView.RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                if (!findAdapter.mark) {                    //判断是否是编辑模式
                    /**
                     * 界面跳转及传值
                     */
                    Intent intent = new Intent(getActivity(), FindList_Item.class);
                    String bookJson = FastJsonUtils.objectToJsonString(bookList.get(position));
                    intent.putExtra("book", bookJson);
                    startActivity(intent);
                } else {
                    if (bookList.get(position).getBkNum() > 0) {
                        bookList.get(position).setCheck(!bookList.get(position).isCheck);
                        findAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), bookList.get(position).getBkName() + "库存不足，不可借阅", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        findAdapter.setRecyclerOnItemLongClickListener(new FindRecycleView.RecyclerViewOnItemLongClickListener() {
            @Override
            public boolean onItemLongClickListener(View view, int position) {
                if (!findAdapter.mark) {
                    edit();
                }
                return true;
            }
        });
        listView.setAdapter(findAdapter);

        //刷新
        swipeRefreshLayout = getView().findViewById(R.id.find_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.purple_500);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setData();
                findAdapter.setFindRecycleViewData(bookList);
                findAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        /**
         * 初始化SharedPreferences
         */

        person = getActivity().getSharedPreferences("data", getContext().MODE_PRIVATE);
        editor = person.edit();
        rdID = person.getInt("rdId", 0);
        canLendDay = person.getInt("CanLendNumDay", 0);

        /**
         * 查找editView
         */
        edit_search = getView().findViewById(R.id.editText);

        /**
         * 搜索Button
         */
        search = getView().findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText = edit_search.getText().toString().trim();

                search(searchText);

                findAdapter.setFindRecycleViewData(bookList);
                findAdapter.notifyDataSetChanged();
            }
        });


        /**
         * 借书Button
         */
        lend = getView().findViewById(R.id.lend);
        lend.setVisibility(View.GONE);
        lend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lendBooks();
            }

        });

        /**
         * 编辑框设置
         *
         */
        editButton = getView().findViewById(R.id.edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit();
            }
        });
    }


    private void setData() {

        search(searchText);

    }

//    private void setData() {
//
//        bookList = new ArrayList<>();
//
//        for (int i = 0; i < 10; i++) {
//            Book book = new Book();
//            book.setBkId(i);
//            book.setBkName("xy" + i);
//            book.setBkPress("清华出版社" + i);
//            book.setBkAuthor("於阳明" + i);
//            book.setBkPrice(5 + i);
//            book.setBkNum(i);
//            book.setBkState((byte) 1);
//            bookList.add(book);
//        }
//
//    }

    /**
     * 取消全部选中的
     */
    private void btnNoList() {

        if (findAdapter.mark) {
            for (int i = 0; i < bookList.size(); i++) {
                bookList.get(i).isCheck = false;
            }

            findAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取选中数据并向服务器发送
     */
    private void lendBooks() {
        String message = "确定将 ";
        List<Book> bookList = new ArrayList<>();

        List<Integer> bkIds = new ArrayList<>();
        if (findAdapter.mark) {
            for (Book book : this.bookList) {
                if (book.isCheck) {

                    bkIds.add(book.getBkId());
                    bookList.add(book);
                    message = message + book.getBkName() + "    ";
                }
            }

            /**
             * 判断是否选择了书籍，未选择这则直接返回
             */
            if (bkIds.size() == 0) {
                Toast.makeText(getContext(), "请先选择要借阅的书籍", Toast.LENGTH_SHORT).show();
                return;
            }

            message = message + " 借阅吗？";
            AlertDialogUtils.showDialog(getContext(), "借 书", message, "取消", "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //取消
                    dialog.dismiss();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    /**
                     * 将bkIds数据转为json发送
                     */

                    String json = FastJsonUtils.listToJsonString(bkIds);

                    String data;
                    try {
                            data = "rdId=" + rdID
                                + "&bkIds=" + json
                                + "&readerTypeDayNum=" + canLendDay;

                        NetUtil netUtil = new NetUtil(data, NetEnum.LendBookServletUrl, "POST");
                        netUtil.start();
                        netUtil.join();//线程同步
                        response = netUtil.getResponse();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Map<String, Object> map = FastJsonUtils.strToJavaBeanMap(response,
                            new TypeReference<Map<String, Object>>() {
                            });

                    try {
                        int status = (int) map.get("status");
                        if (status == 0) {
                            List<Boolean> flags = FastJsonUtils.strToJavaBeanList(map.get("data").toString(),
                                    new TypeReference<List<Boolean>>() {
                                    });
                            String success = "";
                            int returnFlag = 0;
                            for (int i = 0; i < flags.size(); i++) {
                                if (flags.get(i)) {
                                    success += bookList.get(i).getBkName() + "   ";
                                    returnFlag++;
                                }
                            }
                            String result;
                            if (returnFlag == 0) {
                                result = "全部借书失败";
                            } else if (returnFlag == flags.size()) {
                                result = "全部借书成功";
                            } else {
                                result = success + "借书成功,其他失败";
                            }
                            editor.putInt("alreadyLendBookNum",person.getInt("alreadyLendBookNum",0)+returnFlag);
                            editor.commit();
                            Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                        } else if (status == 1) {
                            Toast.makeText(getContext(), "异常", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }

                    dialog.dismiss();
                }
            });

        }

    }

    /**
     * 编辑框状态更改
     */
    private void edit() {

        findAdapter.mark = !findAdapter.mark;

        if (findAdapter.mark) {

            editButton.setImageDrawable(getResources().getDrawable(R.drawable.back));

            /**
             * 借书按钮变为可见
             */
            lend.setVisibility(View.VISIBLE);

            btnNoList();

        } else {

            editButton.setImageDrawable(getResources().getDrawable(R.drawable.menu));

            /**
             * 借书按钮变为不可见
             */
            lend.setVisibility(View.GONE);
        }

        findAdapter.notifyDataSetChanged();

    }

    /**
     * 搜索书籍
     */
    private void search(String bkInfo) {
        try {
            String data = "bkInfo=" + URLEncoder.encode(bkInfo, "UTF-8");
            NetUtil netUtil = new NetUtil(data, NetEnum.QueryBookServletUrl, "GET");
            netUtil.start();
            netUtil.join();
            response = netUtil.getResponse();
        } catch (UnsupportedEncodingException | InterruptedException e) {
            e.printStackTrace();
        }

        Map<String, Object> map = FastJsonUtils.strToJavaBeanMap(response,
                new TypeReference<Map<String, Object>>() {
                });
        int flag = (int) map.get("status");
        if (flag == 5) {
            bookList = FastJsonUtils.strToJavaBeanList(map.get("data").toString(), new TypeReference<List<Book>>() {
            });

        } else if(flag == 6){
            Toast.makeText(getContext(), "未找到书籍！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "异常", Toast.LENGTH_SHORT).show();
        }
    }

}
