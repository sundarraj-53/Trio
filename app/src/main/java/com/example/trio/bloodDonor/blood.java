package com.example.trio.bloodDonor;

public class blood{
    private String name;
    private String department;
    private String phoneno;
    private String profile;
    private String bloodgroup;

    public String getProfile() {

        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public blood(String name, String department, String phoneno, String profile, String bloodgroup) {
        this.name = name;
        this.department = department;
        this.phoneno = phoneno;
        this.profile = profile;
        this.bloodgroup = bloodgroup;
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

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }




}
