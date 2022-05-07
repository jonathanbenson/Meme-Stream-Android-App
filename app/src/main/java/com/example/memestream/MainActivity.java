package com.example.memestream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the login onClick listener
        Button loginButton = this.findViewById(R.id.loginButton);



        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "login", Toast.LENGTH_SHORT).show();
            }

        });

        // Set the register onClick listener
        Button registerButton = this.findViewById(R.id.registerButton);

        MainActivity mainActivity = this;

        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mainActivity.register();
            }

        });

    }

    public void register() {
        // Goes to the RegisterActivity so the user can register an account

        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        MainActivity.this.startActivity(intent);
    }
}