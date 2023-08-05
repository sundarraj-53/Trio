package com.example.trio.Storage;

import java.util.ArrayList;

public class Storage
{
    private static final String PREF_NODE_NAME = "com.example.myapp";
    public static String KEY_USERNAME = "username";
    public static int role=0;
    public static ArrayList<String> arrayList=new ArrayList<>();
    public static ArrayList<Integer> club_name=new ArrayList<>();
    public static ArrayList<String> member_club=new ArrayList<>();
    public static ArrayList<Integer> club_no=new ArrayList<>();

    private static String user_id="user_id";
    private static String username=" ";

    public void saveName(String user){
        username=user;
    }
    public String getName(){
        return username;
    }

    public ArrayList<Integer> getnewList(){
        return  club_name;
    }

    public ArrayList<String> getArrayList(){

        return arrayList;
    }
    public static void saveUsername(String username) {

        KEY_USERNAME=username;
    }
    public static String getKeyUsername(){

        return KEY_USERNAME;
    }
    public static void deleteUsername(){
        arrayList.clear();
        club_name.clear();
        member_club.clear();
        club_no.clear();
        user_id="user_id";
        username=" ";
        KEY_USERNAME=" ";
    }
    public void saveRole(int role){
        this.role=role;
    }
    public int getRole(){
        return role;
    }
    public static void saveId(String user){
        user_id=user;
    }
    public static String getUser_id(){

        return user_id;
    }

    public ArrayList<String> getMember(){

        return member_club;
    }

}
