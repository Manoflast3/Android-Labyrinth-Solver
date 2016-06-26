package ca.uwaterloo.lab3_201_03;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Tony Wang on 5/28/2016.
 */ //Rotation vector listener.

class StepCounter implements SensorEventListener {
    //2 textviews; both current and max are passed into the listener at the same time.
    protected int totalStepCount;
    private TextView stepView, dirView, locView;
    private Orientation orientation;
    private float direction;
    private double[] location = {0,0};;
    //put in 2 textviews, first is current and second is max.
    public StepCounter(TextView[] viewComb, Button button, Orientation orientation) {
        stepView = viewComb[0];
        dirView = viewComb[1];
        locView = viewComb[2];

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reset();
            }
        });
        this.orientation = orientation;
    }

    public void onAccuracyChanged(Sensor s, int i) {
    }

    private int state = 0;
    private long delay = 2000;
    private boolean period = true;

    public void reset() {
        totalStepCount = 0;
        state = 0;
        period = true;
        location[0]=0;
        location[1]=0;
        locView.setText(String.format("(%f,%f)",location[0],location[1]));
    }



    public void enableCounter(){
        period = true;
    }

    public void resetCounter(){
        state = 0;
        period = false;
    }

    protected float lastDir = 0;
    public int getDirection(){
        float a = 1f;
        float current = a * orientation.getDir() + (1-a)*lastDir;
        lastDir = current;
        this.direction = current;
        double temp = direction/Math.PI*180;
        return (int)(temp/20)*20;
    }






    protected float lastAcc = 0;
    public void onSensorChanged(SensorEvent se) {
        float a = 0.9f;
        float current = a * se.values[2] + (1-a)*lastAcc;
        lastAcc = current;
        if((Math.abs(se.values[0])>8||Math.abs(se.values[1])>8||Math.abs(se.values[2])>8)){
            resetCounter();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    enableCounter();
                }

            }, delay);
        }else
        if(period) {
            dirView.setText(String.format("%d",getDirection()));
            if (state == 0) {
                if (current > 0 && current < 0.5) {
                    state = 1;
                }
            } else if (state == 1) {
                if (current > 1 && current < 2) {
                    state = 2;
                } else if (Math.abs(current) > 6) {
                    state = 0;
                }
            }else if (state == 2) {
                if (current > 1.4 && current < 4) {
                    state = 3;
                } else if (Math.abs(current) > 6) {
                    state = 0;
                }
            }else if (state == 3) {
                if (current > 0 && current < 1) {
                    state = 4;
                } else if (Math.abs(current) > 6) {
                    state = 0;
                }
            } else if (state == 4) {
                if (current > -1 && current < 0) {
                    state = 5;
                } else if (Math.abs(current) > 6) {
                    state = 0;
                }
            } else if (state == 5) {
                if (current > -2 && current < -1) {
                    state = 6;
                } else if (Math.abs(current) > 6) {
                    state = 0;
                }
            } else if (state == 6) {
                if (current > -4 && current < -1.4) {
                    state = 7;
                } else if (Math.abs(current) > 6) {
                    state = 0;
                }

            } else if (state == 7) {
                if (current > -0.5 && current < 0) {
                    totalStepCount++;
                    state = 0;
                    location[0] += Math.cos(getDirection()/180);
                    location[1] += Math.sin(getDirection()/180);
                    locView.setText(String.format("(%f,%f)",location[0],location[1]));
                } else if (Math.abs(current) > 5) {
                    state = 0;
                }

            }
        }
            stepView.setText(Integer.toString(totalStepCount));
        
    }








}
