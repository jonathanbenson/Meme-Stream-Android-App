package com.example.memestream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
    }

    public void goBack() {

        // Navigate back to the FeedActivity
        Intent intent = new Intent(AddCommentActivity.this, CommentsActivity.class);
        AddCommentActivity.this.startActivity(intent);

    }
}