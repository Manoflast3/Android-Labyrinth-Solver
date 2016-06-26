package ca.uwaterloo.lab3_201_03;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import ca.uwaterloo.sensortoy.LineGraphView;

/**
 * Created by Tony Wang on 5/28/2016.
 */ //Rotation vector listener.

class LineGraphListener implements SensorEventListener {
    LineGraphView graph;
    protected TextView[] output = new TextView[2];
    private float[] cur = new float[3];
    private float[] max = {0,0,0};
    float x,y,z,s;
    //put in 2 textviews, first is current and second is max.
    public LineGraphListener(LineGraphView outputView, TextView [] textviews) {

        graph = outputView;
        output = textviews;
    }

    public void onAccuracyChanged(Sensor s, int i) {
    }

    public void onSensorChanged(SensorEvent se) {
        graph.addPoint(se.values);
//go through all 3 values
        for (int i = 0; i < 3 ; i++) {
            cur[i] = se.values[i];
            //compare the max to the current value
            if(Math.abs(max[i])<=Math.abs(se.values[i])){
                max[i]=se.values[i];
            }
        }
        x = cur[0];
        y = cur[1];
        z = cur[2];
        String current = String.format("(%.2f, %.2f, %.2f)",x, y, z);
        x = max[0];
        y = max[1];
        z = max[2];
        String max = String.format("(%.2f, %.2f, %.2f)",x, y, z);
        //set the textviews.
        output[0].setText(current);
        output[1].setText(max);
    }
}
