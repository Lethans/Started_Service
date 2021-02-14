package com.example.startedservice;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyStartedService extends Service {

    private static final String TAG = MyStartedService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        //If is not destroy, the onCreate in the service is only call once
        Log.i(TAG, "onCreate, Thread " + Thread.currentThread().getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand, Thread " + Thread.currentThread().getName());
        //it is called every time we call the service

        //We can return FLAGS. Android close the service when low on memory.
        /**START_STICKY:
         Service restarts automatically, intent loss(become null);
         START_REDELIVER_INTENT:
         Service restarts automatically, intent redelivered;
         START_NOT_STICKY: Service not restarted, intent loss (become null)**/

        int sleepTime = intent.getExtras().getInt("EXAMPLE", 1);
        new MyAsyncTask().execute(sleepTime);
        //Here we perform short operations, so we dont block the main ui thread, if is a long operation
        //we need to perform it in the worker / background thread.

        //return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Called when we destroy the service
        Log.i(TAG, "onDestroy, Thread " + Thread.currentThread().getName());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //Return null for a Started Service
        Log.i(TAG, "onBind, Thread " + Thread.currentThread().getName());
        return null;
    }

    @SuppressLint("StaticFieldLeak")
    class MyAsyncTask extends AsyncTask<Integer, String, String> {
        //In, progress and out parameters
        private final String TAG = MyStartedService.class.getSimpleName();


        @Override
        protected void onPreExecute() {
            //Main thread
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            //Main thread
            Toast.makeText(MyStartedService.this, values[0], Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onProgressUpdate: Count is : " + values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Integer... params) {
            //Worker / Background thread
            int sleepTime = params[0];

            int ctrl = 1;

            while (ctrl <= sleepTime) {
                publishProgress("Counter is " + ctrl);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ctrl++;
            }

            return "Stop at: " + ctrl;
        }

        @Override
        protected void onPostExecute(String str) {
            //Main Thread
            //Here we can automatically stop the service after the operation is finish
            //todo with this method stopSelf();
            super.onPostExecute(str);
            Intent intent = new Intent("action.service.to.activity");
            intent.putExtra("startedServiceResult", str);
            sendBroadcast(intent);
        }

    }

}
