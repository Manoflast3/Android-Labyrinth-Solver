package ca.uwaterloo.lab3_201_03;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

class Orientation implements SensorEventListener{



    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;

    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float direction;
    protected TextView output;

    public Orientation(TextView orientation){
        output = orientation;
    }

//    protected void onResume() {
//        mLastAccelerometerSet = false;
//        mLastMagnetometerSet = false;
//        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
//    }
//
//    protected void onPause() {
//        mSensorManager.unregisterListener(this);
//    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public float getDir(){
        return mOrientation[0];
    }

    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(se.values, 0, mLastAccelerometer, 0, se.values.length);
            mLastAccelerometerSet = true;
        } else if (se.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(se.values, 0, mLastMagnetometer, 0, se.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            String temp =String.format("(%f, %f, %f)", mOrientation[0], mOrientation[1], mOrientation[2]);
            output.setText(temp);
            direction = mOrientation[0];
        }
    }
}