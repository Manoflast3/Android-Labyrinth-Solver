package ca.uwaterloo.lab3_201_03;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

class Orientation implements SensorEventListener{



    private float[] oLastAccelerometer = new float[3];
    private float[] oLastMagnetometer = new float[3];
    private boolean lastAcc = false;
    private boolean lastMag = false;

    private float[] R = new float[9];
    private float[] orient = new float[3];
    private float direction;
    protected TextView output;

    public Orientation(TextView orientation){
        output = orientation;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public float getDir(){
        return orient[0];
    }

    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            oLastAccelerometer = se.values;
            lastAcc = true;
        } else if (se.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            oLastMagnetometer = se.values;
            lastMag = true;
        }
        if (lastAcc && lastMag) {
            SensorManager.getRotationMatrix(R, null, oLastAccelerometer, oLastMagnetometer);
            SensorManager.getOrientation(R, orient);
            String temp =String.format("(%f, %f, %f)", orient[0], orient[1], orient[2]);
            output.setText(temp);
            direction = orient[0];
        }
    }
}