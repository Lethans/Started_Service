package com.example.startedservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    //Diferences between services
    //Started : May Block UI, MainThread (Work Around is AsyncTask), short operations, you have to stop the service.
    //Intent : WorkerThread (Background), Longer operations, automatically stop after performing the task

    private TextView started_txt, intent_txt;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        started_txt = findViewById(R.id.started_result_txvw);
        intent_txt = findViewById(R.id.intent_result_txvw);
    }

    /**
     * STARTED SERVICE WITH BROADCAST RECEIVER FOR RETRIEVE DATA
     **/

    public void startService(View view) {
        Intent intent = new Intent(MainActivity.this, MyStartedService.class);
        intent.putExtra("EXAMPLE", 10);
        startService(intent);
    }

    public void stopService(View view) {
        Intent intent = new Intent(MainActivity.this, MyStartedService.class);
        stopService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //For the broadcast receiver.
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.service.to.activity");
        registerReceiver(myStartedServiceReceiver, intentFilter);

    }

    private BroadcastReceiver myStartedServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String result = intent.getStringExtra("startedServiceResult");
            started_txt.setText(result);

        }
    };


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myStartedServiceReceiver);
    }

    /**
     * INTENT SERVICE WITH RESULT RECEIVER FOR RETRIEVE DATA
     **/
    public void startIntentService(View view) {
        Toast.makeText(this, "Wait 10 seconds", Toast.LENGTH_SHORT).show();
        ResultReceiver myResultReceiver = new MyResultReceiver(null);
        Intent intent = new Intent(MainActivity.this, MyIntentService.class);
        intent.putExtra("EXAMPLE", 10);
        //We pass the receiver as extras (is a parcelable), so we can return data from the intent service.
        intent.putExtra("receiver", myResultReceiver);
        startService(intent);
    }


    private class MyResultReceiver extends ResultReceiver {
        //First option to retrieve data from a service, the second one is a broadcast receiver
        //Communication within the same app
        public MyResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            //onReceiveResult operates in the background thread (the same as the intent service background thread
            // , we use a handler to access the main thread and set the textview.
            if (resultCode == 101 && resultData != null) {

                final String result = resultData.getString("resultIntentService");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        intent_txt.setText(result);
                    }
                });
            }
        }
    }


}