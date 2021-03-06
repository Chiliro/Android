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
         * RecyclerView??????
         */
        listView = getView().findViewById(R.id.user_finding_recyclerView);

        //item????????????
        listView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false);
        //???RecyclerView?????????????????????
        listView.setLayoutManager(layoutManager);
        findAdapter = new FindRecycleView(bookList);

        findAdapter.setRecyclerViewOnItemClickListener(new FindRecycleView.RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                if (!findAdapter.mark) {                    //???????????????????????????
                    /**
                     * ?????????????????????
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
                        Toast.makeText(getContext(), bookList.get(position).getBkName() + "???????????????????????????", Toast.LENGTH_SHORT).show();
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

        //??????
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
         * ?????????SharedPreferences
         */

        person = getActivity().getSharedPreferences("data", getContext().MODE_PRIVATE);
        editor = person.edit();
        rdID = person.getInt("rdId", 0);
        canLendDay = person.getInt("CanLendNumDay", 0);

        /**
         * ??????editView
         */
        edit_search = getView().findViewById(R.id.editText);

        /**
         * ??????Button
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
         * ??????Button
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
         * ???????????????
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
//            book.setBkPress("???????????????" + i);
//            book.setBkAuthor("?????????" + i);
//            book.setBkPrice(5 + i);
//            book.setBkNum(i);
//            book.setBkState((byte) 1);
//            bookList.add(book);
//        }
//
//    }

    /**
     * ?????????????????????
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
     * ???????????????????????????????????????
     */
    private void lendBooks() {
        String message = "????????? ";
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
             * ?????????????????????????????????????????????????????????
             */
            if (bkIds.size() == 0) {
                Toast.makeText(getContext(), "??????????????????????????????", Toast.LENGTH_SHORT).show();
                return;
            }

            message = message + " ????????????";
            AlertDialogUtils.showDialog(getContext(), "??? ???", message, "??????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //??????
                    dialog.dismiss();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    /**
                     * ???bkIds????????????json??????
                     */

                    String json = FastJsonUtils.listToJsonString(bkIds);

                    String data;
                    try {
                            data = "rdId=" + rdID
                                + "&bkIds=" + json
                                + "&readerTypeDayNum=" + canLendDay;

                        NetUtil netUtil = new NetUtil(data, NetEnum.LendBookServletUrl, "POST");
                        netUtil.start();
                        netUtil.join();//????????????
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
                                result = "??????????????????";
                            } else if (returnFlag == flags.size()) {
                                result = "??????????????????";
                            } else {
                                result = success + "????????????,????????????";
                            }
                            editor.putInt("alreadyLendBookNum",person.getInt("alreadyLendBookNum",0)+returnFlag);
                            editor.commit();
                            Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                        } else if (status == 1) {
                            Toast.makeText(getContext(), "??????", Toast.LENGTH_SHORT).show();
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
     * ?????????????????????
     */
    private void edit() {

        findAdapter.mark = !findAdapter.mark;

        if (findAdapter.mark) {

            editButton.setImageDrawable(getResources().getDrawable(R.drawable.back));

            /**
             * ????????????????????????
             */
            lend.setVisibility(View.VISIBLE);

            btnNoList();

        } else {

            editButton.setImageDrawable(getResources().getDrawable(R.drawable.menu));

            /**
             * ???????????????????????????
             */
            lend.setVisibility(View.GONE);
        }

        findAdapter.notifyDataSetChanged();

    }

    /**
     * ????????????
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
            Toast.makeText(getContext(), "??????????????????", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "??????", Toast.LENGTH_SHORT).show();
        }
    }

}
