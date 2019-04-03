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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.ArrayList;

public class attendingEvent extends AppCompatActivity {


    FirebaseAuth mAuth;
    String attend;
    String modelid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attending_event);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FirebaseApp.initializeApp(this);



        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        if (firebaseDatabase==null){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        mAuth=FirebaseAuth.getInstance();






       DatabaseReference attref=firebaseDatabase.getReference().child("Attending").child(mAuth.getUid());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.reportholderr);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        attref.keepSynced(true);

        super.onStart();
        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(attref,Model.class)
                        .build();

        FirebaseRecyclerAdapter<Model,MyAdapter>firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Model, MyAdapter>(
                options
        ) {
            @Override
            protected void onBindViewHolder(@NonNull final MyAdapter holder, final int position, @NonNull final Model model) {


                    holder.setComplaintLocation(model.getComplaintLocation());
                    holder.setDesc(model.getDesc());
                    holder.Directions.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + model.getComplaintLocation());
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");

                            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                startActivity(mapIntent);
                            } else {
                                Toast.makeText(attendingEvent.this, "There is no app available for this feature", Toast.LENGTH_LONG).show();
                            }

                        }
                    });


                    holder.setCompleted(model.getCompleted());
                    holder.setImage(model.getImage(),attendingEvent.this);
                    holder.setStatus(model.getStatus());
                    holder.setAttending(model.getAttending());


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

    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
