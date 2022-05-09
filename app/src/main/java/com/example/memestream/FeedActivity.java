package com.example.memestream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {
    /*
    The FeedActivity is where the user can scroll through the different memes of the application.

    This is where users can like and comment on memes. They can also view a funny video by clicking
    the funny video button in the top right.
     */
    private ViewPager viewPager;

    private int[] images = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4,
            R.drawable.a5, R.drawable.a6};

    private ViewPagerAdapter viewPagerAdapter;

    private ArrayList<Like> likes = new ArrayList<Like>();
    private ArrayList<Comment> comments = new ArrayList<Comment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        FeedActivity feedActivity = this;


        // Setup of the ViewPager that will house the memes
        this.viewPager = (ViewPager)findViewById(R.id.contentViewPager);
        this.viewPagerAdapter = new ViewPagerAdapter(FeedActivity.this, images);
        this.viewPager.setAdapter(this.viewPagerAdapter);

        // Set a SimpleOnPageChangeListener to listen for when the user views a new image
        // This will cause the app to query the database for the meme's likes and comments
        this.viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                feedActivity.beginPageChange();
            }

        });

        // The following two statements are to make sure the FeedActivity loads the last post
        // the user was looking at before they navigated away to another activity
        String currentPost = this.getIntent().getExtras().getString("post");
        if (currentPost != null)
            this.viewPager.setCurrentItem(this.positionFromPostTitle(currentPost));

        // Set the logout button onclick listener
        // this will send the user back to the MainActivity
        Button logoutButton = this.findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                feedActivity.navigateToMainActivity();
            }

        });

        // Set the funny video button onclick listener
        // this will send the user to the FunnyVideoActivity
        Button funnyVideoButton = this.findViewById(R.id.funnyVideoButton);

        funnyVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { feedActivity.navigateToFunnyVideoActivity(); }
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
        // Retrieves the likes and comments of the current post

        String currentPost = this.getCurrentPostTitle();

        new FetchLikesTask().execute(this, currentPost);
        new FetchCommentsTask().execute(this, currentPost);

    }

    public void afterFetchLikes() {
        // After the app retrieves the likes of the current post
        // it will check and see if the user has liked the current post.
        // If the user has liked the current post
        // then the like button will say "view likes" instead of just "like"
        // and it will have a different functionality (see handleLikeButtonOnClick for more info)

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
        // After the app retrieves the comments of the current post
        // it will check and see if the user has commented on the current post.
        // If the user has commented on the current post
        // then the like button will say "view comments" instead of just "comment"
        // and it will have a different functionality (see handleCommentButtonOnClick for more info)

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

    public void navigateToMainActivity() {
        // Navigates back to the MainActivity

        Intent intent = new Intent(FeedActivity.this, MainActivity.class);
        FeedActivity.this.startActivity(intent);
    }

    public void navigateToFunnyVideoActivity() {
        // Navigates to the FunnyVideoActivity

        Bundle extras = this.getIntent().getExtras();
        String username = extras.getString("username");
        String sessionKey = extras.getString("sessionKey");
        String post = this.getCurrentPostTitle();

        // Navigate to the FunnyVideoActivity to view the funny video
        Intent intent = new Intent(FeedActivity.this, FunnyVideoActivity.class);

        intent.putExtra("username", username);
        intent.putExtra("sessionKey", sessionKey);
        intent.putExtra("post", post);

        FeedActivity.this.startActivity(intent);

    }

    public void handleLikeButtonOnClick() {
        // If the user has already liked the current post
        // then navigate to the LikesActivity to view who liked the post
        if (this.hasLiked())
            this.navigateToLikesActivity();
        else
        // else just like the post
            this.like();
    }

    public void handleCommentButtonOnClick() {
        // If the user has already commented on the current post
        // then navigate to the CommentsActivity to view who commented on the post
        if (this.hasCommented())
            this.navigateToCommentsActivity();
        else
        // else navigate to the AddCommentActivity
            this.navigateToAddCommentActivity();
    }

    public void like() {
        // Queries the server that the user likes the current post

        String currentPost = this.getCurrentPostTitle();

        Bundle extras = this.getIntent().getExtras();
        String username = extras.getString("username");
        String sessionKey = extras.getString("sessionKey");

        new LikeTask().execute(this, username, sessionKey, currentPost);

    }

    public void navigateToLikesActivity() {
        // Navigates to the LikesActivity so the user can view who liked the current post

        Bundle extras = this.getIntent().getExtras();
        String username = extras.getString("username");
        String sessionKey = extras.getString("sessionKey");
        String post = this.getCurrentPostTitle();

        // Navigate to the LikesActivity to view the likes
        Intent intent = new Intent(FeedActivity.this, LikesActivity.class);

        intent.putExtra("username", username);
        intent.putExtra("sessionKey", sessionKey);
        intent.putExtra("post", post);
        intent.putExtra("likes", (Serializable)this.likes);

        FeedActivity.this.startActivity(intent);

    }

    public void navigateToAddCommentActivity() {
        // Navigate to the AddCommentActivity so the user can submit a comment on the current post

        String currentPost = this.getCurrentPostTitle();

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

    public void navigateToCommentsActivity() {
        // Navigate to the CommentsActivity so the user can view who commented on the current post

        Bundle extras = this.getIntent().getExtras();
        String username = extras.getString("username");
        String sessionKey = extras.getString("sessionKey");
        String post = this.getCurrentPostTitle();

        // Navigate to the CommentsActivity to view the comments
        Intent intent = new Intent(FeedActivity.this, CommentsActivity.class);

        intent.putExtra("username", username);
        intent.putExtra("sessionKey", sessionKey);
        intent.putExtra("post", post);
        intent.putExtra("comments", (Serializable)this.comments);

        FeedActivity.this.startActivity(intent);

    }

    private String getCurrentPostTitle() {
        // Gets the title of the post based on the position of the ViewPager
        return "a" + String.valueOf(this.viewPager.getCurrentItem() + 1);
    }

    private int positionFromPostTitle(String postTitle) {
        // Gets the position of the ViewPager based on the title of the post

        char c = postTitle.charAt(1);

        return Character.getNumericValue(c) - 1;
    }

    // Setters for the likes and comments of the current post
    public void setLikes(ArrayList<Like> likes) { this.likes = likes; }
    public void setComments(ArrayList<Comment> comments) { this.comments = comments; }
}


class LikeTask extends AsyncTask<Object, Void, String> {

    /*
    Asynchronous task to query the database that a user likes a certain post.
     */

    private FeedActivity feedActivity;
    private String postTitle;

    @Override
    protected String doInBackground(Object... params) {

        this.feedActivity = (FeedActivity)params[0];
        String username = (String)params[1];
        String sessionKey = (String)params[2];
        this.postTitle = (String)params[3];

        // Create the query to like a post
        // Uses the /like/:username/:sessionKey/:post endpoint on the server
        String query = MainActivity.serverBase + "like/" + username + "/" + sessionKey + "/" + this.postTitle;

        // Get the response from the server
        String response = HttpRequest.executeGet(query);
        return response;
    }


    @Override
    protected void onPostExecute(String result) {

        try {

            // Parse the response from the server as JSON
            final JSONObject json = new JSONObject(result);

            // If the status of the response is 0
            // then something when wrong on the server-side, so throw an error
            int status = json.getInt("status");
            if (status == 0)
                throw new Exception();

            // At this point, the server properly processed the like query
            // Let the user know they were able to like the post
            Toast.makeText(this.feedActivity, "Liked post!", Toast.LENGTH_SHORT).show();

            // Refresh the likes on the current post for the user to see
            new FetchLikesTask().execute(this.feedActivity, this.postTitle);

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

    /*
    Fetches all the likes of a particular post (usernames of the likes)
     */

    private FeedActivity feedActivity;

    @Override
    protected String doInBackground(Object... params) {

        this.feedActivity = (FeedActivity)params[0];

        String postTitle = (String)params[1];

        // Create the query to fetch the likes
        // Uses the /likes/:post endpoint on the server
        String query = MainActivity.serverBase + "likes/" + postTitle;

        // Get the response from the server
        String response = HttpRequest.executeGet(query);
        return response;
    }


    @Override
    protected void onPostExecute(String result) {

        try {
            ArrayList<Like> likes = new ArrayList<Like>();

            // parse the resultant JSON response
            JSONArray likesJSON = new JSONArray(result);

            // Convert the string response into a list of Like objects
            for (int i = 0; i < likesJSON.length(); ++i) {
                JSONObject likeJSON = likesJSON.getJSONObject(i);

                String username = likeJSON.getString("username");

                likes.add(new Like(username));

            }

            // Update the likes of the current post
            this.feedActivity.setLikes(likes);

            // Update the UI
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

        // Create the query to be sent to the server
        // Uses the /comments/:post endpoint
        String query = MainActivity.serverBase + "comments/" + postTitle;

        // Get the response from the server
        String response = HttpRequest.executeGet(query);
        return response;
    }


    @Override
    protected void onPostExecute(String result) {

        try {
            ArrayList<Comment> comments = new ArrayList<Comment>();

            // Parse the server response as JSON
            JSONArray commentsJSON = new JSONArray(result);

            // Convert the JSON response into a list of Comment objects
            for (int i = 0; i < commentsJSON.length(); ++i) {
                JSONObject commentJSON = commentsJSON.getJSONObject(i);

                String username = commentJSON.getString("username");
                String comment = commentJSON.getString("comment");

                comments.add(new Comment(username, comment));
            }

            // Set the comments of the current post
            this.feedActivity.setComments(comments);

            // Update the UI
            this.feedActivity.afterFetchComments();

        }
        catch (JSONException exc) {
            Toast.makeText(this.feedActivity, "Cannot connect to server!", Toast.LENGTH_SHORT).show();
        }

    }

}