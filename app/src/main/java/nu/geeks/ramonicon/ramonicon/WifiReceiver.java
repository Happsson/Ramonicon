package nu.geeks.ramonicon.ramonicon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;

/**
 * Created by Hannes on 2016-02-26.
 */
public class WifiReceiver extends BroadcastReceiver {

    private WifiP2pManager manager;
    private Channel channel;
    private MyWifiActivity myWifiActivity;



    public WifiReceiver(WifiP2pManager manager, Channel channel, MyWifiActivity myWifiActivity){
        super();


    }


    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
