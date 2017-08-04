package com.rxd.mychat.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Gray on 2017/8/2.
 * 将好友信息保存到本地
 */

@Entity
public class ContactItem {
    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "current_account")
    private String currentAccount;
    @Property(nameInDb = "contact_avatar")
    private String avatar;
    @Property(nameInDb = "contact_nickname")
    private String nickname;
    @Property(nameInDb = "contact_account")
    private String account;
    @Property(nameInDb = "contact_gender")
    private String gender;
    @Property(nameInDb = "contact_email")
    private String email;
    @Property(nameInDb = "contact_reg")
    private String reg;
    @Property(nameInDb = "contact_signature")
    private String signature;
    @Generated(hash = 1153849639)
    public ContactItem(Long id, String currentAccount, String avatar,
            String nickname, String account, String gender, String email,
            String reg, String signature) {
        this.id = id;
        this.currentAccount = currentAccount;
        this.avatar = avatar;
        this.nickname = nickname;
        this.account = account;
        this.gender = gender;
        this.email = email;
        this.reg = reg;
        this.signature = signature;
    }
    @Generated(hash = 175717015)
    public ContactItem() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCurrentAccount() {
        return this.currentAccount;
    }
    public void setCurrentAccount(String currentAccount) {
        this.currentAccount = currentAccount;
    }
    public String getAvatar() {
        return this.avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getAccount() {
        return this.account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getGender() {
        return this.gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getReg() {
        return this.reg;
    }
    public void setReg(String reg) {
        this.reg = reg;
    }
    public String getSignature() {
        return this.signature;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }

}
