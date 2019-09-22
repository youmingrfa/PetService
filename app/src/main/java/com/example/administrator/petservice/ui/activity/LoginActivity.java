package com.example.administrator.petservice.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.petservice.R;
import com.example.administrator.petservice.lisenter.TextInputWatcher;
import com.example.administrator.petservice.ui.utils.TcpClientConnector;
import com.mob.MobSDK;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.mob.tools.utils.ResHelper.getStringRes;

/**
 * 登录Activity
 * @author rfa
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener  {

    private static final String TAG = "LoginActivity";
    /**
     * The constant LOGIN_RESULT_CODE.
     */
    public static final int LOGIN_RESULT_CODE = 1002;

    /**第三方登录回调标识**/
    private static final int MSG_SMSSDK_CALLBACK = 1;
    private static final int MSG_AUTH_CANCEL = 2;
    private static final int MSG_AUTH_ERROR= 3;
    private static final int MSG_AUTH_COMPLETE = 4;
    private static final int MSG_USERID_FOUND = 5;

    private ImageView mTitleBarIvBack,mQuickLoginIvClearPhoneNumber,mQuickLoginIvClearCode,mIvCheckPicture,mAccountLoginIvClearUsername,mAccountLoginIvClearPassword,mIvCheckCode;
    private ImageView mBottomIvQq,mBottomIvWechat,mBottomIvWeibo,mBottomIvAlipay;
    private TextView mTitleBarTvRegister,mSelectTvQuickLogin,mSelectTvAccountLogin,mAccountLoginTvForgetPassword,tv_tip;
    private View mSelectLeftLine,mSelectRightLine;
    private EditText mQuickLoginEtPhoneNumber,mQuickLoginEtCode,mEtCheckPicture,mAccountLoginEtUsername,mAccountLoginEtPassword,mEtCheckCode;
    private Button mQuickLoginBtnGetCode,mQuickLoginBtn,mAccountLoginBtn;
    private CheckBox mAccountLoginCheckBox;

    private RelativeLayout mLlCheckPicture,mLlCheckCode;
    private LinearLayout mQuickLoginLayout,mAccountLoginLayout;

    private Animation mLeftLineAnimation,mRightLineAnimation;

    private int mSelectedTextColor;
    private int mUnselectedTextColor;

    private TcpClientConnector connector;

    /**快速登陆界面*/
    private boolean isPhoneNumberNull = true;
    private boolean isCodeNull = true;
    /**账号登陆界面*/
    private boolean isUserNameNull = true;
    private boolean isPasswordNull = true;

    private boolean isQuickLoginSelected = true;
    private boolean isAccountLoginSelected = false;
    private int mSecCount;

    private Handler mHandler;
    private int time = 60;
    private boolean flag = true;
    private String iPhone;//手机号码
    private String iCord;//验证码

    BufferedWriter out;
    BufferedReader in;
    private Socket s;
    String str;


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE); // 去除顶部标题栏
        setContentView(R.layout.activity_login);

        initShareSDK();

        initView();
        initAnimation();
        setViewListener();
        registerTime();
    }

    //对于shareSDK的初始化（必要的）
    private void initShareSDK(){
        MobSDK.init(this);
    }

    /**
     * 初始化布局
     */
    private void initView() {
        mTitleBarIvBack = (ImageView) findViewById(R.id.login_titleBar_iv_back);
        mTitleBarTvRegister = (TextView) findViewById(R.id.login_titleBar_tv_register);
        mSelectTvQuickLogin = (TextView) findViewById(R.id.login_select_tv_quickLogin);
        mSelectTvAccountLogin = (TextView) findViewById(R.id.login_select_tv_accountLogin);
        mSelectLeftLine = findViewById(R.id.login_select_left_line);
        mSelectRightLine = findViewById(R.id.login_select_right_line);
        mQuickLoginEtPhoneNumber = (EditText) findViewById(R.id.login_quick_login_et_phoneNumber);
        mQuickLoginIvClearPhoneNumber = (ImageView) findViewById(R.id.login_quick_login_iv_clear_phoneNumber);
        mQuickLoginEtCode = (EditText) findViewById(R.id.login_quick_login_et_code);
        mQuickLoginBtnGetCode = (Button) findViewById(R.id.login_quick_login_btn_getCode);
        mQuickLoginIvClearCode = (ImageView) findViewById(R.id.login_quick_login_iv_clear_code);
        mEtCheckPicture = (EditText) findViewById(R.id.et_check_picture);
        mIvCheckPicture = (ImageView) findViewById(R.id.iv_check_picture);
        mLlCheckPicture = (RelativeLayout) findViewById(R.id.ll_check_picture);
        mQuickLoginLayout = (LinearLayout) findViewById(R.id.login_quick_login_layout);
        mAccountLoginEtUsername = (EditText) findViewById(R.id.login_account_login_et_username);
        mAccountLoginIvClearUsername = (ImageView) findViewById(R.id.login_account_login_iv_clear_username);
        mAccountLoginEtPassword = (EditText) findViewById(R.id.login_account_login_et_password);
        mAccountLoginCheckBox = (CheckBox) findViewById(R.id.login_account_login_checkBox);
        mAccountLoginIvClearPassword = (ImageView) findViewById(R.id.login_account_login_iv_clear_password);
        mEtCheckCode = (EditText) findViewById(R.id.et_check_code);
        mIvCheckCode = (ImageView) findViewById(R.id.iv_check_code);
        mLlCheckCode = (RelativeLayout) findViewById(R.id.ll_check_code);
        mAccountLoginLayout = (LinearLayout) findViewById(R.id.login_account_login_layout);
        mQuickLoginBtn = (Button) findViewById(R.id.login_quick_login_btn);
        mAccountLoginBtn = (Button) findViewById(R.id.login_account_login_btn);
        mAccountLoginTvForgetPassword = (TextView) findViewById(R.id.login_account_login_tv_forget_password);
        mBottomIvQq = (ImageView) findViewById(R.id.login_bottom_iv_qq);
        mBottomIvWechat = (ImageView) findViewById(R.id.login_bottom_iv_wechat);
        mBottomIvWeibo = (ImageView) findViewById(R.id.login_bottom_iv_weibo);
        mBottomIvAlipay = (ImageView) findViewById(R.id.login_bottom_iv_alipay);
        tv_tip = findViewById(R.id.tip);
    }

    /**
     * 设置监听事件
     */
    private void setViewListener() {
        //顶部选择
        mTitleBarIvBack.setOnClickListener(this);
        mTitleBarTvRegister.setOnClickListener(this);
        mSelectTvQuickLogin.setOnClickListener(this);
        mSelectTvAccountLogin.setOnClickListener(this);
        //快速登录
        mQuickLoginIvClearPhoneNumber.setOnClickListener(this);
        mQuickLoginBtnGetCode.setOnClickListener(this);
        mQuickLoginIvClearCode.setOnClickListener(this);
        mQuickLoginBtn.setOnClickListener(this);
        //账号登录
        mAccountLoginIvClearUsername.setOnClickListener(this);
        mAccountLoginIvClearPassword.setOnClickListener(this);
        mAccountLoginBtn.setOnClickListener(this);
        mAccountLoginTvForgetPassword.setOnClickListener(this);
        //第三方登录
        mBottomIvQq.setOnClickListener(this);
        mBottomIvWechat.setOnClickListener(this);
        mBottomIvWeibo.setOnClickListener(this);
        mBottomIvAlipay.setOnClickListener(this);

        mAccountLoginCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                //切换明密文
                if (checked) {
                    mAccountLoginEtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mAccountLoginEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                //光标在末尾显示
                mAccountLoginEtPassword.setSelection(mAccountLoginEtPassword.length());
            }
        });

        mQuickLoginEtPhoneNumber.addTextChangedListener(new TextInputWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                isPhoneNumberNull = TextUtils.isEmpty(mQuickLoginEtPhoneNumber.getText());
                mQuickLoginIvClearPhoneNumber.setVisibility(isPhoneNumberNull ? View.GONE : View.VISIBLE);
                mQuickLoginIvClearPhoneNumber.setEnabled(!isPhoneNumberNull);
                mQuickLoginBtn.setEnabled((isPhoneNumberNull||isCodeNull) ? false : true);
            }
        });

        mQuickLoginEtCode.addTextChangedListener(new TextInputWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                isCodeNull = TextUtils.isEmpty(mQuickLoginEtCode.getText());
                mQuickLoginIvClearCode.setVisibility(isCodeNull ? View.GONE : View.VISIBLE);
                mQuickLoginIvClearCode.setEnabled(!isCodeNull);
                mQuickLoginBtn.setEnabled((isPhoneNumberNull||isCodeNull) ? false : true);
            }
        });

        mAccountLoginEtUsername.addTextChangedListener(new TextInputWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                isUserNameNull = TextUtils.isEmpty(mAccountLoginEtUsername.getText());
                mAccountLoginIvClearUsername.setVisibility(isUserNameNull ? View.GONE : View.VISIBLE);
                mAccountLoginIvClearUsername.setEnabled(!isUserNameNull);
                mAccountLoginBtn.setEnabled((isUserNameNull||isPasswordNull) ? false : true);
            }
        });

        mAccountLoginEtPassword.addTextChangedListener(new TextInputWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                isPasswordNull = TextUtils.isEmpty(mAccountLoginEtPassword.getText());
                mAccountLoginIvClearPassword.setVisibility(isPasswordNull ? View.GONE : View.VISIBLE);
                mAccountLoginIvClearPassword.setEnabled(!isPasswordNull);
                mAccountLoginBtn.setEnabled((isUserNameNull||isPasswordNull) ? false : true);
            }
        });
    }


    /**
     * 初始化动画效果
     */
    private void initAnimation() {
        mSelectedTextColor = getResources().getColor(R.color.app_yellow);
        mUnselectedTextColor = getResources().getColor(R.color.content_color);
        mLeftLineAnimation = AnimationUtils.loadAnimation(this,R.anim.anim_line_move_right);
        mLeftLineAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mSelectTvQuickLogin.setTextColor(mUnselectedTextColor);
                mSelectTvAccountLogin.setTextColor(mSelectedTextColor);
                mQuickLoginLayout.setVisibility(View.GONE);
                mAccountLoginLayout.setVisibility(View.VISIBLE);
                mSelectLeftLine.setVisibility(View.INVISIBLE);
                mSelectRightLine.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mRightLineAnimation = AnimationUtils.loadAnimation(this,R.anim.anim_line_move_left);
        mRightLineAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mSelectTvQuickLogin.setTextColor(mSelectedTextColor);
                mSelectTvAccountLogin.setTextColor(mUnselectedTextColor);
                mQuickLoginLayout.setVisibility(View.VISIBLE);
                mAccountLoginLayout.setVisibility(View.GONE);
                mSelectLeftLine.setVisibility(View.VISIBLE);
                mSelectRightLine.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

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
                    mQuickLoginBtnGetCode.setVisibility(View.VISIBLE);
                }
            }else{
                mQuickLoginBtnGetCode.setText("");
                tv_tip.setText("提示信息");
                time = 60;
                tv_tip.setVisibility(View.GONE);
                mQuickLoginBtnGetCode.setVisibility(View.VISIBLE);
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


//                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                    startActivity(intent);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){//服务器验证码发送成功
                    reminderText();
                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//返回支持发送验证码的国家列表
                    Toast.makeText(getApplicationContext(), "获取国家列表成功", Toast.LENGTH_SHORT).show();
                }
            } else {
                if(flag){
                    mQuickLoginBtnGetCode.setVisibility(View.VISIBLE);
                    Toast.makeText(LoginActivity.this, "验证码获取失败，请重新获取", Toast.LENGTH_SHORT).show();
                    mQuickLoginEtPhoneNumber.requestFocus();
                    mQuickLoginBtnGetCode.setVisibility(View.VISIBLE);
                    tv_tip.setVisibility(View.GONE);
                }else{
                    ((Throwable) data).printStackTrace();
                    int resId = getStringRes(LoginActivity.this, "smssdk_network_error");
                    Toast.makeText(LoginActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    mQuickLoginEtCode.selectAll();
                    if (resId > 0) {
                        Toast.makeText(LoginActivity.this, resId, Toast.LENGTH_SHORT).show();
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

    /**
     * @param v
     * 根据传入的View的Id设置相应的效果
     */
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.login_titleBar_iv_back:

                new Thread(new Runnable(){      //开启线程连接服务端
                    @Override
                    public void run() {
                        try {
                            if(s==null||(!s.isConnected())){
                                s = new Socket("47.100.244.211", 9999);
                            }
                            out=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

                            out.write("rfa");
                            out.newLine();
                            out.flush();
                            Log.d(TAG, "send data");

                            in=new BufferedReader(new InputStreamReader(s.getInputStream()));

                            String str=in.readLine();
                            Log.d(TAG, "get data:"+str);
//                            s.close();
//                    Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                ).start();

//                intent = new Intent(LoginActivity.this,MainActivity.class);
//                startActivity(intent);


                break;
            case R.id.login_titleBar_tv_register:
                intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login_select_tv_quickLogin:
                if (!isQuickLoginSelected) {
                    mSelectRightLine.startAnimation(mRightLineAnimation);
                    isQuickLoginSelected = true;
                    isAccountLoginSelected = false;
                }
                break;
            case R.id.login_select_tv_accountLogin:
                if (!isAccountLoginSelected) {
                    mSelectLeftLine.startAnimation(mLeftLineAnimation);
                    isAccountLoginSelected = true;
                    isQuickLoginSelected = false;
                }
                break;
            case R.id.login_quick_login_iv_clear_phoneNumber:
                mQuickLoginEtPhoneNumber.setText("");
                mQuickLoginIvClearPhoneNumber.setVisibility(View.GONE);
                break;
            case R.id.login_quick_login_iv_clear_code:
                mQuickLoginEtCode.setText("");
                mQuickLoginIvClearCode.setVisibility(View.GONE);
                break;
            case R.id.login_quick_login_btn_getCode:
                if(!TextUtils.isEmpty(mQuickLoginEtPhoneNumber.getText().toString().trim())){
                    if(mQuickLoginEtPhoneNumber.getText().toString().trim().length()==11){
                        iPhone = mQuickLoginEtPhoneNumber.getText().toString().trim();
                        SMSSDK.getVerificationCode("86",iPhone);
                        mQuickLoginEtPhoneNumber.requestFocus();
                        mQuickLoginBtnGetCode.setVisibility(View.GONE);
                        tv_tip.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(LoginActivity.this, "请输入完整电话号码", Toast.LENGTH_LONG).show();
                        mQuickLoginEtPhoneNumber.requestFocus();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "请输入您的电话号码", Toast.LENGTH_LONG).show();
                    mQuickLoginEtPhoneNumber.requestFocus();
                }
                break;
            case R.id.login_quick_login_btn:
                if(!TextUtils.isEmpty(mQuickLoginEtCode.getText().toString().trim())){
                    if(mQuickLoginEtCode.getText().toString().trim().length()==4){
                        iCord = mQuickLoginEtCode.getText().toString().trim();
                        SMSSDK.submitVerificationCode("86", iPhone, iCord);
                        flag = false;
                    }else{
                        Toast.makeText(LoginActivity.this, "请输入完整验证码", Toast.LENGTH_LONG).show();
                        mQuickLoginEtCode.requestFocus();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
                    mQuickLoginEtCode.requestFocus();
                }
                break;
//            case R.id.login_account_login_iv_clear_username:
//                mAccountLoginEtUsername.setText("");
//                mAccountLoginIvClearUsername.setVisibility(View.GONE);
//                break;
            case R.id.login_account_login_iv_clear_password:
                mAccountLoginEtPassword.setText("");
                mAccountLoginIvClearPassword.setVisibility(View.GONE);
                break;
//            case R.id.login_account_login_btn:
//                String username = mAccountLoginEtUsername.getText().toString();
//                String password = mAccountLoginEtPassword.getText().toString();
//                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
//                    BmobManager.getInstance(new BmobLoginCallback() {
//                        @Override
//                        public void onLoginSuccess() {
//                            ToastUtil.show(LoginActivity.this,R.string.login_success);
//                            Intent data = new Intent();
//                            setResult(LOGIN_RESULT_CODE,data);
//                            finish();
//                        }
//                        @Override
//                        public void onLoginFailure() {
//                            ToastUtil.show(LoginActivity.this,R.string.login_failed);
//                        }
//                    }).login(username,password);
//                } else {
//                    ToastUtil.show(this,R.string.login_input_empty);
//                }
//                break;
            case R.id.login_account_login_btn:
                intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.login_account_login_tv_forget_password:
                intent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);
                break;
//            case R.id.login_bottom_iv_qq:
//                Platform qq = ShareSDK.getPlatform(QQ.NAME);
//                authorize(qq);
//                break;
//            case R.id.login_bottom_iv_wechat:
//                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
//                authorize(wechat);
//                break;
//            case R.id.login_bottom_iv_weibo:
//                Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
//                authorize(sina);
//                /*Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
//                weibo.SSOSetting(false);  //设置false表示使用SSO授权方式
//                weibo.setPlatformActionListener(this); // 设置分享事件回调
//                weibo.authorize();//单独授权
//                weibo.showUser(null);//授权并获取用户信息*/
//                break;
            case R.id.login_bottom_iv_alipay:
                break;
            default:
                break;
        }
    }


}
