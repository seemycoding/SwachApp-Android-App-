package com.example.sih;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class login extends AppCompatActivity {
private EditText login;
private EditText password;
private Button log;
private Button Reg;
private String email;
private String pass;
private FirebaseAuth mAuth;
private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        log=(Button)findViewById(R.id.login);
        Reg=(Button)findViewById(R.id.button2);
        mAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(login.this);
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser!=null){
            Intent intent=new Intent(login.this,Main2Activity.class);
            startActivity(intent);
            finish();
        }
        else
        {

            Toast.makeText(login.this,"Please Login First",Toast.LENGTH_LONG).show();
        }



        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login.this,registration.class);
                startActivity(intent);
                finish();

            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=login.getText().toString();
                pass=password.getText().toString();
            if (!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pass))
            {
                progressDialog.setTitle("Authenticating");
                progressDialog.setMessage("Please wait ! Logging In");
                progressDialog.show();
                userlogin(email,pass);
            }
            else {
                Toast.makeText(login.this,"One of the tect is empty",Toast.LENGTH_LONG).show();
            }

            }
        });

    }

    private void userlogin(String email,String pass){

        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Intent intent2=new Intent(login.this,MainActivity.class);
                    startActivity(intent2);
                    finish();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(login.this, "Authentication failed " +task.getException(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
