package com.example.memestream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class FeedActivity extends AppCompatActivity {

    private ViewPager viewPager;

    private int[] images = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4,
            R.drawable.a5, R.drawable.a6};

    private ViewPagerAdapter viewPagerAdapter;

    private Vector<Like> likes = new Vector<Like>();
    private Vector<Comment> comments = new Vector<Comment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        FeedActivity feedActivity = this;

        // Initialze ViewPager

        this.viewPager = (ViewPager)findViewById(R.id.contentViewPager);

        this.viewPagerAdapter = new ViewPagerAdapter(FeedActivity.this, images);

        this.viewPager.setAdapter(this.viewPagerAdapter);

        this.viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                feedActivity.beginPageChange();
            }

        });

        // Set the logout button onclick listener
        Button logoutButton = this.findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                feedActivity.logout();
            }

        });

        // Set the view tutorial button onclick listener
        Button viewTutorialButton = this.findViewById(R.id.viewTutorialButton);

        viewTutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { feedActivity.viewTutorial(); }
        });

        // Set the view likes button onclick listener
        Button likeButton = this.findViewById(R.id.likeButton);

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { feedActivity.handleLikeButtonOnClick(); }
        });

        // Set the view comments button onclick listener
        Button commentButton = this.findViewById(R.id.commentButton);

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { feedActivity.handleCommentButtonOnClick(); }
        });

        this.beginPageChange();
    }

    public void beginPageChange() {
        // Executes whenever the ViewPager is swiped

        String currentPost = "a" + String.valueOf(this.viewPager.getCurrentItem() + 1);

        new FetchLikesTask().execute(this, currentPost);
        new FetchCommentsTask().execute(this, currentPost);

    }

    public void afterFetchLikes() {

        Button likeButton = (Button)this.findViewById(R.id.likeButton);

        if (this.hasLiked()) {
            likeButton.setText("view likes");
        }
        else {
            likeButton.setText("like");
        }

    }

    private boolean hasLiked() {
        // Checks if the current user has liked the current post

        String username = this.getIntent().getExtras().getString("username");

        for (int i = 0; i < this.likes.size(); ++i) {
            if (likes.get(i).getUsername().equals(username))
                return true;
        }

        return false;

    }

    public void afterFetchComments() {

        Button commentButton = (Button)this.findViewById(R.id.commentButton);

        if (this.hasCommented()) {
            commentButton.setText("view comments");
        }
        else {
            commentButton.setText("comment");
        }
    }

    private boolean hasCommented() {
        // Checks if the current user has commented on the current post

        String username = this.getIntent().getExtras().getString("username");

        for (int i = 0; i < this.comments.size(); ++i) {
            if (comments.get(i).getUsername().equals(username))
                return true;
        }

        return false;
    }

    public void logout() {

        // Navigate back to the MainActivity
        Intent intent = new Intent(FeedActivity.this, MainActivity.class);
        FeedActivity.this.startActivity(intent);
    }

    public void viewTutorial() {

        Bundle extras = this.getIntent().getExtras();
        String username = extras.getString("username");
        String sessionKey = extras.getString("sessionKey");

        // Navigate to the TutorialActivity to view the tutorial
        Intent intent = new Intent(FeedActivity.this, TutorialActivity.class);

        intent.putExtra("username", username);
        intent.putExtra("sessionKey", sessionKey);

        FeedActivity.this.startActivity(intent);

    }

    public void handleLikeButtonOnClick() {
        if (this.hasLiked())
            this.viewLikes();
        else
            this.like();
    }

    public void handleCommentButtonOnClick() {
        if (this.hasCommented())
            this.viewComments();
        else
            this.comment();
    }

    public void like() {

        String currentPost = "a" + String.valueOf(this.viewPager.getCurrentItem() + 1);

        Bundle extras = this.getIntent().getExtras();
        String username = extras.getString("username");
        String sessionKey = extras.getString("sessionKey");

        new LikeTask().execute(this, username, sessionKey, currentPost);

    }

    public void viewLikes() {

        Bundle extras = this.getIntent().getExtras();
        String username = extras.getString("username");
        String sessionKey = extras.getString("sessionKey");

        // Navigate to the LikesActivity to view the likes
        Intent intent = new Intent(FeedActivity.this, LikesActivity.class);

        intent.putExtra("username", username);
        intent.putExtra("sessionKey", sessionKey);

        FeedActivity.this.startActivity(intent);

    }

    public void comment() {

        String currentPost = "a" + String.valueOf(this.viewPager.getCurrentItem() + 1);

        // Navigate to the AddCommentActivity to add a comment
        Intent intent = new Intent(FeedActivity.this, AddCommentActivity.class);

        Bundle extras = this.getIntent().getExtras();
        String username = extras.getString("username");
        String sessionKey = extras.getString("sessionKey");

        intent.putExtra("username", username);
        intent.putExtra("sessionKey", sessionKey);
        intent.putExtra("post", currentPost);

        FeedActivity.this.startActivity(intent);
    }

    public void viewComments() {

        Bundle extras = this.getIntent().getExtras();
        String username = extras.getString("username");
        String sessionKey = extras.getString("sessionKey");

        // Navigate to the CommentsActivity to view the comments
        Intent intent = new Intent(FeedActivity.this, CommentsActivity.class);

        intent.putExtra("username", username);
        intent.putExtra("sessionKey", sessionKey);

        FeedActivity.this.startActivity(intent);

    }

    public void setLikes(Vector<Like> likes) { this.likes = likes; }
    public void setComments(Vector<Comment> comments) { this.comments = comments; }
}

class Like {

    private String username;

    public Like(String username) {
        this.username = username;
    }

    public String getUsername() { return this.username; }
}

class Comment {

    private String username;
    private String comment;

    public Comment(String username, String comment) {
        this.username = username;
        this.comment = comment;
    }

    public String getUsername() { return this.username; }
    public String getComment() { return this.comment; }


}

class LikeTask extends AsyncTask<Object, Void, String> {

    private FeedActivity feedActivity;

    @Override
    protected String doInBackground(Object... params) {

        this.feedActivity = (FeedActivity)params[0];

        String username = (String)params[1];
        String sessionKey = (String)params[2];
        String postTitle = (String)params[3];

        String query = MainActivity.serverBase + "like/" + username + "/" + sessionKey + "/" + postTitle;

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

            Toast.makeText(this.feedActivity, "Liked post!", Toast.LENGTH_SHORT).show();

        }
        catch (JSONException exc) {
            Toast.makeText(this.feedActivity, "Cannot connect to server!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception excep) {
            Toast.makeText(this.feedActivity, "An error occurred!", Toast.LENGTH_SHORT).show();
        }

    }

}

class FetchLikesTask extends AsyncTask<Object, Void, String> {

    private FeedActivity feedActivity;

    @Override
    protected String doInBackground(Object... params) {

        this.feedActivity = (FeedActivity)params[0];

        String postTitle = (String)params[1];

        String query = MainActivity.serverBase + "likes/" + postTitle;

        String response = HttpRequest.executeGet(query);

        return response;
    }


    @Override
    protected void onPostExecute(String result) {

        try {
            Vector<Like> likes = new Vector<Like>();

            JSONArray likesJSON = new JSONArray(result);

            for (int i = 0; i < likesJSON.length(); ++i) {
                JSONObject likeJSON = likesJSON.getJSONObject(i);

                String username = likeJSON.getString("username");

                likes.add(new Like(username));
            }

            this.feedActivity.setLikes(likes);

            this.feedActivity.afterFetchLikes();

        }
        catch (JSONException exc) {
            Toast.makeText(this.feedActivity, "Cannot connect to server!", Toast.LENGTH_SHORT).show();
        }

    }

}

class FetchCommentsTask extends AsyncTask<Object, Void, String> {

    private FeedActivity feedActivity;

    @Override
    protected String doInBackground(Object... params) {

        this.feedActivity = (FeedActivity)params[0];

        String postTitle = (String)params[1];

        String query = MainActivity.serverBase + "comments/" + postTitle;

        String response = HttpRequest.executeGet(query);

        return response;
    }


    @Override
    protected void onPostExecute(String result) {

        try {
            Vector<Comment> comments = new Vector<Comment>();

            JSONArray commentsJSON = new JSONArray(result);

            for (int i = 0; i < commentsJSON.length(); ++i) {
                JSONObject commentJSON = commentsJSON.getJSONObject(i);

                String username = commentJSON.getString("username");
                String comment = commentJSON.getString("comment");

                comments.add(new Comment(username, comment));
            }

            this.feedActivity.setComments(comments);

            this.feedActivity.afterFetchComments();

        }
        catch (JSONException exc) {
            Toast.makeText(this.feedActivity, "Cannot connect to server!", Toast.LENGTH_SHORT).show();
        }

    }

}