package com.example.trio;

public class club {
    public int id;
    public String clubName;

    public club() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }


    public club(String clubName, int id) {
        this.id=id;
        this.clubName=clubName;
    }
}
