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
    /*
    The AddCommentActivity allows a user to submit a comment on a particular post.
     */
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
        // The goBack method navigates back to the FeedActivity

        Bundle extras = this.getIntent().getExtras();
        String username = extras.getString("username");
        String sessionKey = extras.getString("sessionKey");
        String post = extras.getString("post");

        // Navigate back to the FeedActivity
        Intent intent = new Intent(AddCommentActivity.this, FeedActivity.class);

        intent.putExtra("username", username);
        intent.putExtra("sessionKey", sessionKey);
        intent.putExtra("post", post);

        AddCommentActivity.this.startActivity(intent);

    }

    public void submitComment() {
        /*
        Queries the database with a comment to submit on a post.
         */
        Bundle extras = this.getIntent().getExtras();

        String username = extras.getString("username");
        String sessionKey = extras.getString("sessionKey");
        String post = extras.getString("post");
        String comment = ((EditText)this.findViewById(R.id.commentText)).getText().toString();

        // if the comment input field is blank
        // then remind the user to enter a comment...and do nothing
        if (comment.isEmpty())
            Toast.makeText(this, "Comment must not be empty!", Toast.LENGTH_SHORT).show();
        // if the user has entered a comment
        // then send the comment to the database
        else
            new CommentTask().execute(this, username, sessionKey, post, comment);

    }
}

class CommentTask extends AsyncTask<Object, Void, String> {

    /*
    Asynchronously sends a comment to the database for a particular post.
     */

    private AddCommentActivity addCommentActivity;

    @Override
    protected String doInBackground(Object... params) {

        this.addCommentActivity = (AddCommentActivity)params[0];

        String username = (String)params[1];
        String sessionKey = (String)params[2];
        String postTitle = (String)params[3];
        String comment = (String)params[4];

        // Construct the query to send to the server
        String query = MainActivity.serverBase + "comment/" + username + "/" + sessionKey + "/" + postTitle + "/" + comment;

        // Get the response from the server
        String response = HttpRequest.executeGet(query);
        return response;
    }


    @Override
    protected void onPostExecute(String result) {

        try {
            // Parse the response's JSON content
            JSONObject res = new JSONObject(result);

            // If the status from the response is 1 (success)
            // then thank the user for their feedback and navigate back to the FeedActivity
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