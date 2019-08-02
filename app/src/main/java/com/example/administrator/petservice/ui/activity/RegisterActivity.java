package com.example.administrator.petservice.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.petservice.R;
import com.example.administrator.petservice.lisenter.TextInputWatcher;
import com.mob.MobSDK;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.mob.tools.utils.ResHelper.getStringRes;


/**
 * 注册Activity
 * @author rfa
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView register_back;
    private EditText et_phone_number;
    private ImageView btn_clear_phone_number;
    private EditText et_register_code;
    private Button btn_register_code;
    private ImageView btn_clean_register_code;
    private TextView tv_tip;
    private Button btn_save_info;

    private int time = 60;
    private boolean flag = true;
    private String iPhone;//手机号码
    private String iCord;//验证码


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE); // 去除顶部标题栏
        setContentView(R.layout.activity_register);

        initShareSDK();
        initView();
        initData();
        initTextWatch();
        registerTime();
    }

    //对于shareSDK的初始化（必要的）
    private void initShareSDK(){
        MobSDK.init(this);
    }


    private void initView(){
        register_back = findViewById(R.id.register_titleBar_iv_back);
        et_phone_number = findViewById(R.id.register_et_phoneNumber);
        btn_clear_phone_number = findViewById(R.id.register_iv_clear_phoneNumber);
        et_register_code = findViewById(R.id.register_et_code);
        btn_register_code = findViewById(R.id.register_btn_getCode);
        btn_clean_register_code = findViewById(R.id.register_iv_clear_code);
        tv_tip = findViewById(R.id.tip);
        btn_save_info = findViewById(R.id.save_info);
    }

    private void initData(){
        register_back.setOnClickListener(this);
        btn_clear_phone_number.setOnClickListener(this);
        btn_register_code.setOnClickListener(this);
        btn_clean_register_code.setOnClickListener(this);
        btn_save_info.setOnClickListener(this);
    }

    //对于EditText的监听事件
    private void initTextWatch(){
        //为输入手机号的EditText添加监听事件
        et_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!et_phone_number.getText().equals("")){
                    btn_clear_phone_number.setVisibility(View.VISIBLE);
                }
                else{
                    btn_clear_phone_number.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et_phone_number.getText().equals("")){
                    btn_clear_phone_number.setVisibility(View.GONE);
                }
            }
        });
        //为输入的验证码EditText设置监听事件
        et_register_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!et_register_code.getText().equals("")){
                    btn_clean_register_code.setVisibility(View.VISIBLE);
                }
                else{
                    btn_clean_register_code.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et_register_code.getText().equals("")){
                    btn_clean_register_code.setVisibility(View.GONE);
                }
            }
        });
    }

    //实现验证信息的时间限制
    private void registerTime(){

        EventHandler eh=new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = Message.obtain();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eh);
    }


    Handler handlerText = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                if(time>0){
                    tv_tip.setText("验证码已发送"+time+"秒");
                    time--;
                    handlerText.sendEmptyMessageDelayed(1, 1000);
                }else{
                    tv_tip.setText("提示信息");
                    time = 60;
                    tv_tip.setVisibility(View.GONE);
                    btn_register_code.setVisibility(View.VISIBLE);
                }
            }else{
                et_register_code.setText("");
                tv_tip.setText("提示信息");
                time = 60;
                tv_tip.setVisibility(View.GONE);
                btn_register_code.setVisibility(View.VISIBLE);
            }
        }
    };

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event="+event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                //短信注册成功后，返回Activity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功,验证通过
                    Toast.makeText(getApplicationContext(), "验证码校验成功", Toast.LENGTH_SHORT).show();
                    handlerText.sendEmptyMessage(2);

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){//服务器验证码发送成功
                    reminderText();
                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//返回支持发送验证码的国家列表
                    Toast.makeText(getApplicationContext(), "获取国家列表成功", Toast.LENGTH_SHORT).show();
                }
            } else {
                if(flag){
                    btn_register_code.setVisibility(View.VISIBLE);
                    Toast.makeText(RegisterActivity.this, "验证码获取失败，请重新获取", Toast.LENGTH_SHORT).show();
                    et_phone_number.requestFocus();
                    btn_register_code.setVisibility(View.VISIBLE);
                    tv_tip.setVisibility(View.GONE);
                }else{
                    ((Throwable) data).printStackTrace();
                    int resId = getStringRes(RegisterActivity.this, "smssdk_network_error");
                    Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    et_register_code.selectAll();
                    if (resId > 0) {
                        Toast.makeText(RegisterActivity.this, resId, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    //验证码送成功后提示文字
    private void reminderText() {
        tv_tip.setVisibility(View.VISIBLE);
        handlerText.sendEmptyMessageDelayed(1, 1000);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            //返回按钮
            case R.id.register_titleBar_iv_back:
                finish();
                break;
                //清空输入的手机号码
            case R.id.register_iv_clear_phoneNumber:
                et_phone_number.setText("");
                btn_clear_phone_number.setVisibility(View.GONE);
                break;
                //短信注册码的发送
            case R.id.register_btn_getCode:
                if(!TextUtils.isEmpty(et_phone_number.getText().toString().trim())){
                    if(et_phone_number.getText().toString().trim().length()==11){
                        iPhone = et_phone_number.getText().toString().trim();
                        SMSSDK.getVerificationCode("86",iPhone);
                        et_register_code.requestFocus();
                        btn_register_code.setVisibility(View.GONE);
                        tv_tip.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(RegisterActivity.this, "请输入完整电话号码", Toast.LENGTH_LONG).show();
                        et_phone_number.requestFocus();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "请输入您的电话号码", Toast.LENGTH_LONG).show();
                    et_phone_number.requestFocus();
                }
                break;
                //清空输入的注册码
            case R.id.register_iv_clear_code:
                et_register_code.setText("");
                btn_clean_register_code.setVisibility(View.GONE);
                break;
                //注册按钮
            case R.id.save_info:
                if(!TextUtils.isEmpty(et_register_code.getText().toString().trim())){
                    if(et_register_code.getText().toString().trim().length()==4){
                        iCord = et_register_code.getText().toString().trim();
                        SMSSDK.submitVerificationCode("86", iPhone, iCord);
                        flag = false;
                    }else{
                        Toast.makeText(RegisterActivity.this, "请输入完整验证码", Toast.LENGTH_LONG).show();
                        et_register_code.requestFocus();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
                    et_register_code.requestFocus();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
}
