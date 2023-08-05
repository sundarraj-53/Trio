package com.example.trio;

public class comment {
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;
    public comment(String comment, String username) {
        this.comment=comment;
        this.username=username;
    }
}
