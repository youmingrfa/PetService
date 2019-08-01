package com.example.administrator.petservice.ui;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.administrator.petservice.R;
import com.example.administrator.petservice.ui.activity.HistorySearchActivity;
import com.example.administrator.petservice.ui.activity.LoginActivity;
import com.example.administrator.petservice.ui.adapter.PagerAdapter;
import com.example.administrator.petservice.ui.fragment.HomeFragment;
import com.example.administrator.petservice.ui.fragment.MineFragment;
import com.example.administrator.petservice.ui.fragment.OrderFragment;
import com.example.administrator.petservice.ui.fragment.PetFragment;
import com.example.administrator.petservice.widget.BannerLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import static com.example.administrator.petservice.ui.utils.ImageUtil.imageUrls;

/**
 * 主界面，应该是在登录成功后进入此界面，在布局上需要有一个搜索按钮（比较简单实现），轮播图来轮播广告，剩下的没有想好
 * @author rfa
 */
public class MainActivity extends AppCompatActivity  {

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private List<Fragment> fragmentList = new ArrayList<>();
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去除顶部标题栏
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initListener();

    }

    private void initView() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        viewPager = findViewById(R.id.viewPager);
    }

    private void initData(){
        fragmentList.add(new HomeFragment());
        fragmentList.add(new PetFragment());
        fragmentList.add(new OrderFragment());
        fragmentList.add(new MineFragment());
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(pagerAdapter);

        //viewPager默认显示HomeFragment
        viewPager.setCurrentItem(0);
        //底部按钮默认选中第1个
        bottomNavigationView.getMenu().getItem(0).setChecked(true);

    }

    private void initListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_home:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.item_pet:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.item_order:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.item_mine:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return false;
            }
        });
    }
}
