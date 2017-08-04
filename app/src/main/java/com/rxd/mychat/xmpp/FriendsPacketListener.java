package com.rxd.mychat.xmpp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.RemoteViews;

import com.orhanobut.logger.Logger;
import com.rxd.mychat.R;
import com.rxd.mychat.db.AddNewFriendBean;
import com.rxd.mychat.ui.activity.NewFriendListActivity;
import com.rxd.mychat.utils.DBUtils;
import com.rxd.mychat.utils.Utils;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.util.List;

/**
 * Created by Administrator on 2017/6/8.
 */

public class FriendsPacketListener implements StanzaListener{

    private SmackService service;

    public FriendsPacketListener(SmackService smackService) {
        this.service = smackService;
    }


    @Override
    public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException {
        Log.d("sc","监听到了");
        if (packet.getFrom() == packet.getTo()){
            //TODO
            return;
        }
        if (packet instanceof Presence){
            Presence presence = (Presence) packet;


            //收到好友申请，如果是别人向我申请，则添加到数据库中
            //如果是别人向我返回的好友申请，则自动发送一个同意包
            if (presence.getType().equals(Presence.Type.subscribe)){ //有好友申请
                Logger.d("有好友申请");
                //获取到FromJid
                String jid = presence.getFrom().asEntityBareJidOrThrow().toString().trim().substring(0,
                        presence.getFrom().asEntityBareJidOrThrow().toString().trim().lastIndexOf("@"));

                Logger.d("JID: " + jid);

                //判断这个人之前是否添加过或者是不是返回的好友请求
                boolean isExist = DBUtils.getNewFriendsSend(jid);
                //都不是则发送通知栏请求
                if (!isExist){
                    VCard vCard = new VCard();
                    vCard = XmppUtils.getUserVCard(jid);
                    createNotification(vCard.getNickName(), vCard.getAvatar());
                }

            }else if (presence.getType().equals(Presence.Type.subscribed)){//同意申请,把好友添加到数据中去
                Logger.d("同意申请");
                //获取到FromJid
                String jid = presence.getFrom().asEntityBareJidOrThrow().toString().trim().substring(0,
                        presence.getFrom().asEntityBareJidOrThrow().toString().trim().lastIndexOf("@"));

                Logger.d("JID: " + jid);

                List<AddNewFriendBean> beanList = DBUtils.getNewFriendAgree(jid);

                if (beanList.size() != 0){
                    XmppUtils.agreeFriendFinal(jid);
                }

                DBUtils.insertNewContact(jid);

            }else if (presence.getType().equals(Presence.Type.unsubscribe)){//请求删除好友
                Log.d("sc","删除好友");
            }else if (presence.getType().equals(Presence.Type.unsubscribed)){//删除好友
                Log.d("sc","删除好友");
            }else if (presence.getType().equals(Presence.Type.unavailable)) {//好友下线   要更新好友列表，可以在这收到包后，发广播到指定页面   更新列表
                Log.d("sc","好友下线");
            }else if(presence.getType().equals(Presence.Type.available)){//好友上线
                Log.d("sc","好友上线");
            }
        }
    }

    private void createNotification(String nickname, byte[] avatar){
        //TODO 创建notification
        NotificationManager manager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
        long when = System.currentTimeMillis();
        Notification notification = new Notification(R.mipmap.ic_launcher, "好友申请", when);
        Intent intent = new Intent(service, NewFriendListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(service, 0, intent, 0);
        notification.contentIntent = pendingIntent;

        RemoteViews rv = new RemoteViews(service.getPackageName(), R.layout.notification_add_new_friend);
        rv.setTextViewText(R.id.nf_new_friend_nickname, nickname);
        rv.setImageViewBitmap(R.id.nf_new_friend_avatar, Utils.getPicFromBytes(avatar, new BitmapFactory.Options()));
        notification.contentView = rv;
        manager.notify(0, notification);
    }
}
