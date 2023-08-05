package com.example.trio;

public class commitee {
    public int id;
    public String clubName;

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



    public commitee(int id, String clubName) {
        this.id = id;
        this.clubName = clubName;
    }


}
