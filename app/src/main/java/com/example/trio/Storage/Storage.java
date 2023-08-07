package com.example.trio.Storage;

import java.util.ArrayList;

public class Storage
{

    public static ArrayList<String> clubNames=new ArrayList<>();
    public static ArrayList<Integer> clubIDNames=new ArrayList<>();
    public static String MainAdmin;

    public void clubAdd(){
        clubNames.add("Select Your club");
        clubNames.add("NSS");
        clubNames.add("NCC");
        clubNames.add("YRC");
        clubNames.add("Yoga");
        clubNames.add("Linux");
        clubNames.add("Fine Arts");
        clubNames.add("Eco");
    }
    public void clubIdAdd(){
        clubIDNames.add(0);
        clubIDNames.add(1);
        clubIDNames.add(2);
        clubIDNames.add(3);
        clubIDNames.add(4);
        clubIDNames.add(5);
        clubIDNames.add(6);
        clubIDNames.add(7);
    }
    public String getAdmin(){
        return MainAdmin;
    }

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
        clubNames.clear();
        clubIDNames.clear();
        club_name.clear();
        member_club.clear();
        club_no.clear();
        MainAdmin=" ";
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
    public ArrayList<String> getHomeFilter(){
        return clubNames;
    }

}
