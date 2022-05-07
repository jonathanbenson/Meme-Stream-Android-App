package com.example.memestream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String serverBase = "http://10.0.2.2:3000/";
    public static String sessionKey = "";
    public static String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        int SDK_INT = android.os.Build.VERSION.SDK_INT;
//        if (SDK_INT > 8)
//        {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                    .permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        }

        MainActivity mainActivity = this;


        // Set the login onClick listener
        Button loginButton = this.findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                new LoginTask().execute(mainActivity);

            }

        });

        // Set the register onClick listener
        Button registerButton = this.findViewById(R.id.registerButton);

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

class LoginTask extends AsyncTask<MainActivity, Void, String> {

    private MainActivity mainActivity;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(MainActivity... mainActivity) {

        this.mainActivity = mainActivity[0];

        String response = HttpRequest.executeGet(MainActivity.serverBase);

        return response;
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Toast.makeText(this.mainActivity, result, Toast.LENGTH_SHORT).show();
    }
}
