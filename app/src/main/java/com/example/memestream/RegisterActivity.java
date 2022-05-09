package com.example.memestream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    /*
    The RegisterActivity allows a user to register an account with the app.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RegisterActivity registerActivity = this;

        // Set the login onClick listener
        Button registerSubmitButton = this.findViewById(R.id.registerSubmitButton);
        registerSubmitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) { registerActivity.register(); }

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
        // Handles an attempt to register an account from the user

        String username = ((EditText)findViewById(R.id.registerUsernameField)).getText().toString();
        String password = ((EditText)findViewById(R.id.registerPasswordField)).getText().toString();
        String retypePassword = ((EditText)findViewById(R.id.registerRetypePasswordField)).getText().toString();

        // if any of the fields are left blank
        // then notify the user
        if (username.isEmpty() || password.isEmpty() || retypePassword.isEmpty())
            Toast.makeText(this, "Fields can't be empty!", Toast.LENGTH_SHORT).show();
        // if the text in the password field doesn't match the text in the retype password field
        // then notify the user
        else if (!password.equals(retypePassword))
            Toast.makeText(this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
        // if the user has entered all the required info
        // then query the database to register
        else
            new RegisterTask().execute(this, username, password);

    }

    public void navigateToFeed(String username, String sessionKey) {
        // Navigate back to the FeedActivity
        Intent intent = new Intent(RegisterActivity.this, FeedActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("sessionKey", sessionKey);

        RegisterActivity.this.startActivity(intent);
    }
}

class RegisterTask extends AsyncTask<Object, Void, String> {

    /*
    An asynchronous task to register a new account to the server.
     */

    private RegisterActivity registerActivity;

    private String username;

    @Override
    protected String doInBackground(Object... params) {

        this.registerActivity = (RegisterActivity)params[0];
        this.username = (String)params[1];
        String password = (String)params[2];

        // Create the query
        // Uses the /register/:username/:password endpoint on the server
        String query = MainActivity.serverBase + "register/" + this.username + "/" + password;

        // Get the response from the server
        String response = HttpRequest.executeGet(query);
        return response;
    }


    @Override
    protected void onPostExecute(String result) {

        try {

            // Parse the response from the server as JSON
            final JSONObject json = new JSONObject(result);

            // If the status of the response is 0 (fail)
            // then raise an exception
            int status = json.getInt("status");
            if (status == 0)
                throw new Exception();

            String sKey = json.getString("key");

            // At this point the server has successfully registered a new account for the user
            // Give the user a warm welcome and navigate to the FeedActivity
            Toast.makeText(this.registerActivity, "Welcome, " + this.username + "!", Toast.LENGTH_SHORT).show();
            this.registerActivity.navigateToFeed(this.username, sKey);

        }
        catch (JSONException exc) {
            Toast.makeText(this.registerActivity, "Cannot connect to server!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception excep) {
            Toast.makeText(this.registerActivity, "Username already exists!", Toast.LENGTH_SHORT).show();
        }


    }

}
