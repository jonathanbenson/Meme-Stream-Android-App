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
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static final String serverBase = "http://10.0.2.2:3000/";
    private String sessionKey = "";
    private String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity mainActivity = this;


        // Set the login onClick listener
        Button loginButton = this.findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) { mainActivity.login(); }

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

    public void login() {

        String username = ((EditText)findViewById(R.id.loginUsernameField)).getText().toString();
        String password = ((EditText)findViewById(R.id.loginPasswordField)).getText().toString();

        if (username.isEmpty() || password.isEmpty())
            Toast.makeText(this, "Username and password fields cannot be left blank!", Toast.LENGTH_SHORT).show();
        else
            new LoginTask().execute(this, username, password);
    }

    public void register() {
        // Goes to the RegisterActivity so the user can register an account

        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public void navigateToFeed() {
        // Navigate back to the FeedActivity
        Intent intent = new Intent(MainActivity.this, FeedActivity.class);

        intent.putExtra("username", this.username);
        intent.putExtra("sessionKey", this.sessionKey);

        MainActivity.this.startActivity(intent);
    }

    public void setSessionKey(String newSessionKey) { this.sessionKey = newSessionKey; }
    public void setUsername(String newUsername) { this.username = newUsername; }
}

class LoginTask extends AsyncTask<Object, Void, String> {

    private MainActivity mainActivity;

    private String username;
    private String sessionKey;

    @Override
    protected String doInBackground(Object... params) {

        this.mainActivity = (MainActivity)params[0];

        this.username = (String)params[1];
        String password = (String)params[2];

        String query = MainActivity.serverBase + "login/" + this.username + "/" + password;

        String response = HttpRequest.executeGet(query);

        return response;
    }


    @Override
    protected void onPostExecute(String result) {
        try {

            final JSONObject json = new JSONObject(result);

            int status = json.getInt("status");

            if (status == 0)
                throw new Exception();

            String sKey = json.getString("key");

            this.mainActivity.setSessionKey(sKey);
            this.mainActivity.setSessionKey(this.username);

            Toast.makeText(this.mainActivity, "Welcome back, " + this.username + "!", Toast.LENGTH_SHORT).show();

            this.mainActivity.navigateToFeed();

        }
        catch (JSONException exc) {
            Toast.makeText(this.mainActivity, "Cannot connect to server!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception excep) {
            Toast.makeText(this.mainActivity, "Invalid username or password!", Toast.LENGTH_SHORT).show();
        }
    }
}
