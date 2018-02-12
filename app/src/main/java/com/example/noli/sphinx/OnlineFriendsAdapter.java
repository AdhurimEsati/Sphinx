package com.example.noli.sphinx;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Adhu on 5/16/2017.
 */

class OnlineFriendsAdapter extends ArrayAdapter<String> {

    private String username;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    OnlineFriendsAdapter(Context context, ArrayList<String> friends, String username) {
        super(context, R.layout.list_friends_notti, friends);
        this.username = username;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View costumView = inflater.inflate(R.layout.list_friends_notti, parent, false);

        TextView textView = (TextView) costumView.findViewById(R.id.textView6);
        textView.setText(getItem(position));
        final ImageView img = (ImageView) costumView.findViewById(R.id.imageView12);

        myRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){

                    if(d.getKey().trim().equals(mAuth.getCurrentUser().getUid().trim())){
                        User user = d.getValue(User.class);
                        username = user.getUsername().trim();
                    }
                }

                myRef.child("msg_noti").child(username).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator it = dataSnapshot.getChildren().iterator();
                        String user = "";
                        while(it.hasNext()) {
                            DataSnapshot dataSnap = (DataSnapshot) it.next();

                            if(dataSnap.getKey().equals(getItem(position))) {
                                if(dataSnap.getValue().toString().equals("1")) {
                                    System.out.println("SUCCESS 69");
                                    img.setImageResource(R.drawable.imageview12);
                                }
                                else {
                                    img.setImageResource(0);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return costumView;
    }
}
