package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.txtHello);
        tv.setText("Hello from the code !");

        Log.i("TAG", "message informatif");
    }

    protected void onStart(){
        super.onStart();
        Log.i("CV", "onStart");
    }

    protected void onResume(){
        super.onResume();
        Log.i("CV", "onResume");
    }

    protected void onPause(){
        super.onPause();
        Log.i("CV", "onPause");
    }

    protected void onStop(){
        super.onStop();
        Log.i("CV", "onStop");
    }

    protected void onRestart(){
        super.onRestart();
        Log.i("CV", "onRestart");
    }

    protected void onDestroy(){
        super.onDestroy();
        Log.i("CV", "onDestroy");
    }
}
