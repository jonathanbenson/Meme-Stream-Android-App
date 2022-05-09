package com.example.memestream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Vector;

public class LikesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);

        ArrayList<Like> likes = (ArrayList<Like>)this.getIntent().getSerializableExtra("likes");

        ArrayList<String> items = new ArrayList<String>();

        for (int i = 0; i < likes.size(); ++i) {
            Like like = likes.get(i);

            items.add(like.getUsername());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview, items);
        ListView listView = (ListView)this.findViewById(R.id.likesListView);
        listView.setAdapter(adapter);

        LikesActivity likesActivity = this;

        // Set the go back button onclick listener
        Button likesGoBackButton = this.findViewById(R.id.likesGoBackButton);

        likesGoBackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                likesActivity.goBack();
            }

        });
    }

    public void goBack() {

        Bundle extras = this.getIntent().getExtras();
        String username = extras.getString("username");
        String sessionKey = extras.getString("sessionKey");
        String post = extras.getString("post");

        // Navigate back to the FeedActivity
        Intent intent = new Intent(LikesActivity.this, FeedActivity.class);

        intent.putExtra("username", username);
        intent.putExtra("sessionKey", sessionKey);
        intent.putExtra("post", post);

        LikesActivity.this.startActivity(intent);

    }
}