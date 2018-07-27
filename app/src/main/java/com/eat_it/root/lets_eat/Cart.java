package com.eat_it.root.lets_eat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eat_it.root.lets_eat.VIewHolder.CartAdapter;

import com.eat_it.root.lets_eat.common.common;
import com.eat_it.root.lets_eat.database.Database;
import com.eat_it.root.lets_eat.model.Order;
import com.eat_it.root.lets_eat.model.Request;
import com.google.android.gms.common.internal.service.Common;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView totalprice;
    Button placeorder;
    List<Order> cart=new ArrayList<>();
    CartAdapter adapter;

    FirebaseDatabase database;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        totalprice=findViewById(R.id.total);
        placeorder=findViewById(R.id.placeorder);


        database =FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");


        recyclerView=findViewById(R.id.listcart);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cart.size()>0) {
                    showAlertdialog();
                }else {
                    Toast.makeText(Cart.this, "Your's cart is Empty", Toast.LENGTH_LONG).show();
                }
            }
        });



        loadlistfood();
    }

    private void showAlertdialog() {

        AlertDialog.Builder alertdialog=new AlertDialog.Builder(Cart.this);
        alertdialog.setTitle("One more step !");
        alertdialog.setMessage("Enter your Address :");
        final EditText editAddress=new EditText(Cart.this);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT

        );
        editAddress.setLayoutParams(lp);
        alertdialog.setView(editAddress);
        alertdialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        alertdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Request request=new Request(
                        common.currentuser.getPhone(),
                        common.currentuser.getName(),
                        editAddress.getText().toString(),
                        totalprice.getText().toString(),
                        cart

                );

                //firebase ma data send garnw
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                new Database(getBaseContext()).clearcart();
                Toast.makeText(Cart.this, "Thank you, visit again", Toast.LENGTH_SHORT).show();
                finish();

            }
        });


        alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertdialog.show();

        }

    private void loadlistfood() {

        cart=new Database(this).getcart();
        adapter=new CartAdapter(cart,this);

        adapter.notifyDataSetChanged();//for after delete refresh
        recyclerView.setAdapter(adapter);


        //total money
        int total=0;
        for (Order order:cart){
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
            Locale locale=new Locale("en","us");
            NumberFormat format=NumberFormat.getCurrencyInstance(locale);


            totalprice.setText(format.format(total));
        }
    }


    //for delete cart  //ctrl+o press gerer function search garne
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(common.DELETE)){
            deleteCart(item.getOrder());
        }

        return true;
    }

    private void deleteCart(int position) {

        //delete garda item ko position use gareko
        cart.remove(position);//delete all data from sqllite
        new Database(this).clearcart();

        //then upadate new database
        for (Order item:cart){
            new Database(this).addtocart(item);

        }

        //refresh
        loadlistfood();



    }
}
