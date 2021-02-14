
package com.example.startedservice;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyIntentService extends IntentService {

    private static final String TAG = MyIntentService.class.getSimpleName();

    //In the IntentService we dont need to call the override
    //of onBind or onStartCommand

    public MyIntentService() {
        super("ThisIsTheWorkerThreadName"); //Give the name of the worker thread
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //All operations in here are perform in a background thread

        int sleepTime = intent.getExtras().getInt("EXAMPLE");
        int ctrl = 1;

        while (ctrl <= sleepTime) {
            Log.i(TAG, "onHandleIntent: Counter is " + ctrl);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ctrl++;
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
