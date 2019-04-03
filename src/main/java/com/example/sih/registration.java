package com.example.sih;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class registration extends AppCompatActivity {
    private EditText name;
    private EditText login;
    private EditText password;
    private EditText dob;
    private EditText number;
    private String Name;
    private String email;
    private String pass;
    private String Dob;
    private String phone;
    private FirebaseAuth mAuth;
    private Button reg;
    private ProgressDialog progressDialog;
    private FirebaseDatabase firebaseDatabase;
    private    DatabaseReference myref;
    HashMap<String,String>hashMap=new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();
        name=(EditText)findViewById(R.id.name);
        login=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        dob=(EditText)findViewById(R.id.dob);
        number=(EditText)findViewById(R.id.phone);
        progressDialog=new ProgressDialog(registration.this);
        assert  getSupportActionBar()!=null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reg=(Button)findViewById(R.id.reg);
        myref=firebaseDatabase.getReference().child("RegisteredUsers");



                hashMap.put("Name",Name);
                hashMap.put("Email",email);
                hashMap.put("Password",pass);
                hashMap.put("Dob",Dob);
                hashMap.put("Phone",phone);


reg.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Name=name.getText().toString();
        email=login.getText().toString();
        pass=password.getText().toString();
        Dob=dob.getText().toString();
        phone=number.getText().toString();

        if (!TextUtils.isEmpty(Name)&&!TextUtils.isEmpty(email)
                &&!TextUtils.isEmpty(pass)&&!TextUtils.isEmpty(Dob)&&!TextUtils.isEmpty(phone))
        {
            progressDialog.setTitle("Authenticating");
            progressDialog.setMessage("Please wait while we creating your new account");
            progressDialog.show();
            Registration(email,pass);

        }
        else
        {
            Toast.makeText(registration.this,"One of the fields are empty",Toast.LENGTH_LONG).show();
        }


    }
});


    }
    private void Registration(String email,String pass){
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    myref.child(mAuth.getUid()).push().setValue(hashMap);
                    progressDialog.dismiss();
                    Intent intent=new Intent(registration.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(registration.this, "Registration failed " +task.getException(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent=new Intent(registration.this,login.class);
        startActivity(intent);
        finish();
        return true;
    }
}


