package com.example.whereruapp;

import com.google.gson.annotations.SerializedName;

/**
 * The returned login info from the login method in GetDataService
 */
public class RetroLogin {
    /**
     * the returned username from the login method in GetDataService
     */
    @SerializedName("username")
    public String username = "";
}
