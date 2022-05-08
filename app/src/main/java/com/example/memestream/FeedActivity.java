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

        // Set the view tutorial button onclick listener
        Button viewTutorialButton = this.findViewById(R.id.viewTutorialButton);

        viewTutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { feedActivity.viewTutorial(); }
        });

        // Set the view likes button onclick listener
        Button viewLikesButton = this.findViewById(R.id.viewLikesButton);

        viewLikesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { feedActivity.viewLikes(); }
        });

        // Set the view comments button onclick listener
        Button viewCommentsButton = this.findViewById(R.id.viewCommentsButton);

        viewCommentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { feedActivity.viewComments(); }
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

    public void viewTutorial() {

        // Navigate to the TutorialActivity to view the tutorial
        Intent intent = new Intent(FeedActivity.this, TutorialActivity.class);
        FeedActivity.this.startActivity(intent);

    }

    public void viewLikes() {

        // Navigate to the TutorialActivity to view the tutorial
        Intent intent = new Intent(FeedActivity.this, LikesActivity.class);
        FeedActivity.this.startActivity(intent);

    }

    public void viewComments() {

        // Navigate to the TutorialActivity to view the tutorial
        Intent intent = new Intent(FeedActivity.this, CommentsActivity.class);
        FeedActivity.this.startActivity(intent);

    }
}