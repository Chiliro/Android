package com.example.booksys.android.user.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booksys.R;
import com.example.booksys.Utils.NetUtil;
import com.example.booksys.pojo.Book;
import com.example.booksys.pojo.Borrow;

import java.util.List;

public class LendRecycleView extends RecyclerView.Adapter<LendRecycleView.ViewHolder> implements View.OnClickListener,View.OnLongClickListener {

    private List<Book> bookList;
    private List<Borrow> borrowList;

    private RecyclerViewOnItemClickListener onItemClickListener;
    private RecyclerViewOnItemLongClickListener onItemLongClickListener;

    public boolean mark = false;

    public LendRecycleView(List<Book> bookList, List<Borrow> borrowList) {
        this.bookList = bookList;
        this.borrowList = borrowList;
    }
    public  void setLendRecycleViewData(List<Book> bookList, List<Borrow> borrowList) {
        this.bookList = bookList;
        this.borrowList = borrowList;
    }


    /**
     * Item的控件绑定
     */
    class ViewHolder extends RecyclerView.ViewHolder{
        public View itemView;

        public ImageView imageView;

        public CheckBox checkbox;

        public TextView bookName;

        public TextView bookAuthor;

        public TextView lendTime;

        public TextView returnTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageView = itemView.findViewById(R.id.book_image);
            checkbox = itemView.findViewById(R.id.checkbox_operate_data);

            bookName = itemView.findViewById(R.id.book_name);
            bookAuthor = itemView.findViewById(R.id.book_author);
            lendTime = itemView.findViewById(R.id.lend_time);
            returnTime = itemView.findViewById(R.id.return_time);
        }
    }


    /**
     * 绑定Item及设置监听
     *
     * @param parent
     * @param viewType
     * @return ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_lend_bookitem,parent,false);
        ViewHolder holder = new ViewHolder(view);

        //为Item设置点击事件,以整个item做为可点击的对象
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);

        return holder;
    }

    /**
     * 给item设置数据
     *
     * @param holder
     * @param position
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Book book = bookList.get(position);
        final Borrow borrow = borrowList.get(position);
        try {
            NetUtil netUtil = new NetUtil(book.getBkImageUrl());
            netUtil.start();
            netUtil.join();
            Bitmap bitmap = netUtil.getImage();

            holder.imageView.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        holder.bookName.setText("书    名：" + book.getBkName());
        holder.bookAuthor.setText("作    者：" + book.getBkAuthor());
        holder.lendTime.setText("借书时间：" + borrow.getDateBorrow().substring(0,10));
        if(borrow.getOverdue() == 0){
            holder.returnTime.setText("应还时间：" + borrow.getDateLendPlan().substring(0,10));
        }else{
            holder.returnTime.setText("已逾期！请尽快归还");
            holder.returnTime.setTextColor(Color.parseColor("#ff0000"));
        }

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
        holder.itemView.setTag(position);
    }



    @Override
    public int getItemCount() {
        return bookList.size();
    }

    @Override
    public void onClick(View view) {

        //这里使用getTag方法获取数据
        if(onItemClickListener != null){
            onItemClickListener.onItemClickListener(view,(Integer)view.getTag());
        }
    }

    @Override
    public boolean onLongClick(View view) {
        return onItemLongClickListener.onItemLongClickListener(view,(Integer)view.getTag());
    }

    /*设置点击事件*/
    public void setRecyclerViewOnItemClickListener(RecyclerViewOnItemClickListener onItemClickListener) {
        this.onItemClickListener = (RecyclerViewOnItemClickListener) onItemClickListener;
    }

    /*设置长按事件*/
    public void setRecyclerOnItemLongClickListener(RecyclerViewOnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = (RecyclerViewOnItemLongClickListener) onItemLongClickListener;
    }

    public interface RecyclerViewOnItemClickListener {

        void onItemClickListener(View view, int position);

    }

    public interface RecyclerViewOnItemLongClickListener {

        boolean onItemLongClickListener(View view, int position);

    }
}
