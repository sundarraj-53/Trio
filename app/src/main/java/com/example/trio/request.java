package com.example.trio;

import android.content.Context;

public class request {
    public String name,department;
    public String dept_id;

    public request(Context context, String name, String department, String dept_id) {
        this.name = name;
        this.department = department;
        this.dept_id = dept_id;
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
