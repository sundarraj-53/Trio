package com.example.trio.Storage;

public class Storage
{
    private static final String PREF_NODE_NAME = "com.example.myapp";
    public static String KEY_USERNAME = "username";
    private static final String KEY_THEME = "theme";

    public static void saveUsername(String username) {
        KEY_USERNAME=username;
    }
    public static String getKeyUsername(){
        return KEY_USERNAME;
    }
}
