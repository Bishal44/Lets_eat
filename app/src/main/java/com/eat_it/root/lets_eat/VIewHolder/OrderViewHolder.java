package com.eat_it.root.lets_eat.VIewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.eat_it.root.lets_eat.Interface.Item_clicker;
import com.eat_it.root.lets_eat.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrderid,txtOrderstatus,txtOrderPhone,txtOrderaddress;
    private Item_clicker itemclicklistner;


    public OrderViewHolder(View itemView) {
        super(itemView);

        txtOrderid=itemView.findViewById(R.id.order_id);
        txtOrderstatus=itemView.findViewById(R.id.order_status);
        txtOrderPhone=itemView.findViewById(R.id.order_phone);
        txtOrderaddress=itemView.findViewById(R.id.order_address);

        itemView.setOnClickListener(this);

    }


    public void setItemclicklistner(Item_clicker itemclicklistner) {
        this.itemclicklistner = itemclicklistner;
    }

    @Override
    public void onClick(View view) {
        itemclicklistner.onclick(view,getAdapterPosition(),false);



    }
}
