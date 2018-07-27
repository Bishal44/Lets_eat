package com.eat_it.root.lets_eat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.eat_it.root.lets_eat.VIewHolder.OrderViewHolder;
import com.eat_it.root.lets_eat.common.common;
import com.eat_it.root.lets_eat.model.Request;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Order_status extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager manager;

    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);



        firebaseDatabase=FirebaseDatabase.getInstance();
        requests=firebaseDatabase.getReference("Requests");


        recyclerView=findViewById(R.id.listorder);
        recyclerView.setHasFixedSize(true);
       manager=new LinearLayoutManager(this);
       recyclerView.setLayoutManager(manager);


       loadorders(common.currentuser.getPhone());




    }

    private void loadorders(String phone) {
        adapter= new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("phone").equalTo(phone))
        {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {

                viewHolder.txtOrderid.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderstatus.setText(convertcodetostatus(model.getStatus()));
                viewHolder.txtOrderPhone.setText(model.getPhone());
                viewHolder.txtOrderaddress.setText(model.getAddress());


            }
        };
        recyclerView.setAdapter(adapter);


    }

    private String convertcodetostatus(String status) {
        if(status.equals("0")){
            return "Placed";

        }else if (status.equals("1")){
            return "On the way";
        }else {
            return "Shipped";
        }
    }
}
