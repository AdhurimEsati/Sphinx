package com.example.noli.sphinx;

import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LeaderBoard extends AppCompatActivity {
    private Map<String, Integer> players;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private ArrayList<Map<String, String>> list;
    private ListView leaderboard;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        players = new TreeMap();
        //players = new HashMap<>();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();
        list = new ArrayList<>();

        leaderboard = (ListView)  findViewById(R.id.scoreB);
        adapter = new SimpleAdapter(this, list, R.layout.score, new String[]{"player" , "score"}, new int[]{R.id.playerName, R.id.scoreWins});
        score();

        leaderboard.setAdapter(adapter);

    }

    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue( Map<K, V> map )
    {
        List<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
        {
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }

    private void score(){
        myRef.child("players").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    players.put( d.getKey().toString(), Integer.parseInt(d.child("wins").getValue().toString()));
                    System.out.println(d.child("wins").getValue().toString() + "  ///***//// + " + d.getKey().toString());
                }

                System.out.println(sortByValue(players));
                int count = 1;
                int tKey = 0;
                String pos = "st";
                for(int i = 0; i < players.size(); i++){
                    HashMap<String, String> temp = new HashMap<>();
                    String key = (String) sortByValue(players).keySet().toArray()[i];
                    if(i!= 0 && tKey > players.get(key)){
                        count++;
                        if(count == 2){
                            pos = "nd";
                        }else if(count == 3){
                            pos = "rd";
                        }else if(count > 3){
                            pos = "th";
                        }
                    }
                    System.out.println(key);
                    System.out.println(sortByValue(players).get(key));
                    temp.put("player", count + pos + "  " +key);
                    temp.put("score", sortByValue(players).get(key) + "W - " + dataSnapshot.child(key).child("draws").getValue() + "D - " + dataSnapshot.child(key).child("losses").getValue() + "L");
                    list.add(temp);
                    adapter.notifyDataSetChanged();
                    tKey = players.get(key);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed(){
        finish();
        startActivity(new Intent(this, DashBoardActivity.class));
    }
}
