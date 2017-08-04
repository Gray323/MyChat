package com.rxd.mychat.db;

import org.greenrobot.greendao.AbstractDao;

/**
 * Created by sc on 2017/5/31.
 */

public class AddNewFriendManager extends BaseManager<AddNewFriendBean,Long> {
    @Override
    public AbstractDao<AddNewFriendBean, Long> getAbstractDao() {
        return daoSession.getAddNewFriendBeanDao();
    }
}
