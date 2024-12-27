package com.netlink.newsapplication.models;



import java.util.Map;

public class UserModel {
    private String email;
    private Map<String, String> feveroite;
    private String mobile;
    private String name;
    private String password;

    public UserModel() {
        // Default constructor required for calls to DataSnapshot.getValue(UserModel.class)
    }

    public UserModel(String email, Map<String, String> feveroite, String mobile, String name, String password) {
        this.email = email;
        this.feveroite = feveroite;
        this.mobile = mobile;
        this.name = name;
        this.password = password;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, String> getFeveroite() {
        return feveroite;
    }

    public void setFeveroite(Map<String, String> feveroite) {
        this.feveroite = feveroite;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
