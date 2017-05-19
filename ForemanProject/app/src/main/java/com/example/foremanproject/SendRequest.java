package com.example.foremanproject;

/**
 * Created by labuser on 5/17/2017.
 */

class SendRequest {
    private static String url;
    private static String username;
    private static String password;

    static void setUrl(String newUrl) {
        url = newUrl;
        if(!url.substring(0,4).equals("https"))
            url = "https://" + url;
        if(!url.substring(url.length()-1).equals("/"))
            url = url + "/";
    }

    public static void setUsername(String newUsername) { username = newUsername; }

    public static void setPassword(String newPassword) { password = newPassword; }

    public static String getUrl(){ return url;}

    public static String getUNandPW(){ return (username + ":" + password);}

}