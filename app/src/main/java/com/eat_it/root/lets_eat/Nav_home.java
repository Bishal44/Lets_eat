package com.eat_it.root.lets_eat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eat_it.root.lets_eat.Interface.Item_clicker;
import com.eat_it.root.lets_eat.VIewHolder.main_VIewholder;
import com.eat_it.root.lets_eat.common.common;
import com.eat_it.root.lets_eat.model.Category;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

import static com.eat_it.root.lets_eat.R.layout.menu_item;

public class Nav_home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    FirebaseDatabase firebaseDatabase;
    DatabaseReference category;
    TextView fullname;
    RecyclerView recyclermenu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category,main_VIewholder> adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);


        firebaseDatabase = FirebaseDatabase.getInstance();
        category = firebaseDatabase.getReference("Category");
        Paper.init(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Nav_home.this, Cart.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerview = navigationView.getHeaderView(0);
        fullname = (TextView) headerview.findViewById(R.id.fullname);
        fullname.setText(common.currentuser.getName());

        //for load the view
        recyclermenu = (RecyclerView) findViewById(R.id.recyclercard);

        recyclermenu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclermenu.setLayoutManager(layoutManager);

        if (common.Isconnectedtointernet(this)) {
            Loadmenu();
        }else {

            Toast.makeText(Nav_home.this, "Check your Internet connection !!", Toast.LENGTH_LONG).show();
            return;
        }
    }


    private void Loadmenu() {

        adapter= new FirebaseRecyclerAdapter<Category, main_VIewholder>(Category.class,R.layout.menu_item,main_VIewholder.class,category) {
            @Override
            protected void populateViewHolder(main_VIewholder viewHolder, Category model, int position) {

                viewHolder.txtMenuname.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.image);
                final Category clickitem=model;


                viewHolder.setItem_clicker(new Item_clicker() {
                    @Override
                    public void onclick(View view, int position, boolean islongclick) {
                        //Toast.makeText(Nav_home.this, ""+clickitem.getName(), Toast.LENGTH_SHORT).show();

                       //for category id
                        Intent intentfood=new Intent(Nav_home.this,Food_list.class);
                        intentfood.putExtra("categoryid",adapter.getRef(position).getKey());
                        startActivity(intentfood);
                    }
                });
            }
        };
        recyclermenu.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.refresh)
            Loadmenu();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            startActivity(new Intent(Nav_home.this,Nav_home.class));

        } else if (id == R.id.nav_cart) {

            startActivity(new Intent(Nav_home.this,Cart.class));
        } else if (id == R.id.nav_order) {
            startActivity(new Intent(Nav_home.this,Order_status.class));

        } else if (id == R.id.nav_signout) {

            //delete remember me
            Paper.book().destroy();


            //back press garer previous ma jane stop gareko
            Intent signin=new Intent(Nav_home.this,Sign_in.class);
            signin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signin);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
