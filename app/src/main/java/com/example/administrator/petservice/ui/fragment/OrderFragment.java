package com.example.administrator.petservice.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.petservice.R;


public class OrderFragment extends Fragment {

	private TextView notice_no_order;
	private RecyclerView order_list;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_order, container, false);

		initView(view);

		return view;
	}

	private void initView(View view){

		notice_no_order = view.findViewById(R.id.tv_notice);
		order_list = view.findViewById(R.id.order);

	}

	private void initData(){
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

	}
	
}
