package com.example.administrator.petservice.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.petservice.R;

public class RecommedFoodAdapter extends RecyclerView.Adapter<RecommedFoodAdapter.ViewHolder> {

    private Context mContext;

    public RecommedFoodAdapter(Context context){
        mContext = context;
    }

    @Override
    public RecommedFoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_item,viewGroup,false);
        RecommedFoodAdapter.ViewHolder viewHolder = new RecommedFoodAdapter.ViewHolder(view);
        return viewHolder;
    }

    /**
     * @param viewHolder
     * @param i
     * 在这里是设置具体的内容
     */
    @Override
    public void onBindViewHolder(RecommedFoodAdapter.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView foodImg;
        private TextView title;
        private TextView description;
        private TextView price;
        private TextView value;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImg = itemView.findViewById(R.id.good_image);
            title = itemView.findViewById(R.id.good_tv_title);
            description = itemView.findViewById(R.id.good_tv_description);
            price = itemView.findViewById(R.id.good_tv_price);
            value = itemView.findViewById(R.id.good_tv_value);
//            value.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
        }
    }

}
