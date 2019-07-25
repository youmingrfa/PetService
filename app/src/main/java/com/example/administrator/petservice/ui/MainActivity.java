package com.example.administrator.petservice.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.administrator.petservice.R;
import com.example.administrator.petservice.ui.activity.HistorySearchActivity;
import com.example.administrator.petservice.ui.activity.LoginActivity;
import com.example.administrator.petservice.widget.BannerLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import static com.example.administrator.petservice.ui.utils.ImageUtil.imageUrls;

/**
 * 主界面，应该是在登录成功后进入此界面，在布局上需要有一个搜索按钮（比较简单实现），轮播图来轮播广告，剩下的没有想好
 * @author rfa
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button searchBtn;
    private Banner banner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去除顶部标题栏
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        setClickListener();
    }

    private void initView() {
        searchBtn = findViewById(R.id.search_btn);
        banner = findViewById(R.id.banner);
    }

    private void initData(){
        //轮播图的设置
        banner.setImageLoader(new BannerLoader());
        List<String> imageList = new ArrayList<>();
        for(int i=0;i<imageUrls.length;i++)
            imageList.add(imageUrls[i]);
        banner.setImages(imageList);
        banner.start();

    }

    private void setClickListener(){
        searchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch ((v.getId())){
            case R.id.search_btn:
                intent = new Intent(MainActivity.this,HistorySearchActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
