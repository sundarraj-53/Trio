package com.example.trio;

import android.content.Context;

import java.util.ArrayList;

public class AdminHolder {
    private Context context;
    private String userId;
    private String name;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public ArrayList<Integer> getClubId() {
        return clubId;
    }

    public void setClubId(ArrayList<Integer> clubId) {
        this.clubId = clubId;
    }

    public ArrayList<String> getClubName() {
        return clubName;
    }

    public void setClubName(ArrayList<String> clubName) {
        this.clubName = clubName;
    }

    private String department;
    private ArrayList<Integer> clubId;
    private ArrayList<String> clubName;

    public AdminHolder(Context context, String userId, String name, String department, ArrayList<Integer> clubId, ArrayList<String> clubName) {
        this.context = context;
        this.userId = userId;
        this.name = name;
        this.department = department;
        this.clubId = clubId;
        this.clubName = clubName;
    }



}
