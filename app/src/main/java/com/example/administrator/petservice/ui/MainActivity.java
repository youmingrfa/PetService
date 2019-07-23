package com.example.administrator.petservice.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import com.example.administrator.petservice.R;
import com.example.administrator.petservice.ui.activity.HistorySearchActivity;
import com.example.administrator.petservice.ui.activity.LoginActivity;

/**
 * 主界面，应该是在登录成功后进入此界面，在布局上需要有一个搜索按钮（比较简单实现），轮播图来轮播广告，剩下的没有想好
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE); // 去除顶部标题栏
        setContentView(R.layout.activity_main);

        initView();
        init();
        Intent intent =  new Intent(MainActivity.this,HistorySearchActivity.class);
        startActivity(intent);
    }

    private void initView() {

    }

    private void init(){

    }

}
