package com.eat_it.root.lets_eat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.eat_it.root.lets_eat.common.common;
import com.eat_it.root.lets_eat.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Home extends AppCompatActivity {
    Button btnsignup,btnsignin;
    TextView txtslogan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnsignin=findViewById(R.id.btnsignIn);
        btnsignup=findViewById(R.id.btnsignUp);
        txtslogan=findViewById(R.id.txtslogan);
        Typeface typeface= Typeface.createFromAsset(getAssets(),"font/Nabila.ttf");
        txtslogan.setTypeface(typeface);

        //check remember me
        Paper.init(this);




        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Home.this,Sign_in.class);
                startActivity(intent);
            }
        });


        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this,Sign_up.class));

            }
        });

        String user=Paper.book().read(common.USER_KEY);
        String pass=Paper.book().read(common.PASS_KEY);
        String phon=Paper.book().read(common.Phone_key);

        if(user!=null && pass!=null &phon!=null){
            if(!user.isEmpty()&&!pass.isEmpty()){
                login(phon,user,pass);
            }
        }
    }

    private void login(final String phon, final String User, final String pass) {

        final FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        final DatabaseReference user_table=firebaseDatabase.getReference("User");


        if (common.Isconnectedtointernet(getBaseContext())) {

            final ProgressDialog dialog = new ProgressDialog(Home.this);
            dialog.setMessage("please wait.....");
            dialog.show();


            user_table.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    // to check
                    if (dataSnapshot.child(phon).exists()) {


                        dialog.dismiss();
                        User user = dataSnapshot.child(phon).getValue(User.class);
                        user.setPhone(phon);

                        if (user.getPassword().equals(pass) && user.getName().equals(User)) {

                            Intent intent = new Intent(Home.this, Nav_home.class);
                            common.currentuser = user;
                            startActivity(intent);
                            finish();


                        } else {
                            Toast.makeText(Home.this, "Sign in fail !!", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        dialog.dismiss();
                        Toast.makeText(Home.this, "User not exist", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });

}
    }
}
