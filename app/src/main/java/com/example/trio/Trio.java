package com.example.trio;

import android.util.Log;

import org.json.JSONArray;

public class Trio {
    private String postImage;
    private String tag;
    private String captions;

    public Trio(JSONArray array) {
        comment=array;

    }

    public String getClubimage() {
        return clubimage;
    }

    public void setClubimage(String clubimage) {
        this.clubimage = clubimage;
    }

    private String  clubimage;

    public JSONArray getComment() {
        Log.d("JOHN",comment.toString());
        return comment;
    }

    public void setComment(JSONArray comment) {
        Log.d("JOHN",comment.toString());
        this.comment = comment;
    }

    private JSONArray comment;

//    public Trio(String format,String tag, String captions, String clubName, int like, String text) {
//        this.format=format;
//        this.tag = tag;
//        this.captions = captions;
//        this.clubName = clubName;
//        this.like = like;
//
//    }
//     public void addComment(String newComment) {
//        commentArray.put(newComment);
//    }
    private String clubName;
    private String like;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    private String  format;
    private boolean isLiked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    public Trio(String id, String format, String image, String tag, String caption, String clubName, String like, String text, JSONArray comment, String clubimage) {
        this.format=format;
        this.text=text;
        this.postImage = image;
        this.tag = tag;
        this.captions = caption;
        this.clubName = clubName;
        this.like = like;
        this.isLiked = false;
        this.id=id;
        this.comment=comment;
        this.clubimage=clubimage;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }






//    public Trio(String profileIcon, String postImage, String captions, String clubName, String like) {
//        this.profileIcon = profileIcon;
//        this.postImage = postImage;
//        this.captions = captions;
//        this.clubName = clubName;
//        this.like = like;
//    }


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
    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
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
