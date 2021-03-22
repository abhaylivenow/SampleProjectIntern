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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {

    private Button btnLogIn,btnSignIn;
    private TextView txtForgetPass;
    private EditText edtEmail, edtPassword;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize views
        btnSignIn = findViewById(R.id.btn_signUp);
        btnLogIn = findViewById(R.id.btn_login);
        edtEmail = findViewById(R.id.login_email_edt);
        edtPassword = findViewById(R.id.login_password_edt);
        txtForgetPass = findViewById(R.id.txt_forget_password);
        loadingBar = new ProgressDialog(loginActivity.this);

        // handle onclick on buttons
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity.this,signUp.class);
                startActivity(intent);
            }
        });

        txtForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

    }
    // login user method
    private void loginUser(){
        // get data coming from text fields
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        // check for general errors
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
        }else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            // get instance of firebase Auth
            // sign in using FirebaseAuth
            mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(loginActivity.this,homeActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(loginActivity.this, "error occurred", Toast.LENGTH_SHORT).show();
                    }
                    loadingBar.dismiss();
                }
            });
        }
    }
    // method for resetting password
    // an email will be sent for resetting password
    private void resetPassword(){
        mAuth = FirebaseAuth.getInstance();
        String email = edtEmail.getText().toString();
        loadingBar.setTitle("Please wait");
        loadingBar.show();

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    // handle if the task is successful
                    Toast.makeText(loginActivity.this, "Check your email", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(loginActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                }
                loadingBar.dismiss();
            }
        });
    }
}