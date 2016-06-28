package ca.uwaterloo.lab3_201_03;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;


import java.util.Arrays;

import ca.uwaterloo.map.MapView;
import ca.uwaterloo.sensortoy.LineGraphView;

public class Lab3_201_03 extends AppCompatActivity{
    private SensorManager sensorManager;
    private Sensor accSensor;
    private LineGraphView graph;

    private Sensor mAccelerometer;
    private Sensor mMagnetometer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3_201_03);

        // Locks Screen Orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        TextView Current = (TextView)findViewById(R.id.Current);
        TextView Max = (TextView)findViewById(R.id.Max);
        TextView [] Sensor = {Current, Max};
        TextView OView = (TextView) findViewById(R.id.orientation);

        TextView stepCount = (TextView)findViewById(R.id.stepCount);
        Button button = (Button) findViewById(R.id.start);
        TextView dirView = (TextView)findViewById(R.id.direction);
        TextView locView = (TextView)findViewById(R.id.location);
        TextView[] viewComb = {stepCount, dirView, locView};
        Orientation orientation = new Orientation(OView);

        SensorEventListener counter = new StepCounter(viewComb, button, orientation);
        sensorManager.registerListener(counter, accSensor,SensorManager.SENSOR_DELAY_FASTEST);

        sensorManager.registerListener(orientation, mAccelerometer,SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(orientation, mMagnetometer,SensorManager.SENSOR_DELAY_UI);

        // Map
        MapView mv = new MapView ( getApplicationContext () , 400 , 400 , 60 , 60);
        registerForContextMenu (mv);


//
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);
        graph = new LineGraphView(getApplicationContext(),
                100,
                Arrays.asList("x", "y", "z"));
        linearLayout.addView(graph);
        graph.setVisibility(View.VISIBLE);

        SensorEventListener lineGraph = new LineGraphListener(graph, Sensor);
        sensorManager.registerListener(lineGraph, accSensor,SensorManager.SENSOR_DELAY_FASTEST);

    }

}
