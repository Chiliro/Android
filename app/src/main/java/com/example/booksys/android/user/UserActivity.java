package com.example.booksys.android.user;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.booksys.R;
import com.example.booksys.android.user.lend.LendList;
import com.example.booksys.android.user.find.FindList;
import com.example.booksys.android.user.my.My;

public class UserActivity extends FragmentActivity implements
        OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    //定义Fragment
    private Fragment fragment1;
    private Fragment fragment2;
    private Fragment fragment3;
    //定义FragmentManager
    private FragmentManager fragmentManager;

    //定义组件
    private ViewPager viewPager;
    private List<Fragment> fragmentLists;
    private MyFragmentPageAdapter adapter;

    private RadioGroup radioGroup;
    private RadioButton find,lend,my;               // find表示第一个RadioButton 组件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//取消ActionBar
        setContentView(R.layout.activity_user);

        //初始化界面组件
        init();
        //初始化ViewPager
        initViewPager();
    }

    private void initViewPager() {
        fragment1 = new FindList();
        fragment2 = new LendList();
        fragment3 = new My();

        fragmentLists = new ArrayList<>();
        fragmentLists.add(fragment1);
        fragmentLists.add(fragment2);
        fragmentLists.add(fragment3);
        //获取FragmentManager对象
        fragmentManager = getSupportFragmentManager();
        //获取FragmentPageAdapter对象
        adapter = new MyFragmentPageAdapter(fragmentManager, fragmentLists);
        //设置Adapter，使ViewPager 与 Adapter 进行绑定
        viewPager.setAdapter(adapter);
        //设置ViewPager默认显示第一个View
        viewPager.setCurrentItem(0);
        //设置第一个RadioButton为默认选中状态
        find.setChecked(true);
        //ViewPager页面切换监听
        viewPager.addOnPageChangeListener(this);
    }

    private void init() {
        radioGroup =  findViewById(R.id.radioGrop);
        viewPager =  findViewById(R.id.viewPager);
        find =  findViewById(R.id.user_finding);
        lend =  findViewById(R.id.user_Lend);
        my =  findViewById(R.id.user_my);
        //RadioGroup状态改变监听
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.user_finding:             // find
                //显示第一个Fragment并关闭动画效果
                viewPager.setCurrentItem(0,true);
                find.setBackgroundColor(Color.parseColor("#808080"));
                lend.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                my.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                break;


            case R.id.user_Lend:                // lend
                viewPager.setCurrentItem(1,true);
                find.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                lend.setBackgroundColor(Color.parseColor("#808080"));
                my.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                break;


            case R.id.user_my:                  // my
                viewPager.setCurrentItem(2,true);
                find.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                lend.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                my.setBackgroundColor(Color.parseColor("#808080"));
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {}

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {}

    /**
     * ViewPager切换Fragment时，RadioGroup做相应的监听
     */
    @Override
    public void onPageSelected(int arg0) {
        switch (arg0) {
            case 0:
                find.setChecked(true);
                break;
            case 1:
                lend.setChecked(true);
                break;
            case 2:
                my.setChecked(true);
                break;

        }
    }
}
