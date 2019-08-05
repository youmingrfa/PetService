package com.example.administrator.petservice.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.administrator.petservice.R;
import com.example.administrator.petservice.ui.adapter.OrderTabAdapter;

import java.util.ArrayList;
import java.util.List;


public class OrderFragment extends Fragment {


	private TextView notice_no_order;
	private ViewPager viewPager;
	private TabLayout tabLayout;
	private OrderTabAdapter orderTabAdapter;
	private android.support.v7.widget.Toolbar toolbar;
	private List<String> titles = new ArrayList<>();
	private List<Fragment> fragments = new ArrayList<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_order, container, false);

		initView(view);
		initData();

		return view;
	}

	private void initView(View view){
		toolbar = view.findViewById(R.id.toolbar);
		tabLayout = view.findViewById(R.id.tablayout);
		viewPager = view.findViewById(R.id.viewPager);
		notice_no_order = view.findViewById(R.id.tv_notice);


	}

	private void initData(){

		//填充menu
		toolbar.inflateMenu(R.menu.menu_order_toolbar);

		//设置点击事件
		toolbar.setOnMenuItemClickListener(new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				switch(menuItem.getItemId()){
					case R.id.action_search:
						Toast.makeText(getActivity(), "toolbar search", Toast.LENGTH_SHORT).show();
						return true;
					default:
						break;
				}
				return false;
			}
		});


		titles.add("全部");
		titles.add("待付款");
		titles.add("待使用");
		titles.add("评价");
		titles.add("退款/售后");

		orderTabAdapter = new OrderTabAdapter(getActivity().getSupportFragmentManager(),fragments,titles);

		fragments.add(new TotalFragment());
		fragments.add(new WaitPayFragment());
		fragments.add(new WaitUseFragment());
		fragments.add(new EvaluateFragment());
		fragments.add(new DrawbackFragment());
		orderTabAdapter=new OrderTabAdapter(getActivity().getSupportFragmentManager(),fragments,titles);
		viewPager.setAdapter(orderTabAdapter);
		//将ViewPager和TabLayout绑定
		tabLayout.setupWithViewPager(viewPager);

	}


}
