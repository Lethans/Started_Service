package com.example.startedservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {


    boolean isBound = false;
    private MyBoundService myBoundService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyBoundService.MyLocalBinder myLocalBinder = (MyBoundService.MyLocalBinder) iBinder;
            myBoundService = myLocalBinder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(SecondActivity.this, MyBoundService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
        /*
        * Flags
        * BIND_AUTO_CREATE : Automatically creates the service as long as components are bound to it
        * BIND_DEBUG_UNBIND : Create debug information for unmatched bind/unbind calls
        * BIND_NOT_FOREGROUND : The bound service will never be brought to the foreground process level
        * 0[ZERO] : none of the above.
        * */
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(mConnection);
            isBound = false;
        }
    }

    public void onClickEvent(View view) {

        TextView myResult = findViewById(R.id.textView);
        int firstNumber = Integer.parseInt(((EditText) findViewById(R.id.editTextTextPersonName)).getText().toString());
        int secondNumber = Integer.parseInt(((EditText) findViewById(R.id.editTextTextPersonName2)).getText().toString());


        if (!isBound)
            return;

        String result = "";

        switch (view.getId()) {
            case (R.id.btn_add):
                result = String.valueOf(myBoundService.add(firstNumber, secondNumber));
                break;
            case (R.id.btn_subtract):
                result = String.valueOf(myBoundService.substract(firstNumber, secondNumber));
                break;
            case (R.id.btn_multiply):
                result = String.valueOf(myBoundService.multiply(firstNumber, secondNumber));
                break;
            case (R.id.btn_divide):
                result = String.valueOf(myBoundService.divide(firstNumber, secondNumber));
                break;
        }
        myResult.setText(result);
    }
}