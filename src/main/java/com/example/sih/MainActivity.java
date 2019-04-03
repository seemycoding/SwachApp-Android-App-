package com.example.sih;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    String completed;
    String fake;
    String eid;
    String support_count;
    String EventId;
    String eventAtt;
    FirebaseAuth mAuth;
    Model amodel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FirebaseApp.initializeApp(this);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        if (firebaseDatabase==null){ 
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        final DatabaseReference myref = firebaseDatabase.getReference().child("Users");
        final DatabaseReference attref = firebaseDatabase.getReference().child("Attending");
        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.Add);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.reportholderr);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        mAuth=FirebaseAuth.getInstance();
        myref.keepSynced(true);
        attref.keepSynced(true);




        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Report this event as completed or fake " +
                "Note!! if the fake report exceeded 10 the event will get auto canceled and remove from the server");
                builder.setTitle("Report this event");
        builder.setPositiveButton("COMPLETED", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int report_complete=Integer.parseInt(completed);
                if (report_complete >= 0){
                    report_complete=report_complete+1;

                }
                String com_report=String.valueOf(report_complete);
                myref.child(eid).child("Completed").setValue(com_report);




            }
        });

        builder2.setPositiveButton(" ATTEND AND SUPPORT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int count=Integer.parseInt(support_count);
                if (count>=0){
                    count=count+1;

                }
                int count2=Integer.parseInt(eventAtt);
                if (count2>=0){
                    count2=count2+1;
                    if (count2==11){
                        if (mAuth.getUid().equals(amodel.getId())) {
                            Intent email = new Intent(Intent.ACTION_SEND);
                            email.putExtra(Intent.EXTRA_EMAIL, new String[]{"komalk413@gmail.com"});
                            email.putExtra(Intent.EXTRA_SUBJECT, amodel.getIssue());
                            email.putExtra(Intent.EXTRA_TEXT, amodel.getDesc());

                            //need this to prompts email client only
                            email.setType("message/rfc822");

                            startActivity(Intent.createChooser(email, "Choose an Email client :"));
                        }
                    }

                }


                String count_support=String.valueOf(count);

                myref.child(EventId).child("Support").setValue(count_support);
                String count_Attsupport=String.valueOf(count2);
                myref.child(EventId).child("Attending").setValue(count_Attsupport);
                HashMap<String,String>atthashmap=new HashMap<>();
                atthashmap.put("Name",amodel.getName() );
                atthashmap.put("Phone", amodel.getPhone());
                atthashmap.put("ComplaintLocation", amodel.getComplaintLocation());
                atthashmap.put("Issue", amodel.getIssue());
                atthashmap.put("Desc", amodel.getDesc());
                atthashmap.put("Status", "False");
                atthashmap.put("Support", "0");
                atthashmap.put("Report","0");
                atthashmap.put("EventID",amodel.getEventID());
                atthashmap.put("Completed","0");
                atthashmap.put("Attending","0");
                atthashmap.put("Image",amodel.getImage());
                attref.child(mAuth.getUid()).child(EventId).setValue(atthashmap);


            }
        });
        builder2.setNegativeButton("SUPPORT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int count=Integer.parseInt(support_count);
                if (count>=0){
                    count=count+1;

                }

                String count_support=String.valueOf(count);
                myref.child(EventId).child("Support").setValue(count_support);
            }
        });
        builder.setNegativeButton("FAKE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int fake_report=Integer.parseInt(fake);
                if (fake_report >=0 ){
                    fake_report=fake_report+1;
                    if (fake_report>10){
                        myref.child(eid).removeValue();
                    }else {
                        String report_fake=String.valueOf(fake_report);
                        myref.child(eid).child("Report").setValue(report_fake);

                    }
                }



            }
        });

        final AlertDialog dialog = builder.create();
        //recyclerView.setHasFixedSize(true);

        //final RecyclerView.Adapter adapter;
        //list = new ArrayList<Model>();
       // adapter = new MyAdapter(MainActivity.this, list);
       // recyclerView.setAdapter(adapter);

        super.onStart();
        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(myref,Model.class)
                        .build();

        FirebaseRecyclerAdapter<Model,MyAdapter>firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Model, MyAdapter>(
                options
        ) {
            @Override
            protected void onBindViewHolder(@NonNull final MyAdapter holder, final int position, @NonNull final Model model) {
               amodel=model;
                holder.setComplaintLocation(model.getComplaintLocation());
                holder.setDesc(model.getDesc());
                holder.Directions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+model.getComplaintLocation());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");

                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(mapIntent);
                        }
                        else{
                            Toast.makeText(MainActivity.this,"There is no app available for this feature",Toast.LENGTH_LONG).show();
                        }

                    }
                });
                holder.Support.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder2.setMessage(
                               "You can Either Support or Attend as well as support this event");
                        builder2.setTitle("Contribute !!");
                        builder2.show();

                        System.out.println(model.getEventID());
                        support_count=model.getSupport();
                        EventId=model.getEventID();
                        eventAtt=model.getAttending();


                    }
                });
                holder.setSupport(model.getSupport());
                holder.report.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            completed=model.getCompleted();
                            fake=model.getReport();
                            eid=model.getEventID();
                        dialog.show();

                    }
                });
                holder.setCompleted(model.getCompleted());
                holder.setReport(model.getReport());
               holder.setStatus(model.getStatus());
               holder.setAttending(model.getAttending());
               holder.setIsuue(model.getIssue());


                holder.setImage(model.getImage(),MainActivity.this);



            }

            @NonNull
            @Override
            public MyAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rtext,viewGroup,false);
                return new MyAdapter(view);
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, addreport.class);
                startActivity(intent);
            }
        });


    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
