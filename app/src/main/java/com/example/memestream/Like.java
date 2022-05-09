package com.example.memestream;

import java.io.Serializable;

class Like implements Serializable {
    /*
    Utility class for housing the likes of posts.
     */

    private String username;

    public Like(String username) {
        this.username = username;
    }

    public String getUsername() { return this.username; }
}
