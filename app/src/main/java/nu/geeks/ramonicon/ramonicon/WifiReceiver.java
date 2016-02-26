package nu.geeks.ramonicon.ramonicon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;
import android.widget.Toast;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Hannes on 2016-02-26.
 */
public class WifiReceiver extends BroadcastReceiver {

    private WifiP2pManager manager;
    private Channel channel;
    private WifiActivity myWifiActivity;
    private Collection<WifiP2pDevice> devices;

    final String TAG = "WIFIACTIVITY";


    public WifiReceiver(WifiP2pManager manager, Channel channel, WifiActivity myWifiActivity){
        super();
        this.manager = manager;
        this.channel = channel;
        this.myWifiActivity = myWifiActivity;
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("WIFIACTIVITY", "onRecieve");

        String action = intent.getAction();


        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            int state= intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                if(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                    Toast.makeText(myWifiActivity, "Wifi enabled", Toast.LENGTH_LONG).show();
            }else{
                    Toast.makeText(myWifiActivity, "Wifi NOT enabled", Toast.LENGTH_LONG).show();
                }

        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peersnew
            if(manager != null){
                manager.requestPeers(channel, new WifiP2pManager.PeerListListener() {
                    @Override
                    public void onPeersAvailable(WifiP2pDeviceList peers) {
                        Log.e("WIFIACTIVITY", "peers avaliable");
                        devices = peers.getDeviceList();
                        for(WifiP2pDevice dev : devices){
                            Log.e(TAG, dev.deviceName);
                        }

                    }
                });
                Log.e("WIFIACTIVITY", "manager.requestPeers kördes");
            }

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }
}
