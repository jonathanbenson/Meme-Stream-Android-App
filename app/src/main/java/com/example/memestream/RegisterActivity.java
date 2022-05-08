package com.example.memestream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

                String username = ((EditText)findViewById(R.id.registerUsernameField)).getText().toString();
                String password = ((EditText)findViewById(R.id.registerPasswordField)).getText().toString();
                String retypePassword = ((EditText)findViewById(R.id.registerRetypePasswordField)).getText().toString();

                if (username.isEmpty() || password.isEmpty() || retypePassword.isEmpty())
                    Toast.makeText(registerActivity, "Fields can't be empty!", Toast.LENGTH_SHORT).show();
                else if (!password.equals(retypePassword))
                    Toast.makeText(registerActivity, "Passwords don't match!", Toast.LENGTH_SHORT).show();
                else
                    new RegisterTask().execute(registerActivity, username, password);

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

        new RegisterTask().execute(this);

    }
}

class RegisterTask extends AsyncTask<Object, Void, String> {

    private RegisterActivity registerActivity;

    @Override
    protected String doInBackground(Object... params) {

        this.registerActivity = (RegisterActivity)params[0];

        String username = (String)params[1];
        String password = (String)params[2];

        String query = MainActivity.serverBase + "register/" + username + "/" + password;

        String response = HttpRequest.executeGet(query);

        return response;
    }


    @Override
    protected void onPostExecute(String result) {
        Log.d("register response", result);
    }

}
