package com.rxd.mychat.xmpp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rxd.mychat.app.MyChatApp;
import com.rxd.mychat.utils.Consts;
import com.rxd.mychat.utils.SharePreferencesUtils;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.io.IOException;

/**
 * Created by Administrator on 2017/6/6.
 */

public class SmackService extends Service{
    private static SmackService mInstance = null;

    private final IBinder binder = new MyBinder();
    private XMPPTCPConnection connection;
    //XMPP连接监听器
    private CheckConnectionListener listener;
    //好友状态监听
    private FriendsPacketListener friendsPacketListener;

    //账户名和密码
    private String userPhone;
    private String password;



    public static SmackService getInstance() {
        return mInstance;
    }

    public class MyBinder extends Binder {
        public SmackService getService() {
            return SmackService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Log.d("sc","服务启动了");
        initXMPPTask();
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void initXMPPTask() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                initXMPP();
            }
        }).start();

    }

    private void initXMPP() {
        XmppConnectionManager manager = XmppConnectionManager.getInstance();
        connection = manager.init();
        Log.d("sc","connection:"+(connection == null));
        MyChatApp.xmpptcpConnection = connection;
        Log.d("sc","isConnected():"+connection.isConnected());
        login();
    }

    private void login(){
        try{
            Log.d("sc","isConnected?:"+connection.isConnected());
            XMPPTCPConnection conn = null;
            if (MyChatApp.xmpptcpConnection == null){
                XmppConnectionManager manager = XmppConnectionManager.getInstance();
                conn = manager.init();
            }else{
                conn = MyChatApp.xmpptcpConnection;
            }
            if (!conn.isConnected()){
                conn.connect();
            }
            Log.d("sc","isConnected?:"+connection.isConnected());
            userPhone = SharePreferencesUtils.getShareStringData(Consts.USER_ACCOUNT);
            password = SharePreferencesUtils.getShareStringData(Consts.USER_PASSWORD);
            conn.login(userPhone, password);
            if (conn.isAuthenticated()){
                Log.d("sc","登录了");
                MyChatApp.xmpptcpConnection = conn;
                //添加XMPP连接监听
                sendLoginBroadcast(true);
                listener = new CheckConnectionListener();
                //添加好友状态监听
                friendsPacketListener = new FriendsPacketListener(this);
                conn.addConnectionListener(listener);
                //条件过滤器
                AndFilter filter = new AndFilter();
                conn.addAsyncStanzaListener(friendsPacketListener, filter);
            }
        } catch (XMPPException e) {
            e.printStackTrace();
            sendLoginBroadcast(false);
        } catch (IOException e) {
            e.printStackTrace();
            sendLoginBroadcast(false);
        } catch (SmackException e) {
            e.printStackTrace();
            sendLoginBroadcast(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
            sendLoginBroadcast(false);
        }

    }

    /**
     * 发送登录状态广播
     */
    private void sendLoginBroadcast(boolean isLoginSuccess){
        //TODO 发送广播
        Intent intent = new Intent(Consts.LOGIN_BROADCAST);
        intent.putExtra(Consts.LOGIN_EXTRA, isLoginSuccess);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        if (connection !=null){
            connection.disconnect();
            connection = null;
        }
        Log.d("sc","服务停止了");
        super.onDestroy();
    }
}
