package com.eat_it.root.lets_eat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.eat_it.root.lets_eat.common.common;
import com.eat_it.root.lets_eat.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.paperdb.Paper;

public class Sign_in extends AppCompatActivity {
Button signin,back;
EditText ephone,epassword,ename;
com.rey.material.widget.CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        signin=findViewById(R.id.signin);
        back=findViewById(R.id.back);
        epassword=(MaterialEditText)findViewById(R.id.password);
        ephone=(MaterialEditText)findViewById(R.id.phone);
        ename=(MaterialEditText)findViewById(R.id.username);
        checkBox= findViewById(R.id.rememberme);


        final FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        final DatabaseReference user_table=firebaseDatabase.getReference("User");


        //paper
        Paper.init(this);

        signin.setOnClickListener(new View.OnClickListener() {
         @Override
        public void onClick(View view) {

             if (common.Isconnectedtointernet(getBaseContext())) {

                 //for remember me
                 if(checkBox.isChecked()){
                     Paper.book().write(common.USER_KEY,ename.getText().toString());
                     Paper.book().write(common.PASS_KEY,epassword.getText().toString());
                     Paper.book().write(common.Phone_key,ephone.getText().toString());
                 }

                 final ProgressDialog dialog = new ProgressDialog(Sign_in.this);
                 dialog.setMessage("please wait.....");
                 dialog.show();


                 user_table.addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                         // to check
                         if (dataSnapshot.child(ephone.getText().toString()).exists()) {


                             dialog.dismiss();
                             User user = dataSnapshot.child(ephone.getText().toString()).getValue(User.class);
                             user.setPhone(ephone.getText().toString());

                             if (user.getPassword().equals(epassword.getText().toString()) && user.getName().equals(ename.getText().toString())) {

                                 Intent intent = new Intent(Sign_in.this, Nav_home.class);
                                 common.currentuser = user;
                                 startActivity(intent);
                                 finish();


                             } else {
                                 Toast.makeText(Sign_in.this, "Sign in fail !!", Toast.LENGTH_SHORT).show();
                             }

                         } else {
                             dialog.dismiss();
                             Toast.makeText(Sign_in.this, "User not exist", Toast.LENGTH_SHORT).show();
                         }

                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError databaseError) {

                     }
                 });
             }else {

                 Toast.makeText(Sign_in.this, "Check your Internet connection !!", Toast.LENGTH_LONG).show();
                 return;
             }
         }
});


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(Sign_in.this,Home.class));
            }
        });


    }

}
