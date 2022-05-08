package com.example.memestream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LikesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);

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

        // Navigate back to the FeedActivity
        Intent intent = new Intent(LikesActivity.this, FeedActivity.class);
        LikesActivity.this.startActivity(intent);

    }
}