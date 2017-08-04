package com.rxd.mychat.xmpp;

import android.util.Log;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Created by Administrator on 2017/6/6.
 */

public class XmppConnectionManager {

    private static XmppConnectionManager manager;

    private XmppConnectionManager(){

    }

    public static XmppConnectionManager getInstance(){
        if (manager == null){
            manager = new XmppConnectionManager();
        }
        return manager;
    }

    public XMPPTCPConnection init(){
        XMPPTCPConnectionConfiguration config = null;

        try {
            InetAddress addr = InetAddress.getByName("192.168.0.176");
            DomainBareJid serviceName = JidCreate.domainBareFrom("shengcheng");
            Log.d("sc",serviceName.toString());
            HostnameVerifier verifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return false;
                }
            };
            config = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword("admin","1993323")
                    .setHost("192.168.0.176")
                    .setPort(5222)
                    .setXmppDomain(serviceName)
                    .setHostAddress(addr)
                    .setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.disabled)
                    .setHostnameVerifier(verifier)
                    .setCompressionEnabled(false)
                    .setDebuggerEnabled(true).build();
            //需要经过同意才可以添加好友
            Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        XMPPTCPConnection connection = new XMPPTCPConnection(config);
        try {
            connection.connect();
            Log.d("sc","manager():"+connection.isConnected());
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return connection;


    }

}
