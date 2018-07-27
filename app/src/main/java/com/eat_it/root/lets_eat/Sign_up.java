package com.eat_it.root.lets_eat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eat_it.root.lets_eat.common.common;
import com.eat_it.root.lets_eat.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Sign_up extends AppCompatActivity {
MaterialEditText Phone,Name,Password;
Button edtsignup,cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Phone=(MaterialEditText)findViewById(R.id.edtphone);
        Name=(MaterialEditText)findViewById(R.id.edtusername);
        Password=(MaterialEditText)findViewById(R.id.edtpassword);

        edtsignup=findViewById(R.id.signup);
        cancel=findViewById(R.id.cancel);



        final FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        final DatabaseReference user_table=firebaseDatabase.getReference("User");


        edtsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(common.Isconnectedtointernet(getBaseContext())) {

                    final ProgressDialog dialog = new ProgressDialog(Sign_up.this);
                    dialog.setMessage("please wait.....");
                    dialog.show();

                    user_table.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.child(Phone.getText().toString()).exists()) {
                                dialog.dismiss();
                                Toast.makeText(Sign_up.this, "Phone number already exist", Toast.LENGTH_SHORT).show();
                            } else {
                                dialog.dismiss();
                                User user = new User();
                                user.setName(Name.getText().toString());
                                user.setPassword(Password.getText().toString());

                                user_table.child(Phone.getText().toString()).setValue(user);

                                Toast.makeText(Sign_up.this, "Sign Up sucessfully !!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }else {

                    Toast.makeText(Sign_up.this, "Check your Internet connection !!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              finish();
                startActivity(new Intent(Sign_up.this,Home.class));
            }
        });


    }
}
