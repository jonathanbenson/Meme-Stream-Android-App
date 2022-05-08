package com.example.memestream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AddCommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        AddCommentActivity addCommentActivity = this;

        // Set the go back button onclick listener
        Button addCommentGoBackButton = this.findViewById(R.id.addCommentGoBackButton);

        addCommentGoBackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                addCommentActivity.goBack();
            }

        });

        // Set the submit comment button onclick listener
        Button commentSubmitButton = this.findViewById(R.id.commentSubmitButton);

        commentSubmitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                addCommentActivity.submitComment();
            }

        });
    }

    public void goBack() {

        // Navigate back to the FeedActivity
        Intent intent = new Intent(AddCommentActivity.this, FeedActivity.class);

        Bundle extras = this.getIntent().getExtras();

        String username = extras.getString("username");
        String sessionKey = extras.getString("sessionKey");
        String post = extras.getString("post");

        intent.putExtra("username", username);
        intent.putExtra("sessionKey", sessionKey);

        AddCommentActivity.this.startActivity(intent);

    }

    public void submitComment() {

        Bundle extras = this.getIntent().getExtras();

        String username = extras.getString("username");
        String sessionKey = extras.getString("sessionKey");
        String post = extras.getString("post");

        Toast.makeText(this, post, Toast.LENGTH_SHORT).show();

    }
}