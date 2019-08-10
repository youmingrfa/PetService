package com.example.administrator.petservice.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.example.administrator.petservice.R;
import com.example.administrator.petservice.ui.fragment.HomeFragment;
import com.example.administrator.petservice.ui.fragment.MineFragment;
import com.example.administrator.petservice.ui.fragment.OrderFragment;
import com.example.administrator.petservice.ui.fragment.PetFragment;

/**
 * @author rfa
 * 主界面，应该是在登录成功后进入此界面，在布局上需要有一个搜索按钮（比较简单实现），轮播图来轮播广告
 */
public class MainActivity extends AppCompatActivity  {

    private BottomNavigationView bottomNavigationView;
    //定义Fragment
    private HomeFragment homeFragment;
    private PetFragment petFragment;
    private OrderFragment orderFragment;
    private MineFragment mineFragment;
    //记录当前正在使用的fragment
    private Fragment isFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去除顶部标题栏
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initFragment(savedInstanceState);
        initView();
        initData();
        initListener();


    }

    /**
     * @param savedInstanceState
     * 初始化Fragment，并把HomeFragment设立为默认选中的
     */
    public void initFragment(Bundle savedInstanceState) {
        //判断activity是否重建，如果不是，则不需要重新建立fragment.
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if (homeFragment == null) {
                homeFragment = new HomeFragment();
            }
            isFragment = homeFragment;
            ft.replace(R.id.container, homeFragment).commit();
        }
    }

    /**
     * 初始化布局
     */
    private void initView() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);

    }

    private void initData(){


    }

    /**
     * 初始化事件
     */
    private void initListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.item_home:
                        if (homeFragment == null) {
                            homeFragment = new HomeFragment();
                        }
                        switchContent(isFragment, homeFragment);
                        return true;
                    case R.id.item_pet:
                        if (petFragment == null) {
                            petFragment = new PetFragment();
                        }
                        switchContent(isFragment, petFragment);
                        return true;
                    case R.id.item_order:
                        if (orderFragment == null) {
                            orderFragment = new OrderFragment();
                        }
                        switchContent(isFragment, orderFragment);
                        return true;
                    case R.id.item_mine:
                        if(mineFragment == null){
                            mineFragment = new MineFragment();
                        }
                        switchContent(isFragment,mineFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    /**
     * @param from
     * @param to
     * 切换显示的Fragment
     */
    public void switchContent(Fragment from, Fragment to) {
        if (isFragment != to) {
            isFragment = to;
            FragmentManager fm = getSupportFragmentManager();
            //添加渐隐渐现的动画
            FragmentTransaction ft = fm.beginTransaction();
            // 先判断是否被add过
            if (!to.isAdded()) {
                // 隐藏当前的fragment，add下一个到Activity中
                ft.hide(from).add(R.id.container, to).commit();
            } else {
                // 隐藏当前的fragment，显示下一个
                ft.hide(from).show(to).commit();
            }
        }
    }
}
