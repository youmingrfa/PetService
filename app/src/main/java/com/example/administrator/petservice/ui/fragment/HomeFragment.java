package com.example.administrator.petservice.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.administrator.petservice.R;
import com.example.administrator.petservice.ui.activity.CareActivity;
import com.example.administrator.petservice.ui.activity.HistorySearchActivity;
import com.example.administrator.petservice.ui.activity.MedicalActivity;
import com.example.administrator.petservice.ui.activity.StoreActivity;
import com.example.administrator.petservice.ui.activity.WashActivity;
import com.example.administrator.petservice.widget.BannerLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import static com.example.administrator.petservice.ui.utils.ImageUtil.imageUrls;

/**
 * @author rfa
 * HomeFragment是主界面的第一个模块
 */
public class HomeFragment extends Fragment implements View.OnClickListener{

	private LinearLayout searchBar,medicalModule,washModule,careModule,storeModule;
	private Banner banner;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_home, container, false);

		initView(view);
		initData();
		setClickListener();

		return view;
	}

	private void initView(View view) {
		searchBar = view.findViewById(R.id.titleBar_search_ll);
		banner = view.findViewById(R.id.banner);
		medicalModule = view.findViewById(R.id.medical_module);
		washModule = view.findViewById(R.id.wash_module);
		careModule = view.findViewById(R.id.care_module);
		storeModule = view.findViewById(R.id.store_module);

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
		searchBar.setOnClickListener(this);

		//2*2模块的点击事件
		medicalModule.setOnClickListener(this);
		washModule.setOnClickListener(this);
		careModule.setOnClickListener(this);
		storeModule.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch ((v.getId())){
			case R.id.titleBar_search_ll:
				intent = new Intent(getActivity(),HistorySearchActivity.class);
				startActivity(intent);
				break;
			case R.id.medical_module:
				intent = new Intent(getActivity(),MedicalActivity.class);
				startActivity(intent);
				break;
			case R.id.wash_module:
				intent = new Intent(getActivity(),WashActivity.class);
				startActivity(intent);
				break;
			case R.id.care_module:
				intent = new Intent(getActivity(),CareActivity.class);
				startActivity(intent);
				break;
			case R.id.store_module:
				intent = new Intent(getActivity(),StoreActivity.class);
				startActivity(intent);
				break;
			default:
				break;
		}
	}
	
}
