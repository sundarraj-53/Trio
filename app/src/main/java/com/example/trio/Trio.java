package com.example.trio;

public class Trio {
    private String profileIcon;
    private String postImage;
    private String captions;
    private String clubName;
    private String like;

    public Trio(String profileIcon, String postImage, String captions, String clubName, String like) {
        this.profileIcon = profileIcon;
        this.postImage = postImage;
        this.captions = captions;
        this.clubName = clubName;
        this.like = like;
    }

    public String getProfileIcon() {
        return profileIcon;
    }

    public void setProfileIcon(String profileIcon) {
        this.profileIcon = profileIcon;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getCaptions() {
        return captions;
    }

    public void setCaptions(String captions) {
        this.captions = captions;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }
}
