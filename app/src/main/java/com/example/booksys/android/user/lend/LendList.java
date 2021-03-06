package com.example.booksys.android.user.lend;

import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.alibaba.fastjson.TypeReference;
import com.example.booksys.R;
import com.example.booksys.Utils.AlertDialogUtils;
import com.example.booksys.Utils.FastJsonUtils;
import com.example.booksys.Utils.NetEnum;
import com.example.booksys.Utils.NetUtil;
import com.example.booksys.android.user.adapter.LendRecycleView;
import com.example.booksys.pojo.Book;
import com.example.booksys.pojo.Borrow;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LendList extends Fragment {

    String response;

    int rdID;

    ArrayList<Book> bookList;
    ArrayList<Borrow> borrows;

    RecyclerView listView;
    LendRecycleView lendAdapter;
    LinearLayoutManager layoutManager;

    SwipeRefreshLayout swipeRefreshLayout;

    SharedPreferences person ;

    LinearLayout linearLayout;
    ImageButton edit;

    Button returnBook, selectAll, noSelect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_user_lend, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /**
         * RecyclerView??????
         */
        listView = getView().findViewById(R.id.user_return_recyclerView);
        listView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getContext());
        //????????????
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        //???RecyclerView?????????????????????
        listView.setLayoutManager(layoutManager);
        lendAdapter = new LendRecycleView(bookList,borrows);
        lendAdapter.setRecyclerViewOnItemClickListener(new LendRecycleView.RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Book book = bookList.get(position);
            if(book.getBkState() == 0){
                Toast.makeText(getContext(), "????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!lendAdapter.mark) {

                Intent intent = new Intent(getActivity(), LendList_Item.class);

                String bookInfo = FastJsonUtils.objectToJsonString(bookList.get(position));

                intent.putExtra("book", bookInfo);

                startActivity(intent);
            } else {

                bookList.get(position).setCheck(!bookList.get(position).isCheck);
                lendAdapter.notifyDataSetChanged();
            }
            }
        });
        lendAdapter.setRecyclerOnItemLongClickListener(new LendRecycleView.RecyclerViewOnItemLongClickListener() {
            @Override
            public boolean onItemLongClickListener(View view, int position) {
                if (!lendAdapter.mark) {
                    edit();
                }
                return true;
            }
        });
        listView.setAdapter(lendAdapter);


        /**
         * ??????
         */
        swipeRefreshLayout = getView().findViewById(R.id.lend_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.purple_500);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refresh();
                swipeRefreshLayout.setRefreshing(false);//????????????????????????????????????????????????

            }
        });


        /**
         * ????????????
         */
        returnBook = getView().findViewById(R.id.lend);
        returnBook.setVisibility(View.GONE);
        returnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                returnBook();

            }
        });


        linearLayout = getView().findViewById(R.id.edit_layout);
        linearLayout.setVisibility(View.GONE);
        /**
         * ???????????????
         *
         */
        edit = getView().findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit();
            }
        });

        /**
         * ??????????????????
         */
        selectAll = getView().findViewById(R.id.select_all);
        selectAll.setVisibility(View.GONE);
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lendAdapter.mark) {
                    for (int i = 0; i < bookList.size(); i++) {
                        bookList.get(i).isCheck = true;
                    }

                    lendAdapter.notifyDataSetChanged();
                }
            }
        });


        /**
         * ????????????????????????
         */
        noSelect = getView().findViewById(R.id.no_select);
        noSelect.setVisibility(View.GONE);
        noSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNoList();
            }
        });

    }


//    private void setData() {
//
//        bookList = new ArrayList<>();
//
//        for (int i = 0; i < 10; i++) {
//            Book book = new Book();
//            book.setBkId(i);
//            book.setBkName("???xy" + i);
//            book.setBkPress("???????????????" + i);
//            book.setBkAuthor("?????????" + i);
//            book.setBkPrice(5 + i);
//            book.setBkNum(i);
//            book.setBkState((byte) 1);
//            bookList.add(book);
//        }
//    }


    /**
     * ?????????????????????
     */
    private void btnNoList() {

        if (lendAdapter.mark) {
            for (int i = 0; i < bookList.size(); i++) {
                bookList.get(i).isCheck = false;
            }

            lendAdapter.notifyDataSetChanged();
        }
    }

    /**
     * ???????????????????????????????????????
     *
     */
    private void returnBook() {
        String message = "????????? ";
        List<Integer> bkIds = new ArrayList<>();
        List<Book> bookList = new ArrayList<>();
        if (lendAdapter.mark) {
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
            if(bkIds.size() == 0){
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
                     * ???borrows????????????json??????
                     */

                    String json = FastJsonUtils.listToJsonString(bkIds);

                    String data;
                    try {
                        data = "rdId=" + rdID
                        + "&bkIds=" + json;

                        System.out.println(data);
                        NetUtil netUtil = new NetUtil(data, NetEnum.ReturnBookServletUrl,"POST");
                        netUtil.start();
                        netUtil.join();//????????????
                        response = netUtil.getResponse();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Map<String,Object> map = FastJsonUtils.strToJavaBeanMap(response,
                            new TypeReference<Map<String,Object>>(){});
                    int status = (int) map.get("status");

                    if(status == 3){
                        List<Boolean> flags = (List<Boolean>) map.get("data");
                        String success = "";
                        int returnFlag = 0;
                        for (int i = 0; i < flags.size(); i++) {
                            if(flags.get(i)) {
                                success += bookList.get(i).getBkName() + "   ";
                                returnFlag ++;
                            }
                        }
                        String result;
                        if(returnFlag == 0){
                            result = "??????????????????";
                        }else if(returnFlag == flags.size()){
                            result = "??????????????????";
                        }else {
                            result = success+"????????????,????????????" ;
                        }
                        refresh();
                        Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                    }else if(status == 4){
                        Toast.makeText(getContext(), "?????????json????????????", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(), "??????", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            });


        }
    }


    private void edit() {
        lendAdapter.mark = !lendAdapter.mark;

        if (lendAdapter.mark) {
            edit.setImageDrawable(getResources().getDrawable(R.drawable.back));

            linearLayout.setVisibility(View.VISIBLE);
            returnBook.setVisibility(View.VISIBLE);
            noSelect.setVisibility(View.VISIBLE);
            selectAll.setVisibility(View.VISIBLE);
            btnNoList();

        } else {
            edit.setImageDrawable(getResources().getDrawable(R.drawable.menu));

            linearLayout.setVisibility(View.GONE);
            returnBook.setVisibility(View.GONE);
            noSelect.setVisibility(View.GONE);
            selectAll.setVisibility(View.GONE);
        }

        lendAdapter.notifyDataSetChanged();
    }


    /**
     * ?????????????????????
     */

    private void setData() {
        try {
            //?????????SharedPreferences
            person = getActivity().getSharedPreferences("data",getContext().MODE_PRIVATE);
            rdID = person.getInt("rdId",0);

            String data = "rdId=" + rdID;
            NetUtil netUtil = new NetUtil(data, NetEnum.QueryRdBookServletUrl,"POST");
            netUtil.start();
            netUtil.join();
            response = netUtil.getResponse();
        } catch (InterruptedException  e) {
            e.printStackTrace();
        }

        Map<String, Object> map = FastJsonUtils.strToJavaBeanMap(response, new TypeReference<Map<String, Object>>() {
        });

        int flag = (int)map.get("status");
        if(flag == 6){
            bookList = (ArrayList<Book>) FastJsonUtils.strToJavaBeanList( map.get("data").toString(),
                    new TypeReference<List<Book>>(){});

            borrows = (ArrayList<Borrow>) FastJsonUtils.strToJavaBeanList( map.get("borrows").toString(),
                    new TypeReference<List<Borrow>>(){});

        } else if(flag == 7){
            bookList = new ArrayList<>();
        }else if(flag == 8){
            Toast.makeText(getContext(),"json????????????",Toast.LENGTH_SHORT).show();
        }

    }

    public void refresh(){
        setData();
        lendAdapter.setLendRecycleViewData(bookList,borrows);
        lendAdapter.notifyDataSetChanged();
    }

}
