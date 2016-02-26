package nu.geeks.ramonicon.ramonicon;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Hannes on 2016-02-26.
 */
public class WifiActivity extends Activity {

    Button testBt;
    TextView tvMsg;

    WifiP2pManager manager;
    WifiP2pManager.Channel channel;
    BroadcastReceiver bReceiver;

    IntentFilter intentFilter;

    final String TAG = "WIFIACTIVITY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testBt = (Button) findViewById(R.id.bTest);
        tvMsg = (TextView) findViewById(R.id.tvMsg);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        bReceiver = new WifiReceiver(manager, channel, this);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        testBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.e(TAG, "discovered peers");
                    }

                    @Override
                    public void onFailure(int reason) {
                        Log.e(TAG, "Did not discover peers");
                    }
                });

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(bReceiver, intentFilter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(bReceiver);
    }
}
