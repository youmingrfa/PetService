package com.example.administrator.petservice.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.administrator.petservice.R;

public class MedicalActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去除顶部标题栏
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_medical);

        initView();
        initData();
        initListener();
    }

    private void initView(){
        map = findViewById(R.id.iv_map);

    }

    private void initData(){


    }

    private void initListener(){
        map.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch ((view.getId())){
            case R.id.iv_map:
                break;
            default:
                break;
        }
    }
}
