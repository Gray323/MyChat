package com.rxd.mychat.xmpp;

import android.util.Log;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

/**
 * Created by Administrator on 2017/6/7.
 */

public class CheckConnectionListener implements ConnectionListener {

    private SmackService context;

    /*public CheckConnectionListener(SmackService context) {
        this.context = context;
    }*/

    public CheckConnectionListener(){

    }

    @Override
    public void connected(XMPPConnection connection) {

    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {

    }

    @Override
    public void connectionClosed() {
        Log.d("sc","连接断了");
    }

    @Override
    public void connectionClosedOnError(Exception e) {

    }

    @Override
    public void reconnectionSuccessful() {

    }

    @Override
    public void reconnectingIn(int seconds) {

    }

    @Override
    public void reconnectionFailed(Exception e) {

    }
}
