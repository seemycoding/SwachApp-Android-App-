package com.example.sih;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Debug;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class addreport extends AppCompatActivity {
    String UserName;
    String UserPhone;
    String CompaintLocation;
    String ReportableIssues;
    String Description;
    Button getlocation;
    String message;
   String id;
    Uri url;
    private ProgressDialog progressDialog;
    location my = new location(addreport.this);
    private ImageButton image;
    private static final int GALLERY_PICK = 1;
    private StorageReference mImageStorage;
    StorageReference imgref;
     DatabaseReference myref;
    Uri resultUri=null;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addreport);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        if (firebaseDatabase==null){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

         myref = firebaseDatabase.getReference().child("Users");
        final DatabaseReference eventref=firebaseDatabase.getReference().child("Events");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        mImageStorage= FirebaseStorage.getInstance().getReference();
     final Spinner spinnerissues=(Spinner) findViewById(R.id.issues);
        final EditText name = (EditText) findViewById(R.id.editname);
        final EditText phone = (EditText) findViewById(R.id.editphone);
        final EditText location = (EditText) findViewById(R.id.editlocation);
        AlertDialog.Builder builder = new AlertDialog.Builder(addreport.this);

        myref.keepSynced(true);
        eventref.keepSynced(true);


// 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Location Cordinates are not avialable at this moment try again or write yourself")
                .setTitle("Location Not Found");
        builder.setPositiveButton("Try agian", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    message = my.showlocation();
                } catch (IOException e) {

                    e.printStackTrace();
                }
                if (message.equals("")) {

                } else {
                    location.setText(message);
                }
            }
        });
        final AlertDialog dialog = builder.create();
        getlocation = (Button) findViewById(R.id.lo);
        //final EditText issuereport=(EditText)findViewById(R.id.editreport);
        image = (ImageButton) findViewById(R.id.imageButton);
        final EditText desc = (EditText) findViewById(R.id.editdesc);
        Button save = (Button) findViewById(R.id.submit);
        mAuth = FirebaseAuth.getInstance();
        getlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    message = my.showlocation();
                } catch (IOException e) {

                    e.printStackTrace();
                }
                if (message.equals("")) {
                    dialog.show();
                } else {
                    location.setText(message);
                }


                //Toast.makeText(addreport.this,message,Toast.LENGTH_LONG).show();
            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserName = name.getText().toString();
                UserPhone = phone.getText().toString();
                CompaintLocation = location.getText().toString();
                ReportableIssues=spinnerissues.getSelectedItem().toString();
                Description = desc.getText().toString();
                if (!TextUtils.isEmpty(UserPhone) &&
                        !TextUtils.isEmpty(UserName) && !TextUtils.isEmpty(CompaintLocation) && !TextUtils.isEmpty(Description)) {
                    progressDialog.setTitle("Processing");
                    progressDialog.setMessage("Please wait while we creating your event");
                    progressDialog.show();
                } else {
                    Toast.makeText(addreport.this, "One of the fields is empty", Toast.LENGTH_LONG).show();
                }
                System.out.print(UserName);
                 id=UUID.randomUUID().toString();
                final HashMap<String, String> UserData = new HashMap<>();
                final HashMap<String, String> EventData = new HashMap<>();


                UserData.put("Name", UserName);
                UserData.put("Phone", UserPhone);
                UserData.put("ComplaintLocation", CompaintLocation);
                UserData.put("issue", ReportableIssues);
                UserData.put("Desc", Description);
                UserData.put("Status", "False");
                UserData.put("Support", "0");
                UserData.put("Report","0");
                UserData.put("EventID",id);
                UserData.put("Completed","0");
                UserData.put("Attending","0");
                UserData.put("Image","image");
                UserData.put("id",mAuth.getUid());


                myref.child(id).setValue(UserData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            eventref.child(mAuth.getUid()).child(id).setValue(UserData);

                            Intent gotointent = new Intent(addreport.this, MainActivity.class);
                            startActivity(gotointent);
                            finish();
                        }
                    }
                });

                myref.child(id).child("Image").setValue(url.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                                eventref.child(mAuth.getUid()).child(id).child("Image").setValue(url.toString());
                            progressDialog.dismiss();
                            Toast.makeText(addreport.this, "Profile pic uploaded successfully", Toast.LENGTH_SHORT).show();

                        }
                    }

                });

            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.planets_array, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerissues.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();
            System.out.println("working");

            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(500, 500)
                    .start(this);

            //Toast.makeText(SettingsActivity.this, imageUri, Toast.LENGTH_LONG).show();

        }
        else
        {
            System.out.println("working not");
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            System.out.println("working");

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                final Uri uri=data.getData();
                progressDialog = new ProgressDialog(addreport.this);
                progressDialog.setTitle("Uploading Event pic...");
                progressDialog.setMessage("Please wait while we upload and process the image.");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();


               resultUri = result.getUri();
               final String imgid=UUID.randomUUID().toString();
                final StorageReference filepath = mImageStorage.child("event_images").child(imgid+ ".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            progressDialog.setMessage("Uploading is in process just a moment");
                            imgref = mImageStorage.child("event_images").child(imgid + ".jpg");

                            imgref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess( final Uri uri) {
                                    Picasso.with(addreport.this).load(uri).placeholder(R.drawable.ic_launcher_foreground).into(image);
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful())
                                    {
                                        url=task.getResult();
                                        progressDialog.dismiss();




                                    }
                                }
                            });



                        }
                        else
                        if (!task.isSuccessful())
                        {
                            Toast.makeText(addreport.this, " Uploading Failed", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });



            }

        }


    }

}



