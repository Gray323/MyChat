package com.rxd.mychat.utils;

import com.anye.greendao.gen.AddNewFriendBeanDao;
import com.anye.greendao.gen.ContactItemDao;
import com.orhanobut.logger.Logger;
import com.rxd.mychat.db.AddNewFriendBean;
import com.rxd.mychat.db.AddNewFriendManager;
import com.rxd.mychat.db.ContactItem;
import com.rxd.mychat.db.ContactItemManager;
import com.rxd.mychat.xmpp.XmppUtils;

import org.jivesoftware.smack.util.stringencoder.Base64;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.util.ArrayList;
import java.util.List;

import static com.rxd.mychat.utils.Consts.USER_ACCOUNT;
import static com.rxd.mychat.utils.Consts.VCARD_GENDER;
import static com.rxd.mychat.utils.Consts.VCARD_SIGNATURE;

/**
 * Created by Administrator on 2017/8/2.
 * 数据库工具类
 */

public class DBUtils {

    /**
     * 获取当前用户的所有好友
     * @return
     */
    public static List<ContactItem> getContacts(){
        ContactItemManager manager = new ContactItemManager();
        List<ContactItem> contactItems  = new ArrayList<>();
        contactItems = manager.getAbstractDao().queryBuilder()
                .where(ContactItemDao.Properties.CurrentAccount.eq(SharePreferencesUtils.getShareStringData(USER_ACCOUNT)))
                .build().list();
        return contactItems;
    }

    /**
     * 获取指定好友的全部信息
     * @return
     */
    public static List<ContactItem> getContactItem(String account){
        ContactItemManager manager = new ContactItemManager();
        List<ContactItem> contactItems = new ArrayList<>();
        contactItems = manager.getAbstractDao().queryBuilder()
                .where(ContactItemDao.Properties.CurrentAccount.eq(SharePreferencesUtils.getShareStringData(USER_ACCOUNT)))
                .where(ContactItemDao.Properties.Account.eq(account))
                .build().list();
        return contactItems;
    }

    /**
     * 判断这个人之前是否添加过到数据库中，判断这个请求不是返回的添加好友请求
     * @param jid
     * @return
     */
    public static boolean getNewFriendsSend(String jid){
        AddNewFriendManager manager = new AddNewFriendManager();
        List<AddNewFriendBean>  addNewFriendBeanList = new ArrayList<>();
        addNewFriendBeanList = manager.getAbstractDao().queryBuilder()
                .where(AddNewFriendBeanDao.Properties.Currentname.eq(SharePreferencesUtils.getShareStringData(USER_ACCOUNT)))
                .where(AddNewFriendBeanDao.Properties.MyName.eq(jid))
                .build().list();
        if (addNewFriendBeanList.size() == 0){
            AddNewFriendBean bean = new AddNewFriendBean();
            bean.setCurrentname(SharePreferencesUtils.getShareStringData(USER_ACCOUNT));
            bean.setMyName(jid);
            bean.setUsername(SharePreferencesUtils.getShareStringData(USER_ACCOUNT));
            bean.setState(0);
            manager.insert(bean);
            return false;
        }else{
            return true;
        }
    }

    /**
     * 判断这个返回的同意好友请求是B发送的，而不是A发送的
     * @param jid
     * @return
     */
    public static List<AddNewFriendBean> getNewFriendAgree(String jid){
        AddNewFriendManager addNewFriendManager = new AddNewFriendManager();
        List<AddNewFriendBean> beanList = new ArrayList<>();
        beanList = addNewFriendManager.getAbstractDao().queryBuilder()
                .where(AddNewFriendBeanDao.Properties.Currentname.eq(SharePreferencesUtils.getShareStringData(USER_ACCOUNT)))
                .where(AddNewFriendBeanDao.Properties.Username.eq(jid))
                .build().list();
        return beanList;
    }

    /**
     * 判断这个好友是否存在
     * 将这个好友添加到数据库中去
     * @param jid
     */
    public static void insertNewContact(String jid){

        ContactItemManager manager = new ContactItemManager();
        List<ContactItem> items = new ArrayList<>();
        items = manager.getAbstractDao().queryBuilder()
                        .where(ContactItemDao.Properties.Account.eq(jid))
                        .where(ContactItemDao.Properties.CurrentAccount.eq(SharePreferencesUtils.getShareStringData(USER_ACCOUNT)))
                        .build().list();
        boolean isExist = false;
        //TODO 判断这个好友是否存在过
        Logger.d(items.size());
        for (int i = 0; i < items.size(); i++){
            Logger.d(items.get(i).getAccount());
            if (items.get(i).getAccount().equals(jid)){
                isExist = true;
            }
        }
        if (!isExist){
            VCard vCard = XmppUtils.getUserVCard(jid);
            Logger.d("vcard:" + vCard);

            ContactItem contactItem = new ContactItem();
            contactItem.setNickname(vCard.getNickName());
            if (vCard.getAvatar() != null){
                contactItem.setAvatar(Base64.encodeToString(vCard.getAvatar()));
            }
            contactItem.setCurrentAccount(SharePreferencesUtils.getShareStringData(USER_ACCOUNT));
            contactItem.setAccount(jid);
            contactItem.setEmail(vCard.getEmailHome());
            contactItem.setGender(vCard.getField(VCARD_GENDER));
            contactItem.setReg(vCard.getOrganization());
            contactItem.setSignature(vCard.getField(VCARD_SIGNATURE));
            manager.insert(contactItem);
        }
    }


}
