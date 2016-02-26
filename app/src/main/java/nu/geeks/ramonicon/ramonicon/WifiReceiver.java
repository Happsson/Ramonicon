package nu.geeks.ramonicon.ramonicon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;
import android.view.ViewDebug;
import android.widget.Toast;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Hannes on 2016-02-26.
 */
public class WifiReceiver extends BroadcastReceiver implements WifiP2pManager.ConnectionInfoListener, WifiP2pManager.ActionListener {

    private WifiP2pManager manager;
    private Channel channel;
    private WifiActivity myWifiActivity;
    private ArrayList<WifiP2pDevice> devices;

    final String TAG = "WIFIACTIVITY";


    public WifiReceiver(WifiP2pManager manager, Channel channel, WifiActivity myWifiActivity){
        super();
        this.manager = manager;
        this.channel = channel;
        this.myWifiActivity = myWifiActivity;
        this.devices = new ArrayList<>();
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
                        //Clear device list
                        devices.clear();
                        Log.e("WIFIACTIVITY", "peers avaliable");
                        devices.addAll(peers.getDeviceList());
                        for(WifiP2pDevice dev : devices){
                            Log.e(TAG, dev.deviceName);
                        }



                        connect();

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

    public void connect(){
        if(!devices.isEmpty()) {
            WifiP2pDevice device = devices.get(0);
            WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = device.deviceAddress;
            config.wps.setup = WpsInfo.PBC;

            Log.e(TAG, "Connect()");
            manager.connect(channel, config, this);
        }
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        Log.e(TAG,"onConnectionInfoAvailable");
        myWifiActivity.updateText("Group formed: "  + info.groupFormed);


        // InetAddress from WifiP2pInfo struct.
        String groupOwnerAddress = info.groupOwnerAddress.getHostAddress();

        // After the group negotiation, we can determine the group owner.
        if (info.groupFormed && info.isGroupOwner) {
            // Do whatever tasks are specific to the group owner.
            // One common case is creating a server thread and accepting
            // incoming connections.
            Log.e(TAG,"Group Formed");
        } else if (info.groupFormed) {
            Log.e(TAG,"Group Joined");
            // The other device acts as the client. In this case,
            // you'll want to create a client thread that connects to the group
            // owner.
        }
    }

    @Override
    public void onSuccess() {
        Log.e(TAG,"onConnect Success");
    }

    @Override
    public void onFailure(int i) {

    }
}
