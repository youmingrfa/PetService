package com.example.administrator.petservice.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.administrator.petservice.R;

import java.util.ArrayList;
import java.util.List;

public class HotSearchAdapter extends RecyclerView.Adapter<HotSearchAdapter.ViewHolder>{

    Context mContext;
    List<String> list = new ArrayList<>();

    public HotSearchAdapter(Context context){
        mContext = context;
    }

    @Override
    public HotSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.hot_search_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HotSearchAdapter.ViewHolder viewHolder, int i) {
        list.add("宠物");
        list.add("宠物");
        list.add("宠物");
        list.add("宠物");

        viewHolder.btn_item.setText(list.get(i));

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private Button btn_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_item = itemView.findViewById(R.id.btn_hot_search_item);
        }
    }
}
