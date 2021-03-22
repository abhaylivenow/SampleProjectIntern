package com.example.sampleproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class homeActivity extends AppCompatActivity {

    private TextView txtUsername, txtEmail, txtCity, txtAge;
    private Button logout;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtUsername = findViewById(R.id.display_username);
        txtEmail = findViewById(R.id.display_email);
        txtCity = findViewById(R.id.display_city);
        txtAge = findViewById(R.id.display_age);
        logout = findViewById(R.id.btn_logout);

        mAuth = FirebaseAuth.getInstance();

        mRef = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getUid());
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.child("Username").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                String city = snapshot.child("city").getValue().toString();
                String age = snapshot.child("age").getValue().toString();

                txtUsername.setText(username);
                txtEmail.setText(email);
                txtCity.setText(city);
                txtAge.setText(age);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }
    private void logout(){
        Intent intent = new Intent(homeActivity.this,loginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
        Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show();
    }
}