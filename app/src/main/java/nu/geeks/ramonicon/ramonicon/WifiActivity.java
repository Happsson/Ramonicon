package nu.geeks.ramonicon.ramonicon;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Hannes on 2016-02-26.
 */
public class WifiActivity extends AppCompatActivity {

    //Game stuff
    TextView tvTimer, tvInfo;
    float timerTime;
    RelativeLayout rlClick;
    CountDownTimer timer;
    boolean timerIsRunning;

    //WIFI stuff
    WifiP2pManager manager;
    WifiP2pManager.Channel channel;
    BroadcastReceiver bReceiver;

    IntentFilter intentFilter;

    final String TAG = "WIFIACTIVITY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);

        createWifiStuff();
        createGameStuff();

    }

    private void createGameStuff(){
        timerTime  = 2;
        timerIsRunning = false;

        timer = new CountDownTimer(2000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTime -= 0.01;
            }

            @Override
            public void onFinish() {
                timesUp();
            }
        };

        tvInfo = (TextView) findViewById(R.id.tvInfo);
        tvTimer = (TextView) findViewById(R.id.tvMsg);
        rlClick = (RelativeLayout) findViewById(R.id.bgClickable);
        rlClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerIsRunning) {
                    userPressedInTime();
                }
            }
        });

    }

    /**
     * User pressed in time, next player!
     */
    private void userPressedInTime() {
        //TODO - alert next random player
        //TODO - play of the corresponding player.



    }

    public void updateText(String text){
        tvInfo.setText(text);
    }

    //User did not press in time!
    private void timesUp() {
        tvInfo.setText("DRINK!");
    }

    //TODO - den här metoden ska kallas på när ens ljud spelas upp på någon annan mobil.
    public void startTimer(){
        timer.start();
    }

    private void createWifiStuff(){

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        bReceiver = new WifiReceiver(manager, channel, this);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){

            case R.id.findDevices:
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
                break;


        }


        return true;
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
