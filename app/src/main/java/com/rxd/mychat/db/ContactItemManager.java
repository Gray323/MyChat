package com.rxd.mychat.db;

import org.greenrobot.greendao.AbstractDao;

/**
 * Created by sc on 2017/5/31.
 */

public class ContactItemManager extends BaseManager<ContactItem,Long> {
    @Override
    public AbstractDao<ContactItem, Long> getAbstractDao() {
        return daoSession.getContactItemDao();
    }
}
