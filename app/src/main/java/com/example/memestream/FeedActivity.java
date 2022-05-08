package com.example.memestream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FeedActivity extends AppCompatActivity {

    private ViewPager viewPager;

    private int[] images = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4,
            R.drawable.a5, R.drawable.a6};

    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // Initialze ViewPager

        this.viewPager = (ViewPager)findViewById(R.id.contentViewPager);

        this.viewPagerAdapter = new ViewPagerAdapter(FeedActivity.this, images);

        this.viewPager.setAdapter(this.viewPagerAdapter);



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
        Button likeButton = this.findViewById(R.id.likeButton);

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { feedActivity.like(); }
        });

        // Set the view comments button onclick listener
        Button commentButton = this.findViewById(R.id.commentButton);

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { feedActivity.comment(); }
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

    public void like() {

        String currentPost = "a" + String.valueOf(this.viewPager.getCurrentItem() + 1);

        Toast.makeText(this, currentPost, Toast.LENGTH_SHORT).show();

    }

    public void viewLikes() {

        // Navigate to the LikesActivity to view the likes
        Intent intent = new Intent(FeedActivity.this, LikesActivity.class);
        FeedActivity.this.startActivity(intent);

    }

    public void comment() {
        // Navigate to the AddCommentActivity to add a comment
        Intent intent = new Intent(FeedActivity.this, AddCommentActivity.class);
        FeedActivity.this.startActivity(intent);
    }

    public void viewComments() {

        // Navigate to the CommentsActivity to view the comments
        Intent intent = new Intent(FeedActivity.this, CommentsActivity.class);
        FeedActivity.this.startActivity(intent);

    }
}