package com.example.administrator.petservice.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.administrator.petservice.R;
import com.example.administrator.petservice.ui.activity.HistorySearchActivity;
import com.example.administrator.petservice.widget.BannerLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import static com.example.administrator.petservice.ui.utils.ImageUtil.imageUrls;

public class HomeFragment extends Fragment implements View.OnClickListener{

	private Button searchBtn;
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
		searchBtn = view.findViewById(R.id.search_btn);
		banner = view.findViewById(R.id.banner);
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
				intent = new Intent(getActivity(),HistorySearchActivity.class);
				startActivity(intent);
				break;
			default:
				break;
		}
	}
	
}
