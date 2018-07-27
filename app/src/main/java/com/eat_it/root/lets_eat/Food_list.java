package com.eat_it.root.lets_eat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.eat_it.root.lets_eat.Interface.Item_clicker;
import com.eat_it.root.lets_eat.VIewHolder.Foodview_holder;
import com.eat_it.root.lets_eat.common.common;
import com.eat_it.root.lets_eat.model.Food;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Food_list extends AppCompatActivity {


    RecyclerView recyclerfood;
    RecyclerView.LayoutManager Layoutmanager;
    FirebaseDatabase firebaseDatabase;
    FirebaseRecyclerAdapter<Food,Foodview_holder> adapter;
    DatabaseReference foodlist;
    String categoryId="";

    FirebaseRecyclerAdapter<Food,Foodview_holder> searchadapter;  //for searching
    List<String> suggesteditemlist =new ArrayList<>();
    MaterialSearchBar materialSearchBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);


        firebaseDatabase=FirebaseDatabase.getInstance();
        foodlist=firebaseDatabase.getReference("Foods");


        //yo chai load the view
        recyclerfood=(RecyclerView)findViewById(R.id.Recycler_food);

        recyclerfood.setHasFixedSize(true);
        Layoutmanager=new LinearLayoutManager(this);
        recyclerfood.setLayoutManager(Layoutmanager);

        if(getIntent()!=null){
            categoryId=getIntent().getStringExtra("categoryid");

            if(!categoryId.isEmpty() && categoryId !=null){

                if(common.Isconnectedtointernet(this)) {
                    loadlistfood(categoryId);
                }else{

                    Toast.makeText(Food_list.this, "Check your Internet connection !!", Toast.LENGTH_LONG).show();
                    return;
                }

            }else {
                Toast.makeText(this, "Not avalible right now", Toast.LENGTH_SHORT).show();
            }
        }

        materialSearchBar=(MaterialSearchBar)findViewById(R.id.foodsearchbar);
        materialSearchBar.setHint("Enter your Food");
        //materialSearchBar.setSpeechMode(false);
        loadSuggest();   //load the previously Search food from firebase

        materialSearchBar.setLastSuggestions(suggesteditemlist);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {     //auto generate
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> suggest=new ArrayList<String>();
                for(String search:suggesteditemlist){
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);

                    }
                    materialSearchBar.setLastSuggestions(suggest);
                //recent search add gareko

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });//text change garda suggestion change garne
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //search bar close garyo bhane pahele ko food list load garnw
                if(!enabled){
                    recyclerfood.setAdapter(adapter);
                }

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //search confirm bhayepaxi result show garnw

                startsearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });//main search ko lagi

    }


    private void loadlistfood(String categoryId) {

        adapter= new FirebaseRecyclerAdapter<Food, Foodview_holder>(Food.class,
             R.layout.food_item, Foodview_holder.class,
                foodlist.orderByChild("MenuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(Foodview_holder viewHolder, Food model, int position) {

                viewHolder.txtfoodname.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.food_image);
                final Food local=model;
                viewHolder.setItem_clicker(new Item_clicker() {
                    @Override
                    public void onclick(View view, int position, boolean islongclick) {

                        //Toast.makeText(Food_list.this, ""+local.getName(), Toast.LENGTH_SHORT).show();
                        Intent fooddetail= new Intent(Food_list.this,Food_detail.class);
                        fooddetail.putExtra("foodId",adapter.getRef(position).getKey());
                        startActivity(fooddetail);
                    }
                });

            }
        };
        recyclerfood.setAdapter(adapter);
    }




    private void loadSuggest() {
        foodlist.orderByChild("MenuId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                            Food item=postSnapshot.getValue(Food.class);
                            suggesteditemlist.add(item.getName());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        }


    private void startsearch(CharSequence text) {
        searchadapter= new FirebaseRecyclerAdapter<Food, Foodview_holder>(
                Food.class,
                R.layout.food_item,
                Foodview_holder.class,
                foodlist.orderByChild("Name").equalTo(text.toString())  //defaut parameter & name sangw compare gareko
                ) {
            @Override
            protected void populateViewHolder(Foodview_holder viewHolder, Food model, int position) {

                viewHolder.txtfoodname.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.food_image);
                final Food local=model;
                viewHolder.setItem_clicker(new Item_clicker() {
                    @Override
                    public void onclick(View view, int position, boolean islongclick) {

                        //Toast.makeText(Food_list.this, ""+local.getName(), Toast.LENGTH_SHORT).show();
                        Intent fooddetail= new Intent(Food_list.this,Food_detail.class);
                        fooddetail.putExtra("foodId",searchadapter.getRef(position).getKey());
                        startActivity(fooddetail);
            }
        });

    }

             };
        recyclerfood.setAdapter(searchadapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.refresh)
            loadlistfood(categoryId);


        return super.onOptionsItemSelected(item);
    }
}
