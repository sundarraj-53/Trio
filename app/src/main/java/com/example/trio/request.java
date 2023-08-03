package com.example.trio;

public class request {
    public String name,department;
    public String dept_id;
    public int id;

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String club;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public request(String name, String department, String dept_id,int id,String club) {
        this.name = name;
        this.department = department;
        this.dept_id = dept_id;
        this.id=id;
        this.club=club;
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

    public String getDept_id() {

        return dept_id;
    }

    public void setDept_id(String dept_id) {

        this.dept_id = dept_id;
    }




}
