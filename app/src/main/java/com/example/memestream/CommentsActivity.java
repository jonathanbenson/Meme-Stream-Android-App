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

        // Set the add comment button onclick listener
        Button addCommentButton = this.findViewById(R.id.addCommentButton);

        addCommentButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                commentsActivity.addComment();
            }

        });

        // Set the go back button onclick listener
        Button commentsGoBackButton = this.findViewById(R.id.commentsGoBackButton);

        commentsGoBackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                commentsActivity.goBack();
            }

        });
    }

    public void addComment() {
        // Navigate to the AddCommentActivity
        Intent intent = new Intent(CommentsActivity.this, AddCommentActivity.class);
        CommentsActivity.this.startActivity(intent);

    }

    public void goBack() {

        // Navigate back to the FeedActivity
        Intent intent = new Intent(CommentsActivity.this, FeedActivity.class);
        CommentsActivity.this.startActivity(intent);

    }
}