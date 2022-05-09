package com.example.memestream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class CommentsActivity extends AppCompatActivity {
    /*
    The CommentsActivity showcases the comments for a particular post.

    It houses the comments in a RecyclerView container
     */

    CommentRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        // Get the comments from the FeedActivity
        ArrayList<Comment> comments = (ArrayList)this.getIntent().getSerializableExtra("comments");

        // Extract the comment's content and format each of them as Strings
        ArrayList<String> items = new ArrayList<String>();
        for (int i = 0; i < comments.size(); ++i) {
            Comment c = comments.get(i);

            String item = c.getUsername() + " said, \"" + c.getComment() + "\"";

            items.add(item);
        }

        // Create the recycler view to house the comments
        // and populate it with the formatted comment Strings above
        RecyclerView recyclerView = (RecyclerView)this.findViewById(R.id.commentsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.adapter = new CommentRecyclerViewAdapter(this, items);
        recyclerView.setAdapter(adapter);

        CommentsActivity commentsActivity = this;

        // Set the go back button onclick listener
        Button commentsGoBackButton = this.findViewById(R.id.commentsGoBackButton);

        commentsGoBackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                commentsActivity.goBack();
            }

        });
    }

    public void goBack() {
        // Navigate back to the FeedActivity

        Bundle extras = this.getIntent().getExtras();
        String username = extras.getString("username");
        String sessionKey = extras.getString("sessionKey");
        String post = extras.getString("post");

        // Navigate back to the FeedActivity
        Intent intent = new Intent(CommentsActivity.this, FeedActivity.class);

        intent.putExtra("username", username);
        intent.putExtra("sessionKey", sessionKey);
        intent.putExtra("post", post);

        CommentsActivity.this.startActivity(intent);

    }
}

class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder> {
    /*
    The recycler view adapter for the recycler view to hold the comments.
     */
    private List<String> data;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    // data is passed into the constructor
    CommentRecyclerViewAdapter(Context context, List<String> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the row layout from the recycler view xml

        View view = this.inflater.inflate(R.layout.recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Bind the data to the text view in each row of the RecyclerView

        String comment = this.data.get(position);
        viewHolder.textView.setText(comment);
    }

    @Override
    public int getItemCount() {
        // get the total number of comments

        return this.data.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /*
        The ViewHolder class stores and recycles views as they are clipped from view
         */

        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.recyclerViewItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Handles on click of the comment (nothing should happen)
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    String getItem(int id) {
        // Get the data at the corresponding click position

        return this.data.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        // Set the click listener for each item in the RecyclerView
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        // Interface for implementing the on click listener
        void onItemClick(View view, int position);
    }
}