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
    /*
    The LikesActivity houses the usernames of the users who liked a particular post.

    It utilizes a ListView container to display the likes.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);

        // Get the Like objects from the FeedActivity
        ArrayList<Like> likes = (ArrayList<Like>)this.getIntent().getSerializableExtra("likes");

        // Convert the list of Like objects into a list of Strings containing the usernames
        ArrayList<String> items = new ArrayList<String>();
        for (int i = 0; i < likes.size(); ++i) {
            Like like = likes.get(i);

            items.add(like.getUsername());
        }

        // Create the array adapter to be used to project the likes onto the list view
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
        // Navigates back to the FeedActivity

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