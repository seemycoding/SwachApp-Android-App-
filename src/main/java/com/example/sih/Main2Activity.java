package com.example.sih;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main2Activity extends AppCompatActivity {
    private Button organizingEvent;
    private Button liveComplaint;
    private Button resolveComplaint;
    private Button EtoiletCenter;
    private Button wasteManagement;
    private FirebaseAuth mAuth;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            ActivityCompat.requestPermissions(this,

                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,

                            Manifest.permission.ACCESS_COARSE_LOCATION,

                            Manifest.permission.BLUETOOTH,

                            Manifest.permission.BLUETOOTH_ADMIN}, MY_PERMISSIONS_REQUEST_LOCATION);

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        mAuth=FirebaseAuth.getInstance();

        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser!=null){


            Toast.makeText(Main2Activity.this,"Welcome" +
                    "",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(Main2Activity.this,"Please Login First",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(Main2Activity.this,login.class);
            startActivity(intent);
            finish();
        }
        organizingEvent=(Button)findViewById(R.id.oe);
        liveComplaint=(Button)findViewById(R.id.lc);
        //resolveComplaint=(Button)findViewById(R.id.rc);
        EtoiletCenter=(Button)findViewById(R.id.tc);
        wasteManagement=(Button)findViewById(R.id.wm);

        organizingEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main2Activity.this,MainActivity.class);
                startActivity(intent);


            }
        });

        liveComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(Main2Activity.this,addreport.class);
                startActivity(intent1);

            }
        });



        EtoiletCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=toilets");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
                else{
                    Toast.makeText(Main2Activity.this,"There is no app available for this feature",Toast.LENGTH_LONG).show();
                }




            }
        });

        wasteManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=E-waste");
                Intent mapIntent1 = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent1.setPackage("com.google.android.apps.maps");

                if (mapIntent1.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent1);
                }
                else{
                    Toast.makeText(Main2Activity.this,"There is no app available for this feature",Toast.LENGTH_LONG).show();
                }

            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.mlog:
                mAuth.signOut();
                if(mAuth.getCurrentUser()==null){
                    Intent intent=new Intent(Main2Activity.this,login.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.del:
                Intent delintent=new Intent(Main2Activity.this,deleteEvent.class);
                startActivity(delintent);
                break;
            case R.id.matt:
                Intent attintent=new Intent(Main2Activity.this,attendingEvent.class);
                startActivity(attintent);
                break;




                

        }
        return super.onOptionsItemSelected(item);
    }
}
