package com.example.sampleproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class signUp extends AppCompatActivity {

    private EditText edtEmail,edtPassword,edtConfirmPassword
            , edtUsername, edtCity, edtAge;
    private Button btnSignUp;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // initialize all the views
        edtEmail = findViewById(R.id.signIn_email);
        edtUsername = findViewById(R.id.signIn_Username);
        edtPassword = findViewById(R.id.signIn_password_edt);
        edtConfirmPassword = findViewById(R.id.signUp_confirm_password);
        edtCity = findViewById(R.id.signUp_city);
        edtAge = findViewById(R.id.signUp_age);
        btnSignUp = findViewById(R.id.btn_signIn);
        loadingBar = new ProgressDialog(signUp.this);

        // handle onclick on signUp button
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    // this method uses FirebaseAuth inbuilt method to create user
    private void createAccount() {
        // get all the data coming from text fields
        final String email = edtEmail.getText().toString();
        final String username = edtUsername.getText().toString();
        final String password = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();
        final String city = edtCity.getText().toString();
        final String age = edtAge.getText().toString();

        // check for general errors
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please enter password again", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(city)) {
            Toast.makeText(this, "Please enter city", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(age)) {
            Toast.makeText(this, "Please enter age", Toast.LENGTH_SHORT).show();
        }
        if (confirmPassword.equals(password)) {

            // show loading bar
            loadingBar.setTitle("Please wait");
            loadingBar.setMessage("Please wait while we check credentials");
            loadingBar.show();

            // get the instance of FirebaseAuth
            mAuth = FirebaseAuth.getInstance();

            // use inbuilt method of FirebaseAuth to create user
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(signUp.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // handle if the task is successful
                        Intent intent = new Intent(signUp.this, loginActivity.class);
                        startActivity(intent);

                        loadingBar.dismiss();
                        Toast.makeText(signUp.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        loadingBar.dismiss();
                        Toast.makeText(signUp.this, "error occurred", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // get the reference of firebase database
            // these below codes saves user data into firebase database
            final DatabaseReference mRef;
            mRef = FirebaseDatabase.getInstance().getReference();

            // add the data to the database at the same time user is signed in
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.child("User").child(mAuth.getUid()).exists()){
                        HashMap<String, Object> userDataMap = new HashMap<>();
                        userDataMap.put("Username", username);
                        userDataMap.put("email", email);
                        userDataMap.put("password", password);
                        userDataMap.put("city", city);
                        userDataMap.put("age", age);

                        mRef.child("User").child(mAuth.getUid()).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    loadingBar.dismiss();
                                    Toast.makeText(signUp.this, "added to database", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}