package com.rxd.mychat.bean;

/**
 * Created by Administrator on 2017/7/13.
 */

public class UserInfo{

    private String username;
    private String name;
    private String email;
    private byte[] avatar;
    public UserInfo(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }
}
