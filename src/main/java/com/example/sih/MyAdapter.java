package com.example.sih;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

class MyAdapter extends RecyclerView.ViewHolder {
        View mview;
        TextView place;
        TextView description;
        Button Support;
        Button Directions;
        TextView count;
        Button sbutton;
        Button report;
        TextView reporttext;
        TextView completed;
        ImageButton option;
        Button mark;
        Button verified;
        TextView Att;
        TextView isreport;
        ImageButton img;
    ImageView imageView;

    public MyAdapter(@NonNull View itemView) {
        super(itemView);
        mview=itemView;
            place=(TextView)mview.findViewById(R.id.place);
            description=(TextView)mview.findViewById(R.id.detail);
            Support=(Button)mview.findViewById(R.id.supp);
            Directions=(Button)mview.findViewById(R.id.direct);
            count=(TextView)mview.findViewById(R.id.stext);
            sbutton=(Button)mview.findViewById(R.id.ver);
            report=(Button)mview.findViewById(R.id.rep);
            mark=(Button)mview.findViewById(R.id.mark);
            verified=(Button)mview.findViewById(R.id.cver);
            reporttext=(TextView)mview.findViewById(R.id.rtext);
            completed=(TextView)mview.findViewById(R.id.comp);
            option=(ImageButton)mview.findViewById(R.id.option);
            Att=(TextView)mview.findViewById(R.id.attend);
            isreport=(TextView)mview.findViewById(R.id.issreport);
            img=(ImageButton)mview.findViewById(R.id.imageButton);
        imageView=(ImageView)mview.findViewById(R.id.evimg);
    }

    public String setId(String uid){

        return  uid;
    }


    public String setImage(String image,Context ctx){
        System.out.println(image);

        Picasso.with(ctx).load(image).placeholder(R.drawable.ic_launcher_foreground).
       into(imageView);

        return image;
    }

    public  String setIsuue(String issue){
        isreport.setText(issue);

        return issue;

    }


            public String setAttending(String Attend){
                    if (Att.getText().equals("Keep contributing towards a green and clean city")){

                    }else {
                        Att.setText(Attend + " Users are attending this event");
                    }

                return Attend;
            }
           public String setComplaintLocation(String location){
                place.setText(location);
                return location;

           }

           public String setDesc(String Desc){
                description.setText(Desc);
                return Desc;

           }

           public String setSupport(String support){
                count.setText(support);
                int i=Integer.parseInt(support);
                if (i>10){
                    sbutton.setVisibility(View.VISIBLE);
                }
            return support;
           }

           public String setReport(String report)
           {
               reporttext.setText(report);
                return report;
           }

           public String setCompleted(String complete){
        completed.setText(complete+" Users reported this event as completed");
        return complete;

           }

           public String setStatus(String status){
                    if (status.equals("true")){
                        verified.setVisibility(View.VISIBLE);
                        completed.setText("Completed --This event is successful in making city clean and green");
                        Att.setText("Keep contributing towards a green and clean city");
                        Support.setEnabled(false);
                        report.setEnabled(false);
                    }

                return status;
           }



}