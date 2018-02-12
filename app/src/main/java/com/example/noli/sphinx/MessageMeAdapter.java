package com.example.noli.sphinx;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Adhu on 5/12/2017.
 */

public class MessageMeAdapter extends ArrayAdapter<Message> {

    private String username;

    MessageMeAdapter(Context context, ArrayList<Message> msg, String username) {
        super(context, R.layout.message_myself, msg);
        this.username = username;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View costumView = inflater.inflate(R.layout.message_myself, parent, false);
        TextView textView = null;
        String msg = getItem(position).getMsg();
        String sender = getItem(position).getSender();
        System.out.println("Sender: " + sender + " --- Username: " + username);

        textView = (TextView) costumView.findViewById(R.id.msg);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)textView.getLayoutParams();

        //if me bubble right else bubble left
        if(sender.equals(username)) {
            System.out.println("HINA NE LOOP");
            textView.setBackgroundResource(R.drawable.bubble_right_green);
            layoutParams.gravity = Gravity.RIGHT;
        }
        else {
            textView.setBackgroundResource(R.drawable.bubble_left_gray);
            layoutParams.gravity = Gravity.LEFT;
        }
        textView.setText(msg);
        return costumView;
    }

}
