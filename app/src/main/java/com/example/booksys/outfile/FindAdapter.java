package com.example.booksys.outfile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.booksys.R;
import com.example.booksys.Utils.NetUtil;
import com.example.booksys.pojo.Book;

import java.util.List;

public class FindAdapter extends BaseAdapter {

    private Context mContext;

    private List<Book> bookList;

    private LayoutInflater mInflater;


    public boolean mark = false;

    public FindAdapter(Context mContext, List<Book> bookList) {
        this.mContext = mContext;
        this.bookList = bookList;

        mInflater = LayoutInflater.from((this.mContext));

    }


    /**
     * 返回集合数据的数量
     *
     * @return int
     */
    @Override
    public int getCount() {
        return bookList.size();
    }

    /**
     * 获取指定下标对应的数据对象
     *
     * @return Object
     */
    @Override
    public Object getItem(int i) {
        return bookList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    /**
     * 返回对应下表item的view对象
     *
     * @param i         下标
     * @param view
     * @param viewGroup ListView对象
     * @return View
     */
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //加载item布局，得到View对象

        ViewHolder holder = null;

        if (view == null) {
            holder = new ViewHolder();
            // 条目布局
            view = mInflater.inflate(R.layout.activity_find_bookitem, null);

            holder.imageView = view.findViewById(R.id.book_image);
            holder.checkbox = view.findViewById(R.id.checkbox_operate_data);
            holder.bookName = view.findViewById(R.id.book_name);
            holder.bookAuthor = view.findViewById(R.id.book_author);
            holder.bookPress = view.findViewById(R.id.book_Press);

            view.setTag(holder);
        } else {

            holder = (ViewHolder) view.getTag();
        }

        final Book book = bookList.get(i);

            NetUtil netUtil = new NetUtil(book.getBkImageUrl());
            netUtil.start();
            try {
                netUtil.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            Bitmap bitmap = netUtil.getImage();

            holder.imageView.setImageBitmap(bitmap);



        if (book != null) {
            holder.bookName.setText("书    名：" + book.getBkName());
            holder.bookAuthor.setText("作    者：" + book.getBkAuthor());
            holder.bookPress.setText("出版社：" + book.getBkPress());
            //根据isSelected来设置checkbox的显示状况
            if (mark) {
                holder.checkbox.setVisibility(View.VISIBLE);
            } else {
                holder.checkbox.setVisibility(View.GONE);
            }

            holder.checkbox.setChecked(book.isCheck);

            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    book.setCheck(!book.isCheck);
                }
            });

        }

        return view;
    }

    class ViewHolder {

        public ImageView imageView;

        public CheckBox checkbox;

        public TextView bookName;

        public TextView bookAuthor;

        public TextView bookPress;

    }

}