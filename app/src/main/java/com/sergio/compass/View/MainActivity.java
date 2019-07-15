package com.sergio.compass.View;

import android.content.Context;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sergio.compass.Controller.MainController;
import com.sergio.compass.R;

public class MainActivity extends AppCompatActivity {


    private SensorManager sensorManager;
    private ViewCompass viewCompass;
    private MainController controller;

    //**********************GETTER AND SETTER METHODS*******************
    public SensorManager getSensorManager() {
        return sensorManager;
    }

    public ViewCompass getViewCompass() {
        return viewCompass;
    }


    //********************OVERRIDE METHODS*********************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public void onPause() {
        super.onPause();
        controller.stopSensors();
    }

    @Override
    public void onResume() {
        super.onResume();
        controller.startSensors();
    }

    //**********************INSTANCE METHODS*******************
    private void init(){
        viewCompass = findViewById(R.id.viewCompass);
        sensorManager = (SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
        controller = new MainController(this);
    }

}
