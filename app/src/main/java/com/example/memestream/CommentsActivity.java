package com.example.memestream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Vector;

public class CommentsActivity extends AppCompatActivity {

    private ArrayList<Comment> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        this.comments = (ArrayList)this.getIntent().getSerializableExtra("comments");

        for (int i = 0; i < this.comments.size(); ++i) {
            Comment c = this.comments.get(i);

            Log.d("comment", c.getUsername() + " said, \"" + c.getComment() + "\"");
        }

        CommentsActivity commentsActivity = this;

        // Set the go back button onclick listener
        Button commentsGoBackButton = this.findViewById(R.id.commentsGoBackButton);

        commentsGoBackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                commentsActivity.goBack();
            }

        });
    }

    public void goBack() {

        Bundle extras = this.getIntent().getExtras();
        String username = extras.getString("username");
        String sessionKey = extras.getString("sessionKey");
        String post = extras.getString("post");

        // Navigate back to the FeedActivity
        Intent intent = new Intent(CommentsActivity.this, FeedActivity.class);

        intent.putExtra("username", username);
        intent.putExtra("sessionKey", sessionKey);
        intent.putExtra("post", post);

        CommentsActivity.this.startActivity(intent);

    }
}