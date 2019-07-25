package com.example.administrator.petservice.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.administrator.petservice.R;
import com.example.administrator.petservice.lisenter.TextInputWatcher;
import com.example.administrator.petservice.ui.MainActivity;

/**
 * 登录Activity
 * @author rfa
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener  {

    private static final String TAG = LoginActivity.class.getSimpleName();
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

    private ImageView mTitleBarIvBack;
    private TextView mTitleBarTvRegister;
    private TextView mSelectTvQuickLogin;
    private TextView mSelectTvAccountLogin;
    private View mSelectLeftLine;
    private View mSelectRightLine;
    private EditText mQuickLoginEtPhoneNumber;
    private ImageView mQuickLoginIvClearPhoneNumber;
    private EditText mQuickLoginEtCode;
    private Button mQuickLoginBtnGetCode;
    private ImageView mQuickLoginIvClearCode;
    private EditText mEtCheckPicture;
    private ImageView mIvCheckPicture;
    private RelativeLayout mLlCheckPicture;
    private LinearLayout mQuickLoginLayout;
    private EditText mAccountLoginEtUsername;
    private ImageView mAccountLoginIvClearUsername;
    private EditText mAccountLoginEtPassword;
    private CheckBox mAccountLoginCheckBox;
    private ImageView mAccountLoginIvClearPassword;
    private EditText mEtCheckCode;
    private ImageView mIvCheckCode;
    private RelativeLayout mLlCheckCode;
    private LinearLayout mAccountLoginLayout;
    private Button mQuickLoginBtn;
    private Button mAccountLoginBtn;
    private TextView mAccountLoginTvForgetPassword;
    private ImageView mBottomIvQq;
    private ImageView mBottomIvWechat;
    private ImageView mBottomIvWeibo;
    private ImageView mBottomIvAlipay;

    private Animation mLeftLineAnimation;
    private Animation mRightLineAnimation;

    private int mSelectedTextColor;
    private int mUnselectedTextColor;

    /**快速登陆界面*/
    private boolean isPhoneNumberNull = true;
    private boolean isCodeNull = true;
    /**账号登陆界面*/
    private boolean isUserNameNull = true;
    private boolean isPasswordNull = true;

    private boolean isQuickLoginSelected = true;
    private boolean isAccountLoginSelected = false;
    private int mSecCount;
    private String mPhoneNumber;

    private Handler mHandler;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE); // 去除顶部标题栏
        setContentView(R.layout.activity_login);

        initView();
        initAnimation();
        setViewListener();

    }

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
    }

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

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.login_titleBar_iv_back:
                intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
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
//            case R.id.login_quick_login_btn_getCode:
//                mPhoneNumber = mQuickLoginEtPhoneNumber.getText().toString();
//                if (LoginHelperUtil.isPhoneNumber(mPhoneNumber)) {
//                    BmobManager.getInstance(new BmobMsgSendCallback() {
//                        @Override
//                        public void onMsgSendSuccess() {
//                            ToastUtil.show(LoginActivity.this,R.string.sms_code_send_success);
//                            //验证码发送成功，倒计时
//                            setCodeTimeDown();
//                        }
//
//                        @Override
//                        public void onMsgSendFailure() {
//                            ToastUtil.show(LoginActivity.this,R.string.sms_code_send_failure);
//                        }
//                    }).sendMsgCode(mPhoneNumber);
//                } else {
//                    ToastUtil.show(this,R.string.phone_number_incorrect);
//                }
//                break;
            case R.id.login_quick_login_btn:
                intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
//                mPhoneNumber = mQuickLoginEtPhoneNumber.getText().toString();
//                String code = mQuickLoginEtCode.getText().toString();
//                if (LoginHelperUtil.isCodeCorrect(code) && LoginHelperUtil.isPhoneNumber(mPhoneNumber)) {
//                    BmobManager.getInstance(new BmobLoginCallback() {
//                        @Override
//                        public void onLoginSuccess() {
//                            Log.i(TAG, "onLoginSuccess: 登陆成功");
//                            ToastUtil.show(LoginActivity.this,R.string.login_success);
//                        }
//
//                        @Override
//                        public void onLoginFailure() {
//                            Log.i(TAG, "onLoginFailure: 登陆失败");
//                            ToastUtil.show(LoginActivity.this,R.string.login_failed);
//                        }
//                    }).signOrLoginByMsgCode(mPhoneNumber,code);
//                } else {
//                    ToastUtil.showLong(this,R.string.quick_login_input_incorrect);
//                }
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
            case R.id.login_account_login_tv_forget_password:

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
