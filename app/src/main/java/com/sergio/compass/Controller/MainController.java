package com.sergio.compass.Controller;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import com.sergio.compass.R;
import com.sergio.compass.View.MainActivity;

public class MainController implements SensorEventListener {

    private MainActivity mainActivity;
    private Sensor rotationVector, accelerometer, magnetometer;
    private int delta;
    private float[] rMat = new float[9], orientation = new float[9];
    private float[] lastAccelerometer = new float[3], lastMagnetometer = new float[3];
    private boolean haveSensor = false, haveSecondSensor = false, lastAccelerometerSet = false;
    private boolean lastMagnetometerSet = false;


    //*****************CONSTRUCTOR*************************
    public MainController(MainActivity activity){
        mainActivity = activity;
    }

    //**********************OVERRIDE METHODS*******************

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType()==Sensor.TYPE_ROTATION_VECTOR){
            SensorManager.getRotationMatrixFromVector(rMat, event.values);
            delta = (int) ((Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0])+360)%360);

        }

        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            System.arraycopy(event.values, 0, lastAccelerometer, 0 , event.values.length);
            lastAccelerometerSet= true;

        } else if (event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){

            System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.length);
            lastMagnetometerSet=true;

        }

        if(lastAccelerometerSet&&lastMagnetometerSet){

            SensorManager.getRotationMatrix(rMat, null, lastAccelerometer, lastMagnetometer);
            SensorManager.getOrientation(rMat, orientation);
            delta = (int) ((Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0])+360)%360);

        }

        delta = Math.round(delta);
        mainActivity.getViewCompass().setRotation(-delta);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //**********************INSTANCE METHODS*******************

    public void startSensors(){

        if (mainActivity.getSensorManager().getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null) {

            if(mainActivity.getSensorManager().getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)==null ||
                    mainActivity.getSensorManager().getDefaultSensor(Sensor.TYPE_ACCELEROMETER)==null)
                Toast.makeText(mainActivity, mainActivity.getResources().getString(R.string.strNoSensors),
                        Toast.LENGTH_LONG).show();

            else {

                accelerometer = mainActivity.getSensorManager().getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                magnetometer = mainActivity.getSensorManager().getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

                haveSensor = mainActivity.getSensorManager().registerListener(this,
                        accelerometer,
                        SensorManager.SENSOR_DELAY_UI);
                haveSecondSensor = mainActivity.getSensorManager().registerListener(this,
                        magnetometer,
                        SensorManager.SENSOR_DELAY_UI);
            }

        } else {
            rotationVector = mainActivity.getSensorManager().getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            haveSensor = mainActivity.getSensorManager().registerListener(this, rotationVector,
                    SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void stopSensors(){

        if(haveSecondSensor&&haveSensor){

            mainActivity.getSensorManager().unregisterListener(this, accelerometer);
            mainActivity.getSensorManager().unregisterListener(this, magnetometer);

        } else if(haveSensor) mainActivity.getSensorManager().unregisterListener(this,
                rotationVector);
    }

}
