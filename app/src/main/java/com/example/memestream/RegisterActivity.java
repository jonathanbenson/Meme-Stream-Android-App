package com.example.memestream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        // Set the login onClick listener
        Button registerSubmitButton = this.findViewById(R.id.registerSubmitButton);

        RegisterActivity registerActivity = this;

        registerSubmitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(registerActivity, "register submit", Toast.LENGTH_SHORT).show();
            }

        });


        // Set the cancel onClick listener
        Button registerCancelButton = this.findViewById(R.id.registerCancelButton);

        registerCancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                registerActivity.cancel();
            }

        });
    }

    public void cancel() {
        // Cancels registration by going back to the MainActivity (login screen)

        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        RegisterActivity.this.startActivity(intent);
    }

    public void register() {

    }
}