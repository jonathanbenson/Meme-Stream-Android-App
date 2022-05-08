package com.example.memestream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TutorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        TutorialActivity tutorialActivity = this;

        // Set the go back button onclick listener
        Button tutorialGoBackButton = this.findViewById(R.id.tutorialGoBackButton);

        tutorialGoBackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                tutorialActivity.goBack();
            }

        });
    }

    public void goBack() {

        // Navigate back to the FeedActivity
        Intent intent = new Intent(TutorialActivity.this, FeedActivity.class);
        TutorialActivity.this.startActivity(intent);

    }
}