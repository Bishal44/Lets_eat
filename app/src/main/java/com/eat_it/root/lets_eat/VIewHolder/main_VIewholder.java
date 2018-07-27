package com.eat_it.root.lets_eat.VIewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eat_it.root.lets_eat.Interface.Item_clicker;
import com.eat_it.root.lets_eat.R;

public class main_VIewholder extends RecyclerView.ViewHolder implements View.OnClickListener{


    public TextView txtMenuname;
    public ImageView image;
    private Item_clicker item_clicker;


    public main_VIewholder(View itemView) {
        super(itemView);

        txtMenuname=(TextView)itemView.findViewById(R.id.name_menu);
        image=(ImageView)itemView.findViewById(R.id.menu_image);

        itemView.setOnClickListener(this);
    }

    public void setItem_clicker(Item_clicker item_clicker) {
        this.item_clicker = item_clicker;
    }

    @Override
    public void onClick(View view) {

        item_clicker.onclick(view,getAdapterPosition(),false);
    }
}
