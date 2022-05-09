package com.example.memestream;

import java.io.Serializable;

class Like implements Serializable {

    private String username;

    public Like(String username) {
        this.username = username;
    }

    public String getUsername() { return this.username; }
}
