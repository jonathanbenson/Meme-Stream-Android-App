package com.example.memestream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CommentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

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

        // Navigate back to the FeedActivity
        Intent intent = new Intent(CommentsActivity.this, FeedActivity.class);

        intent.putExtra("username", username);
        intent.putExtra("sessionKey", sessionKey);

        CommentsActivity.this.startActivity(intent);

    }
}