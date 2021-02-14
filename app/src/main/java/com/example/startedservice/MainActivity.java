package com.example.startedservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {



    //Diferences between services
    //Started : May Block UI, MainThread (Work Around is AsyncTask), short operations, you have to stop the service.
    //Intent : WorkerThread (Background), Longer operations, automatically stop after performing the task


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startService(View view) {
        Intent intent = new Intent(MainActivity.this, MyStartedService.class);
        intent.putExtra("EXAMPLE", 10);
        startService(intent);
    }

    public void stopService(View view) {
        Intent intent = new Intent(MainActivity.this, MyStartedService.class);
        stopService(intent);
    }

    public void startIntentService(View view) {
        Intent intent = new Intent(MainActivity.this, MyIntentService.class);
        intent.putExtra("EXAMPLE", 10);
        stopService(intent);
    }
}