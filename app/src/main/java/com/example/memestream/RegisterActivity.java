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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        // Set the login onClick listener
        Button registerSubmitButton = this.findViewById(R.id.registerSubmitButton);

        RegisterActivity registerActivity = this;

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

        String username = ((EditText)findViewById(R.id.registerUsernameField)).getText().toString();
        String password = ((EditText)findViewById(R.id.registerPasswordField)).getText().toString();
        String retypePassword = ((EditText)findViewById(R.id.registerRetypePasswordField)).getText().toString();

        if (username.isEmpty() || password.isEmpty() || retypePassword.isEmpty())
            Toast.makeText(this, "Fields can't be empty!", Toast.LENGTH_SHORT).show();
        else if (!password.equals(retypePassword))
            Toast.makeText(this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
        else
            new RegisterTask().execute(this, username, password);

    }

    public void navigateToFeed() {
        // Navigate back to the FeedActivity
        Intent intent = new Intent(RegisterActivity.this, FeedActivity.class);
        RegisterActivity.this.startActivity(intent);
    }
}

class RegisterTask extends AsyncTask<Object, Void, String> {

    private RegisterActivity registerActivity;

    private String username;

    @Override
    protected String doInBackground(Object... params) {

        this.registerActivity = (RegisterActivity)params[0];

        this.username = (String)params[1];
        String password = (String)params[2];

        String query = MainActivity.serverBase + "register/" + this.username + "/" + password;

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

            MainActivity.sessionKey = sKey;
            MainActivity.username = this.username;

            Toast.makeText(this.registerActivity, "Welcome, " + this.username + "!", Toast.LENGTH_SHORT).show();

            this.registerActivity.navigateToFeed();

        }
        catch (JSONException exc) {
            Toast.makeText(this.registerActivity, "Cannot connect to server!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception excep) {
            Toast.makeText(this.registerActivity, "Username already exists!", Toast.LENGTH_SHORT).show();
        }


    }

}
