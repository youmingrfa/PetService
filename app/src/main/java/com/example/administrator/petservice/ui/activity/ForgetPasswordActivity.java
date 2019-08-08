package com.example.administrator.petservice.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.petservice.R;
import com.example.administrator.petservice.widget.VerifyEditView;
import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.mob.tools.utils.ResHelper.getStringRes;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout phoneNumberLayout,checkCodeLayout,passwordLayout;
    private EditText etPhoneNumber;
    private TextView tvTip;
    private Button btnCertainPhoneNumber,btnRegisterCode,btnCertainCheckCode,btnCertainPassword;
    private VerifyEditView chechCode;

    private int time = 60;
    private boolean flag = false;
    private String iPhone;//手机号码
    private String iCord;//验证码


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去除顶部标题栏
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forget_password);

        initShareSDK();
        initView();
        initData();
        initListener();
        registerTime();
    }

    //对于shareSDK的初始化（必要的）
    private void initShareSDK(){
        MobSDK.init(this);
    }

    private void initView(){
        phoneNumberLayout = findViewById(R.id.ll_phone_number);
        checkCodeLayout = findViewById(R.id.ll_check_code_layout);
        passwordLayout = findViewById(R.id.ll_password_layout);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        btnCertainPhoneNumber = findViewById(R.id.btn_certain_phone_number);
        chechCode = findViewById(R.id.check_code);
        btnRegisterCode = findViewById(R.id.register_btn_getCode);
        tvTip = findViewById(R.id.tip);
        btnCertainCheckCode = findViewById(R.id.btn_certain_check_code);
        btnCertainPassword = findViewById(R.id.btn_certain_password);
    }

    private void initData(){

    }

    private void initListener(){
        btnCertainPhoneNumber.setOnClickListener(this);
        btnRegisterCode.setOnClickListener(this);
        btnCertainCheckCode.setOnClickListener(this);
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
                    tvTip.setText("验证码已发送"+time+"秒");
                    time--;
                    handlerText.sendEmptyMessageDelayed(1, 1000);
                }else{
                    tvTip.setText("提示信息");
                    time = 60;
                    tvTip.setVisibility(View.GONE);
                    btnRegisterCode.setVisibility(View.VISIBLE);
                }
            }else{
                chechCode.setText("");
                tvTip.setText("提示信息");
                time = 60;
                tvTip.setVisibility(View.GONE);
                btnRegisterCode.setVisibility(View.VISIBLE);
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
                    flag = true;
                    judgeFlag(flag);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){//服务器验证码发送成功
                    reminderText();
                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//返回支持发送验证码的国家列表
                    Toast.makeText(getApplicationContext(), "获取国家列表成功", Toast.LENGTH_SHORT).show();
                }
            } else {
                if(flag){
                    btnRegisterCode.setVisibility(View.VISIBLE);
                    Toast.makeText(ForgetPasswordActivity.this, "验证码获取失败，请重新获取", Toast.LENGTH_SHORT).show();
                    btnRegisterCode.setVisibility(View.VISIBLE);
                    tvTip.setVisibility(View.GONE);
                }else{
                    ((Throwable) data).printStackTrace();
                    int resId = getStringRes(ForgetPasswordActivity.this, "smssdk_network_error");
                    Toast.makeText(ForgetPasswordActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    chechCode.selectAll();
                    if (resId > 0) {
                        Toast.makeText(ForgetPasswordActivity.this, resId, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    //验证码送成功后提示文字
    private void reminderText() {
        tvTip.setVisibility(View.VISIBLE);
        handlerText.sendEmptyMessageDelayed(1, 1000);
    }

    private void judgeFlag(boolean result){
        if(result){
            checkCodeLayout.setVisibility(View.GONE);
            passwordLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.btn_certain_phone_number:
                iPhone += etPhoneNumber.getText();
                if(!TextUtils.isEmpty(iPhone.trim())){
                    if(etPhoneNumber.getText().toString().trim().length()==11){
                        iPhone = etPhoneNumber.getText().toString().trim();
                        phoneNumberLayout.setVisibility(View.GONE);
                        checkCodeLayout.setVisibility(View.VISIBLE);
                        tvTip.setVisibility(View.GONE);
                    }else{
                        Toast.makeText(ForgetPasswordActivity.this, "请输入完整电话号码", Toast.LENGTH_LONG).show();
                        etPhoneNumber.requestFocus();
                    }
                }else{
                    Toast.makeText(ForgetPasswordActivity.this, "请输入您的电话号码", Toast.LENGTH_LONG).show();
                    etPhoneNumber.requestFocus();
                }
                break;
            case R.id.register_btn_getCode:
                SMSSDK.getVerificationCode("86",iPhone);
                chechCode.requestFocus();
                btnRegisterCode.setVisibility(View.GONE);
                tvTip.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_certain_check_code:
                if(!TextUtils.isEmpty(chechCode.getText().toString().trim())){
                    if(chechCode.getText().toString().trim().length()==4){
                        iCord = chechCode.getText().toString().trim();
                        SMSSDK.submitVerificationCode("86", iPhone, iCord);
                    }else{
                        Toast.makeText(ForgetPasswordActivity.this, "请输入完整验证码", Toast.LENGTH_LONG).show();
                        chechCode.requestFocus();
                    }
                }else{
                    Toast.makeText(ForgetPasswordActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
                    chechCode.requestFocus();
                }
                break;
            case R.id.btn_certain_password:
                intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
