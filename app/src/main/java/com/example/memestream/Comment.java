package com.example.memestream;

import java.io.Serializable;

class Comment implements Serializable {

    private String username;
    private String comment;

    public Comment(String username, String comment) {
        this.username = username;
        this.comment = comment;
    }

    public String getUsername() { return this.username; }
    public String getComment() { return this.comment; }


}