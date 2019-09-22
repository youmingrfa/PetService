package com.example.administrator.petservice.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.petservice.R;
import com.example.administrator.petservice.db.RecordsDao;
import com.example.administrator.petservice.ui.adapter.HotSearchAdapter;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 历史搜索Activity，主要是借用了sqlite数据库存储搜索的信息，需要改进的地方是点击搜索后需要跳转到相应的商品界面，
 * 并且需要把搜索框中的内容设置为空，此活动应该是通过点击MainActivity上方的Button进入的
 * @author rfa
 */
public class HistorySearchActivity extends AppCompatActivity implements View.OnClickListener {

    private RecordsDao mRecordsDao;
    //默然展示词条个数
    private final int DEFAULT_RECORD_NUMBER = 10;
    private List<String> recordList = new ArrayList<>();
    private String username;

    private LinearLayout mHistoryContent;
    private TagFlowLayout tagFlowLayout;

    private ImageView clearAllRecords,clearSearch,backButton,moreArrow;
    private TextView search;
    private EditText editText;
    private RecyclerView hotSearch;

    private TagAdapter mRecordsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE); // 去除顶部标题栏
        setContentView(R.layout.activity_history_search);


        initView();
        initData();
        setClickListener();

        mRecordsDao.setNotifyDataChanged(new RecordsDao.NotifyDataChanged() {
            @Override
            public void notifyDataChanged() {
                initData();
            }
        });
    }

    private void initView(){
        //默认账号
        username = "007";
        //初始化数据库
        mRecordsDao = new RecordsDao(this, username);
        //初始化控件
        editText = findViewById(R.id.edit_query);
        tagFlowLayout = findViewById(R.id.fl_search_records);
        clearAllRecords = findViewById(R.id.clear_all_records);
        moreArrow = findViewById(R.id.iv_arrow);
        search = findViewById(R.id.iv_search);
        clearSearch = findViewById(R.id.iv_clear_search);
        mHistoryContent = findViewById(R.id.ll_history_content);
        backButton = findViewById(R.id.iv_back);

        hotSearch = findViewById(R.id.rv_hot_search);
    }

    private void initData() {
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                emitter.onNext(mRecordsDao.getRecordsByNumber(DEFAULT_RECORD_NUMBER));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> s) throws Exception {
                        recordList.clear();
                        recordList = s;
                        if (null == recordList || recordList.size() == 0) {
                            mHistoryContent.setVisibility(View.GONE);
                        } else {
                            mHistoryContent.setVisibility(View.VISIBLE);
                        }
                        if (mRecordsAdapter != null) {
                            mRecordsAdapter.setData(recordList);
                            mRecordsAdapter.notifyDataChanged();
                        }
                    }
                });

        //创建历史标签适配器,为标签设置对应的内容
        mRecordsAdapter = new TagAdapter<String>(recordList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(HistorySearchActivity.this).inflate(R.layout.tv_history,
                        tagFlowLayout, false);
                //为标签设置对应的内容
                tv.setText(s);
                return tv;
            }
        };

        tagFlowLayout.setAdapter(mRecordsAdapter);
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public void onTagClick(View view, int position, FlowLayout parent) {
                //清空editText之前的数据
                editText.setText("");
                //将获取到的字符串传到搜索结果界面,点击后搜索对应条目内容
                editText.setText(recordList.get(position));
                editText.setSelection(editText.length());
            }
        });
        //删除某个条目
        tagFlowLayout.setOnLongClickListener(new TagFlowLayout.OnLongClickListener() {
            @Override
            public void onLongClick(View view, final int position) {
                showDialog("确定要删除该条历史记录？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除某一条记录
                        mRecordsDao.deleteRecord(recordList.get(position));
                    }
                });
            }
        });

        //view加载完成时回调
        tagFlowLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                boolean isOverFlow = tagFlowLayout.isOverFlow();
                boolean isLimit = tagFlowLayout.isLimit();
                if (isLimit && isOverFlow) {
                    moreArrow.setVisibility(View.VISIBLE);
                } else {
                    moreArrow.setVisibility(View.GONE);
                }
            }
        });
        final StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        hotSearch.setLayoutManager(layoutManager);

        HotSearchAdapter hotSearchAdapter = new HotSearchAdapter(HistorySearchActivity.this);
        hotSearchAdapter.notifyDataSetChanged();
        hotSearch.setAdapter(hotSearchAdapter);

    }

    private void setClickListener(){
        moreArrow.setOnClickListener(this);
        clearAllRecords.setOnClickListener(this);
        search.setOnClickListener(this);
        clearSearch.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    private void showDialog(String dialogTitle, @NonNull DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HistorySearchActivity.this);
        builder.setMessage(dialogTitle);
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_arrow:
                tagFlowLayout.setLimit(false);
                mRecordsAdapter.notifyDataChanged();
                break;
            //清除所有记录
            case R.id.clear_all_records:
                showDialog("确定要删除全部历史记录？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tagFlowLayout.setLimit(true);
                        //清除所有数据
                        mRecordsDao.deleteUsernameAllRecords();
                    }
                });
                break;
            //添加数据
            case R.id.iv_search:
                String record = editText.getText().toString();
                if (!TextUtils.isEmpty(record)) {
                    mRecordsDao.addRecords(record);
                }
                break;
            //清除搜索历史
            case R.id.iv_clear_search:
                editText.setText("");
                break;
            //点击返回按钮，返回主界面
            case R.id.iv_back:
                Intent intent = new Intent(HistorySearchActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mRecordsDao.closeDatabase();
        mRecordsDao.removeNotifyDataChanged();
        super.onDestroy();
    }
}
