package com.rxd.mychat.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/7/24.
 */

@Entity
public class AddNewFriendBean {
    @Id(autoincrement = true)
    private Long id;
    //用户的头像
    @Property(nameInDb = "currentname")
    private String currentname;
    //from
    @Property(nameInDb = "fromname")
    private String myName;
    @Property(nameInDb = "fromavatar")
    private String fromAvatar;
    //to
    @Property(nameInDb = "torname")
    private String username;
    @Property(nameInDb = "toavatar")
    private String toAvatar;
    //当前是否添加了这个好友,0表示未添加，1表示已添加
    @Property(nameInDb = "state")
    private int state;
    @Generated(hash = 2015806174)
    public AddNewFriendBean(Long id, String currentname, String myName,
            String fromAvatar, String username, String toAvatar, int state) {
        this.id = id;
        this.currentname = currentname;
        this.myName = myName;
        this.fromAvatar = fromAvatar;
        this.username = username;
        this.toAvatar = toAvatar;
        this.state = state;
    }
    @Generated(hash = 153415137)
    public AddNewFriendBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCurrentname() {
        return this.currentname;
    }
    public void setCurrentname(String currentname) {
        this.currentname = currentname;
    }
    public String getMyName() {
        return this.myName;
    }
    public void setMyName(String myName) {
        this.myName = myName;
    }
    public String getFromAvatar() {
        return this.fromAvatar;
    }
    public void setFromAvatar(String fromAvatar) {
        this.fromAvatar = fromAvatar;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getToAvatar() {
        return this.toAvatar;
    }
    public void setToAvatar(String toAvatar) {
        this.toAvatar = toAvatar;
    }
    public int getState() {
        return this.state;
    }
    public void setState(int state) {
        this.state = state;
    }

   
}
