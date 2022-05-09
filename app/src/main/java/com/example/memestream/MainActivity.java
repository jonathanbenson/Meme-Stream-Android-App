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
    /*
    The MainActivity serves as the login screen for the app.
     */

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
                mainActivity.navigateToRegisterActivity();
            }
        });

    }

    public void login() {
        /*

        Called when the user presses the login button.

         */

        // Retrieve username and password entered from the user
        String username = ((EditText)findViewById(R.id.loginUsernameField)).getText().toString();
        String password = ((EditText)findViewById(R.id.loginPasswordField)).getText().toString();

        // if either one of the username or password fields is empty
        // then tell the user they can't be empty...do nothing
        if (username.isEmpty() || password.isEmpty())
            Toast.makeText(this, "Username and password fields cannot be left blank!", Toast.LENGTH_SHORT).show();
        // if the user has provided a username and password
        // then attempt to login
        else
            new LoginTask().execute(this, username, password);
    }

    public void navigateToRegisterActivity() {
        // Goes to the RegisterActivity so the user can register an account

        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public void navigateToFeedActivity() {
        // Navigate back to the FeedActivity
        Intent intent = new Intent(MainActivity.this, FeedActivity.class);

        intent.putExtra("username", this.username);
        intent.putExtra("sessionKey", this.sessionKey);

        MainActivity.this.startActivity(intent);
    }

    // Setters for the username and session key of the user
    public void setSessionKey(String newSessionKey) { this.sessionKey = newSessionKey; }
    public void setUsername(String newUsername) { this.username = newUsername; }
}

class LoginTask extends AsyncTask<Object, Void, String> {

    /*
    An asynchronous task to query the server if the user has provided a correct
    username and password to login.
     */

    private MainActivity mainActivity;
    private String username;

    @Override
    protected String doInBackground(Object... params) {

        // Initialize variables
        this.mainActivity = (MainActivity)params[0];
        this.username = (String)params[1];
        String password = (String)params[2];

        // Construct query to send to the server
        // Queries the /login/:username/:password endpoint
        String query = MainActivity.serverBase + "login/" + this.username + "/" + password;

        // Fetch the response from the server
        String response = HttpRequest.executeGet(query);
        return response;
    }


    @Override
    protected void onPostExecute(String result) {
        try {

            // Parse the response as a JSON object
            // ...it should have a status field (1 for success, 0 for fail)
            final JSONObject json = new JSONObject(result);
            int status = json.getInt("status");

            // If the server failed to process the request
            // then throw an error
            if (status == 0)
                throw new Exception();

            // Parse the session key from the server
            String sKey = json.getString("key");

            // Set the username and session key variables in the MainActivity
            this.mainActivity.setSessionKey(sKey);
            this.mainActivity.setUsername(this.username);

            // At this point, the user has logged in successfully
            // Give the user a warm welcome. and navigate to the FeedActivity
            Toast.makeText(this.mainActivity, "Welcome back, " + this.username + "!", Toast.LENGTH_SHORT).show();
            this.mainActivity.navigateToFeedActivity();

        }
        // Error handling
        catch (JSONException exc) {
            Toast.makeText(this.mainActivity, "Cannot connect to server!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception excep) {
            Toast.makeText(this.mainActivity, "Invalid username or password!", Toast.LENGTH_SHORT).show();
        }
    }
}
