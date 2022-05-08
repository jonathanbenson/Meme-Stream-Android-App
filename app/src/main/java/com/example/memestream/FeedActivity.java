package com.example.memestream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        FeedActivity feedActivity = this;

        // Set the logout button onclick listener
        Button logoutButton = this.findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                feedActivity.logout();
            }

        });
    }

    public void logout() {

        // user is logging out, so reset the session key and username
        MainActivity.sessionKey = "";
        MainActivity.username = "";

        // Navigate back to the MainActivity
        Intent intent = new Intent(FeedActivity.this, MainActivity.class);
        FeedActivity.this.startActivity(intent);
    }
}