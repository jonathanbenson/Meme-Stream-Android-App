package com.example.memestream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Vector;

public class LikesActivity extends AppCompatActivity {

    private ArrayList<Like> likes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);

        this.likes = (ArrayList<Like>)this.getIntent().getSerializableExtra("likes");

        for (int i = 0; i < this.likes.size(); ++i) {
            Like c = this.likes.get(i);

            Log.d("liked by", c.getUsername());
        }

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