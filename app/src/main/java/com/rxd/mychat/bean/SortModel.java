package com.rxd.mychat.bean;

/**
 * Created by Administrator on 2017/7/10.
 */

public class SortModel {
    private String name;
    private String sortLetters;
    private String bmAvatar;
    private String nickname;


    public String getBmAvatar() {
        return bmAvatar;
    }

    public void setBmAvatar(String bmAvatar) {
        this.bmAvatar = bmAvatar;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSortLetters() {
        return sortLetters;
    }
    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
