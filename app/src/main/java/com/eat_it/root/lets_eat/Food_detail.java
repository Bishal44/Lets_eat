package com.eat_it.root.lets_eat;

import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import com.eat_it.root.lets_eat.common.common;
import com.eat_it.root.lets_eat.database.Database;
import com.eat_it.root.lets_eat.model.Food;
import com.eat_it.root.lets_eat.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Food_detail extends AppCompatActivity {
    TextView foodName,foodPrice,foodDescription;
    ImageView selected_food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btncart;
    ElegantNumberButton numberButton;

    String food_id="";

    FirebaseDatabase database;
    DatabaseReference foods;
    Food currentfood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        database=FirebaseDatabase.getInstance();
        foods=database.getReference("Foods");

        numberButton=(ElegantNumberButton)findViewById(R.id.number_button);
        btncart=(FloatingActionButton)findViewById(R.id.btncart);

        foodName=findViewById(R.id.selected_food_name);
        foodDescription=findViewById(R.id.food_description);
        foodPrice=findViewById(R.id.selected_food_price);
        selected_food_image=findViewById(R.id.img_selected_food);

        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing);
        //yesko lagi style define garnu parxa
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAppBar);


        if(getIntent() != null){
            food_id=getIntent().getStringExtra("foodId");//food list batw ligeko
            if(!food_id.isEmpty()){
                if(common.Isconnectedtointernet(getBaseContext())) {
                    getDetailFood(food_id);
                }else{

                    Toast.makeText(Food_detail.this, "Check your Internet connection !!", Toast.LENGTH_LONG).show();
                    return;
                }

            }

        }

        btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addtocart(new Order(food_id,currentfood.getName(),numberButton.getNumber(),
                        currentfood.getDiscount(),currentfood.getPrice()));

                Toast.makeText(Food_detail.this, "Added to cart", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getDetailFood(String food_id) {
        foods.child(food_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentfood=dataSnapshot.getValue(Food.class);
                Picasso.with(getBaseContext()).load(currentfood.getImage())
                        .into(selected_food_image);


                collapsingToolbarLayout.setTitle(currentfood.getName());

                foodName.setText(currentfood.getName());
                foodPrice.setText(currentfood.getPrice());
               foodDescription.setText(currentfood.getDescription());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.refresh)
           getDetailFood(food_id);


        return super.onOptionsItemSelected(item);
    }
}
