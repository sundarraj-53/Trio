package com.example.trio.Storage;

public class Storage
{
    private static final String PREF_NODE_NAME = "com.example.myapp";
    public static String KEY_USERNAME = "username";

    private static String user_id="user_id";

    public static void saveUsername(String username) {

        KEY_USERNAME=username;
    }
    public static String getKeyUsername(){

        return KEY_USERNAME;
    }
    public static void deleteUsername(){

        KEY_USERNAME=" ";
    }
    public static void saveId(String user){
        user_id=user;
    }
    public static void deleteId(){
        user_id="user_id";
    }
    public static String getUser_id(){
        return user_id;
    }

}
