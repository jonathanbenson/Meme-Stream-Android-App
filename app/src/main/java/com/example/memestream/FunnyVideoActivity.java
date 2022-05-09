package com.example.memestream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class FunnyVideoActivity extends AppCompatActivity {
    /*
    The FunnyVideoActivity hosts a funny video.
    The path of the funny video is res/raw/funnyvideo.mp4
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funny_video);

        FunnyVideoActivity funnyVideoActivity = this;

        // Set the go back button onclick listener
        Button tutorialGoBackButton = this.findViewById(R.id.tutorialGoBackButton);

        tutorialGoBackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                funnyVideoActivity.goBack();
            }

        });

        // Load the funny video file into the VideoView container
        VideoView videoView = (VideoView)this.findViewById(R.id.funnyVideoVideoView);
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.funnyvideo);

        // Play the video
        videoView.start();


    }

    public void goBack() {
        // Navigates back to the FeedActivity

        Bundle extras = this.getIntent().getExtras();
        String username = extras.getString("username");
        String sessionKey = extras.getString("sessionKey");
        String post = extras.getString("post");

        // Navigate back to the FeedActivity
        Intent intent = new Intent(FunnyVideoActivity.this, FeedActivity.class);

        intent.putExtra("username", username);
        intent.putExtra("sessionKey", sessionKey);
        intent.putExtra("post", post);

        FunnyVideoActivity.this.startActivity(intent);

    }
}