package com.eat_it.root.lets_eat.VIewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.eat_it.root.lets_eat.Interface.Item_clicker;
import com.eat_it.root.lets_eat.R;
import com.eat_it.root.lets_eat.common.common;
import com.eat_it.root.lets_eat.model.Order;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnCreateContextMenuListener /*second for delete cart*/{
    TextView Txtcart_name,cart_item_price;
    ImageView image_cart_count;
    private Item_clicker itemClickListner;


    public void setTxtcart_name(TextView txtcart_name) {
        Txtcart_name = txtcart_name;
    }

    public CartViewHolder(View itemView) {
        super(itemView);

        Txtcart_name=itemView.findViewById(R.id.card_item_name);
        cart_item_price=itemView.findViewById(R.id.card_item_price);
        image_cart_count=(ImageView)itemView.findViewById(R.id.cart_item_count);

        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    @Override //for delete cart
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        contextMenu.setHeaderTitle("select your action");
        contextMenu.add(0,0,getAdapterPosition(), common.DELETE);

    }
}
public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{

    private List<Order> listData=new ArrayList<>();
    private Context context;


    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View itemView=layoutInflater.inflate(R.layout.cart_layout,parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {

        Log.i("position", "onBindViewHolder: "+position);

        TextDrawable drawable=TextDrawable.builder().buildRound(""+listData.get(position).getQuantity(), Color.RED);

        holder.image_cart_count.setImageDrawable(drawable);
        Locale locale=new Locale("en","us");
        NumberFormat format=NumberFormat.getCurrencyInstance(locale);
        int price=(Integer.parseInt(listData.get(position).getPrice()))*(Integer.parseInt(listData.get(position).getQuantity()));

        holder.cart_item_price.setText(format.format(price));
        holder.Txtcart_name.setText(listData.get(position).getProductName());


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
