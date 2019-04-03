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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
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

public class deleteEvent extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private PopupMenu popupMenu;
    private String id;
    private View deview;
    DatabaseReference myref;
    DatabaseReference userref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_event);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FirebaseApp.initializeApp(this);



        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        if (firebaseDatabase==null){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        mAuth=FirebaseAuth.getInstance();
        userref=firebaseDatabase.getReference().child("Users");
         myref = firebaseDatabase.getReference().child("Events").child(mAuth.getUid());
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.reportholderr);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        userref.keepSynced(true);
        myref.keepSynced(true);



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
                holder.setComplaintLocation(model.getComplaintLocation());
                holder.setDesc(model.getDesc());
                holder.setImage(model.getImage(),deleteEvent.this);
                holder.option.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            id=model.getEventID();
                         popupMenu=new PopupMenu(deleteEvent.this,v);
                        MenuInflater inflater = popupMenu.getMenuInflater();
                        inflater.inflate(R.menu.delmenu, popupMenu.getMenu());
                        popupMenu.show();

popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.del:
                myref.child(id).removeValue();
                userref.child(id).removeValue();
                break;
            case R.id.ccom:
             holder.mark.setVisibility(View.VISIBLE);
                myref.child(id).removeValue();
                userref.child(id).child("Status").setValue(true);


        }
        return true;
    }
});


                    }


                });







            }


            @NonNull
            @Override
            public MyAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_del,viewGroup,false);
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
