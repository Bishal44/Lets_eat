package com.eat_it.root.lets_eat.VIewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eat_it.root.lets_eat.Interface.Item_clicker;
import com.eat_it.root.lets_eat.R;

public class Foodview_holder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView txtfoodname;
    public ImageView food_image;
    private Item_clicker item_clicker;


    public void setItem_clicker(Item_clicker item_clicker) {
        this.item_clicker = item_clicker;
    }

    public Foodview_holder(View itemView) {
        super(itemView);


        txtfoodname=(TextView)itemView.findViewById(R.id.food_item);
        food_image=(ImageView)itemView.findViewById(R.id.food_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        item_clicker.onclick(view,getAdapterPosition(),false);
    }
}
