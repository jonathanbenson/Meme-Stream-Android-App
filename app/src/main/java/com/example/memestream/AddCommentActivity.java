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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class AddCommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        AddCommentActivity addCommentActivity = this;

        // Set the go back button onclick listener
        Button addCommentGoBackButton = this.findViewById(R.id.addCommentGoBackButton);

        addCommentGoBackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                addCommentActivity.goBack();
            }

        });

        // Set the submit comment button onclick listener
        Button commentSubmitButton = this.findViewById(R.id.commentSubmitButton);

        commentSubmitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                addCommentActivity.submitComment();
            }

        });
    }

    public void goBack() {

        Bundle extras = this.getIntent().getExtras();
        String username = extras.getString("username");
        String sessionKey = extras.getString("sessionKey");

        // Navigate back to the FeedActivity
        Intent intent = new Intent(AddCommentActivity.this, FeedActivity.class);

        intent.putExtra("username", username);
        intent.putExtra("sessionKey", sessionKey);

        AddCommentActivity.this.startActivity(intent);

    }

    public void submitComment() {

        Bundle extras = this.getIntent().getExtras();

        String username = extras.getString("username");
        String sessionKey = extras.getString("sessionKey");
        String post = extras.getString("post");
        String comment = ((EditText)this.findViewById(R.id.commentText)).getText().toString();

        if (comment.isEmpty())
            Toast.makeText(this, "Comment must not be empty!", Toast.LENGTH_SHORT).show();
        else
            new CommentTask().execute(this, username, sessionKey, post, comment);

    }
}

class CommentTask extends AsyncTask<Object, Void, String> {

    private AddCommentActivity addCommentActivity;

    @Override
    protected String doInBackground(Object... params) {

        this.addCommentActivity = (AddCommentActivity)params[0];

        String username = (String)params[1];
        String sessionKey = (String)params[2];
        String postTitle = (String)params[3];
        String comment = (String)params[4];

        String query = MainActivity.serverBase + "comment/" + username + "/" + sessionKey + "/" + postTitle + "/" + comment;

        String response = HttpRequest.executeGet(query);

        return response;
    }


    @Override
    protected void onPostExecute(String result) {

        try {

            JSONObject res = new JSONObject(result);

            if (res.getInt("status") == 1) {
                Toast.makeText(this.addCommentActivity, "Thanks for the feedback!", Toast.LENGTH_SHORT).show();
                this.addCommentActivity.goBack();
            }
            else
                Toast.makeText(this.addCommentActivity, "An error occured!", Toast.LENGTH_SHORT).show();

        }
        catch (JSONException exc) {
            Toast.makeText(this.addCommentActivity, "Could not connect to database!", Toast.LENGTH_SHORT).show();
        }

    }

}