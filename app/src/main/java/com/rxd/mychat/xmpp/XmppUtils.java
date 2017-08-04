package com.rxd.mychat.xmpp;

import android.graphics.Bitmap;
import android.util.Log;

import com.anye.greendao.gen.AddNewFriendBeanDao;
import com.orhanobut.logger.Logger;
import com.rxd.mychat.app.MyChatApp;
import com.rxd.mychat.bean.UserInfo;
import com.rxd.mychat.db.AddNewFriendBean;
import com.rxd.mychat.db.AddNewFriendManager;
import com.rxd.mychat.db.ContactItem;
import com.rxd.mychat.db.ContactItemManager;
import com.rxd.mychat.utils.SharePreferencesUtils;
import com.rxd.mychat.utils.Utils;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.packet.RosterPacket;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.util.stringencoder.Base64;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jivesoftware.smackx.xdata.Form;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.rxd.mychat.utils.Consts.USER_ACCOUNT;
import static com.rxd.mychat.utils.Consts.VCARD_GENDER;
import static com.rxd.mychat.utils.Consts.VCARD_SIGNATURE;

/**
 * Created by sc on 2017/6/6.
 */

public class XmppUtils {

    /**
     * 注册
     *
     * @param attributes
     * @param account
     * @param password
     * @return 1、注册成功 0、服务器没有返回结果  2、这个账号已经存在 3、注册失败
     */
    public static int register(Map<String, String> attributes, String account, String password, XMPPTCPConnection connection) {
        try {
            Localpart localpart = Localpart.from(account);
            AccountManager accountManager = AccountManager.getInstance(connection);
            //必须加
            accountManager.sensitiveOperationOverInsecureConnection(true);
            if (attributes.size() == 0)
               accountManager.createAccount(localpart, password);
            else {
                accountManager.createAccount(localpart, password, attributes);
            }
            return 1;
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
            return 0;
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
            if (e.getXMPPError().getType() == XMPPError.Type.CANCEL) {
                return 2;
            } else {
                return 3;
            }
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            return 3;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 4;
        } catch (XmppStringprepException e) {
            e.printStackTrace();
            return 5;
        }
    }


    /**
     * 获取账户的额外信息
     0-->name
     1-->email
     2-->password
     3-->username
     4-->registered
     */
    public static ArrayList<String> getAccountAttributes(){
        XMPPTCPConnection conn = reconnect();
        ArrayList<String> list = new ArrayList<>();
        try {
            Set<String>  set =  AccountManager.getInstance(conn).getAccountAttributes();

            int i = 0;
            for(Iterator it = set.iterator(); it.hasNext();i++)
            {
                String s = (String) it.next();
                Log.d("sc",i+"-->"+s);
                list.add(s);
            }
            return list;
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取当前连接人的信息
     * @return
     */
    public static VCard getUserVCard(){
        VCard vCard = new VCard();
        try {
            XMPPTCPConnection connection = reconnect();
            vCard = VCardManager.getInstanceFor(connection).loadVCard();
            return vCard;
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
            return vCard;
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
            return vCard;
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            return vCard;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return vCard;
        }
    }

    /**
     * 获取指定人的信息
     * @param jid
     * @return
     */
    public static VCard getUserVCard(String jid){
        VCard vCard = new VCard();
        try {
            Logger.d("jid --------------> " + jid);
            if (jid != null){
                Localpart localpart = Localpart.from(jid);
                DomainBareJid serviceName = JidCreate.domainBareFrom("shengcheng");
                EntityBareJid entityBareJid = JidCreate.entityBareFrom(localpart, serviceName);
                XMPPTCPConnection connection = reconnect();
                vCard = VCardManager.getInstanceFor(connection).loadVCard(entityBareJid);
            }

            return vCard;
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
            return vCard;
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
            return vCard;
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            return vCard;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return vCard;
        } catch (XmppStringprepException e) {
            e.printStackTrace();
            return vCard;
        }
    }

    /**
     * 设置昵称
     * @param nickName
     */
    public static void setNickName(String nickName){
        try {
            XMPPTCPConnection conn = reconnect();
            VCard vCard = VCardManager.getInstanceFor(conn).loadVCard();
            vCard.setNickName(nickName);
            VCardManager.getInstanceFor(conn).saveVCard(vCard);
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置地区
     * @param organzation
     */
    public static void setOrganzation(String organzation){
        try {
            XMPPTCPConnection conn = reconnect();
            VCard vCard = VCardManager.getInstanceFor(conn).loadVCard();
            vCard.setOrganization(organzation);
            VCardManager.getInstanceFor(conn).saveVCard(vCard);
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置个性签名
     * @param signnature
     */
    public static void setSignnature(String signnature){
        try {
            XMPPTCPConnection conn = reconnect();
            VCard vCard = VCardManager.getInstanceFor(conn).loadVCard();
            vCard.setField(VCARD_SIGNATURE, signnature);
            VCardManager.getInstanceFor(conn).saveVCard(vCard);
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置性别
     * @param gender
     */
    public static void setGender(String gender){
        try {
            XMPPTCPConnection conn = reconnect();
            VCard vCard = VCardManager.getInstanceFor(conn).loadVCard();
            vCard.setField(VCARD_GENDER, gender);
            VCardManager.getInstanceFor(conn).saveVCard(vCard);
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置头像
     * @param bitmap
     */
    public static void setAvatar(Bitmap bitmap){
        try {
            byte[] avatarByte = Utils.Bitmap2Bytes(bitmap);
            XMPPTCPConnection conn = reconnect();
            VCard vCard = VCardManager.getInstanceFor(conn).loadVCard();
            vCard.setAvatar(avatarByte,"image/jpeg");
            VCardManager.getInstanceFor(conn).saveVCard(vCard);
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置邮箱
     * @param email
     */
    public static void setEmail(String email){
        try {
            XMPPTCPConnection conn = reconnect();
            VCard vCard = VCardManager.getInstanceFor(conn).loadVCard();
            vCard.setEmailHome(email);
            VCardManager.getInstanceFor(conn).saveVCard(vCard);
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回好友列表(both关系)
     * @return
     */
    public static ArrayList<ContactItem> findFriends(){
        ContactItemManager manager = new ContactItemManager();
        Set<RosterEntry> set = new HashSet<>();
        ArrayList<ContactItem> list = new ArrayList<>();
        XMPPTCPConnection conn = reconnect();
        set =  Roster.getInstanceFor(conn).getEntries();
        Iterator<RosterEntry> it = set.iterator();

        VCard vCard;
        ContactItem contactItem;
        while(it.hasNext()){
            RosterEntry entry = it.next();
            String name = entry.getJid().toString().trim().substring(0, entry.getJid().toString().trim().lastIndexOf("@"));
            Logger.d("entry.name" + name);
            //判断双方是否是both的关系
            if (RosterPacket.ItemType.both == entry.getType()){
                Logger.d("name" + name);
                vCard = XmppUtils.getUserVCard(name);
                /*sortModel.setName(name);
                if (vCard.getAvatar() != null){
                    sortModel.setBmAvatar(Base64.encodeToString(vCard.getAvatar()));
                }

                sortModel.setNickname(vCard.getNickName());*/

                Log.d("sc",name + "--- type:" + entry.getType());
                contactItem = new ContactItem();
                contactItem.setNickname(vCard.getNickName());
                if (vCard.getAvatar() != null){
                    contactItem.setAvatar(Base64.encodeToString(vCard.getAvatar()));
                }
                contactItem.setCurrentAccount(SharePreferencesUtils.getShareStringData(USER_ACCOUNT));
                contactItem.setAccount(name);
                contactItem.setEmail(vCard.getEmailHome());
                contactItem.setGender(vCard.getField(VCARD_GENDER));
                contactItem.setReg(vCard.getOrganization());
                contactItem.setSignature(vCard.getField(VCARD_SIGNATURE));
                list.add(contactItem);
                manager.insert(contactItem);
            }
        }
        return  list;
    }

    /**
     * 返回好友列表(非both关系)
     * @return
     */
    public static ArrayList<String> findFriendsNone(){
        Set<RosterEntry> set = new HashSet<>();
        ArrayList<String> list = new ArrayList<>();
        XMPPTCPConnection conn = reconnect();
        set =  Roster.getInstanceFor(conn).getEntries();
        Iterator<RosterEntry> it = set.iterator();
        while(it.hasNext()){
            RosterEntry entry = it.next();
            String name = entry.getName();
            BareJid jid = entry.getJid();
            //判断双方是否是both的关系
            if (!(RosterPacket.ItemType.both == entry.getType())){
                list.add(name);
                Log.d("sc",name + "--- type:" + entry.getType());
            }
        }
        return  list;
    }

    /**
     * 搜索指定用户的用户名
     * @param username
     * @return
     */
    public static List<UserInfo> searchFriends(String username){
        try {
            List<UserInfo> nameList = new ArrayList<>();
            XMPPTCPConnection conn = reconnect();
            UserSearchManager manager = new UserSearchManager(conn);
            DomainBareJid domainBareJid = JidCreate.domainBareFrom("search."+conn.getServiceName());
            Form searchForm = manager.getSearchForm(domainBareJid);
            Form answerForm = searchForm.createAnswerForm();
            answerForm.setAnswer("Username",true);
            answerForm.setAnswer("search",username);
            ReportedData data = manager.getSearchResults(answerForm,domainBareJid);
            List<ReportedData.Row> list = data.getRows();
            ReportedData.Row row = null;
            UserInfo userInfo = null;
            VCard vCard = null;
            String uname = "";
            for (int i = 0; i < list.size(); i++){
                row = list.get(i);
                uname = row.getValues("Username").toString();
                if (!SharePreferencesUtils.getShareStringData(USER_ACCOUNT).equals(uname.substring(1,uname.length() - 1))){
                    userInfo = new UserInfo();
                    vCard = XmppUtils.getUserVCard(uname.substring(1,uname.length() - 1));
                    userInfo.setUsername(uname);
                    userInfo.setName(row.getValues("Name").toString());
                    userInfo.setEmail(row.getValues("Email").toString());
                    userInfo.setAvatar(vCard.getAvatar());
                    nameList.add(userInfo);
                }
            }
            return  nameList;

        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    /**
     * 添加好友到指定分组
     * A发送的是请求包
     * @param username
     */
    public static void addFriend(String username){
        try {

            //保存添加好友请求到数据库中
            AddNewFriendManager manager = new AddNewFriendManager();
            AddNewFriendBean bean = new AddNewFriendBean();
            bean.setCurrentname(SharePreferencesUtils.getShareStringData(USER_ACCOUNT));
            bean.setMyName(SharePreferencesUtils.getShareStringData(USER_ACCOUNT));
            bean.setUsername(username);
            bean.setState(0);
            boolean is = manager.insert(bean);
            Log.d("sc","是否插入:" + is);

            XMPPTCPConnection conn = reconnect();
            Presence presence = new Presence(Presence.Type.subscribe);
            BareJid bareJid = JidCreate.bareFrom(username+"@"+conn.getServiceName());
            presence.setTo(bareJid);
            //发送申请包
            conn.sendStanza(presence);
            //Roster.getInstanceFor(conn).createEntry(bareJid,username,new String[]{"我的好友"});

        }  catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }

    }

    /**
     * 同意添加好友
     * B发送的是同意包，同时发送请求包
     * @param username
     */
    public static boolean agreeFriend(String username){
        try {
            XMPPTCPConnection conn = reconnect();
            BareJid jid = JidCreate.bareFrom(username+"@"+conn.getServiceName());

            Presence presence = new Presence(Presence.Type.subscribed);
            presence.setTo(jid);
            //发送同意包
            conn.sendStanza(presence);

            //发送请求包
            Presence presence1 = new Presence(Presence.Type.subscribe);
            presence.setTo(jid);
            conn.sendStanza(presence1);

            //TODO 修改状态
            AddNewFriendManager manager = new AddNewFriendManager();
            List<AddNewFriendBean> addNewFriendBeanList = new ArrayList<>();
            AddNewFriendBean newFriendBean = new AddNewFriendBean();
            newFriendBean = manager.getAbstractDao().queryBuilder()
                    .where(AddNewFriendBeanDao.Properties.Currentname.eq(SharePreferencesUtils.getShareStringData(USER_ACCOUNT)))
                    .where(AddNewFriendBeanDao.Properties.MyName.eq(username))
                    .where(AddNewFriendBeanDao.Properties.Username.eq(SharePreferencesUtils.getShareStringData(USER_ACCOUNT)))
                    .build().unique();
            newFriendBean.setState(1);
            manager.update(newFriendBean);
            return true;
        } catch (XmppStringprepException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void agreeFriendFinal(String username){
        try {
            XMPPTCPConnection conn = reconnect();
            BareJid jid = JidCreate.bareFrom(username+"@"+conn.getServiceName());
            Presence presence = new Presence(Presence.Type.subscribed);
            presence.setTo(jid);
            //发送同意包
            conn.sendStanza(presence);
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }


    }


  /*  *//**
     * 发送文本消息
     * @param Jid
     * @param message
     *//*
    public static void sendTextMessage(String Jid, String message){
        try {
            XMPPTCPConnection conn = reconnect();
            ChatManager chatManager = ChatManager.getInstanceFor(conn);
            Chat chat  = chatManager.createChat(Jid);
            if (chat != null) {
                chat.sendMessage(message);
            }
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/


    /**
     * 重新连接
     * @return
     */
    private static XMPPTCPConnection reconnect(){
        XMPPTCPConnection conn = MyChatApp.xmpptcpConnection;
        if (!conn.isConnected()){
            try {
                conn.connect();
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XMPPException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

}
