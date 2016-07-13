package ca.uwaterloo.lab3_201_03;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;


import java.util.Arrays;

import ca.uwaterloo.sensortoy.*;

public class Lab3_201_03 extends AppCompatActivity{
    private SensorManager sensorManager;
    private Sensor accSensor;
    private LineGraphView graph;

    private Sensor mAcc;
    private Sensor mMag;


    private MapView  mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3_201_03);

        mapView = new  MapView(getApplicationContext(), 800, 800, 35, 35);

        // Locks Screen Orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mAcc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMag = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        TextView Current = (TextView)findViewById(R.id.Current);
        TextView Max = (TextView)findViewById(R.id.Max);
        TextView [] Sensor = {Current, Max};
        TextView OView = (TextView) findViewById(R.id.orientation);

        TextView destination = (TextView) findViewById(R.id.destination);
        TextView stepCount = (TextView)findViewById(R.id.stepCount);
        Button button = (Button) findViewById(R.id.start2);
        TextView dirView = (TextView)findViewById(R.id.direction);
        TextView locView = (TextView)findViewById(R.id.location);
        TextView[] viewComb = {stepCount, dirView, locView, destination};
        Orientation orientation = new Orientation(OView);


//
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);
        LinearLayout mapLayout = (LinearLayout) findViewById(R.id.map);


        graph = new LineGraphView(getApplicationContext(),
                100,
                Arrays.asList("x", "y", "z"));
        linearLayout.addView(graph);
        graph.setVisibility(View.VISIBLE);

        SensorEventListener lineGraph = new LineGraphListener(graph, Sensor);
        sensorManager.registerListener(lineGraph, accSensor,SensorManager.SENSOR_DELAY_FASTEST);


        registerForContextMenu(mapView);
        NavigationalMap map = MapLoader.loadMap(getExternalFilesDir(null),"Lab-room-peninsula.svg");
        newListener listener = new newListener(map);
        mapView.addListener(listener);
        mapView.setMap(map);
        mapLayout.addView(mapView);


        SensorEventListener counter = new StepCounter(viewComb, button, orientation, listener, mapView, map);
        sensorManager.registerListener(counter, accSensor,SensorManager.SENSOR_DELAY_FASTEST);

        sensorManager.registerListener(orientation, mAcc,SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(orientation, mMag,SensorManager.SENSOR_DELAY_UI);
    }
    @Override
    public void  onCreateContextMenu(ContextMenu  menu , View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu , v, menuInfo);
        mapView.onCreateContextMenu(menu , v, menuInfo);
    }


    @Override
    public  boolean  onContextItemSelected(MenuItem item)
    {
        return  super.onContextItemSelected(item) ||  mapView.onContextItemSelected(item);
    }
}
