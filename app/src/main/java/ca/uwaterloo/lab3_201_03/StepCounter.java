package ca.uwaterloo.lab3_201_03;

import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.SQLOutput;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ca.uwaterloo.sensortoy.MapView;
import ca.uwaterloo.sensortoy.NavigationalMap;
import ca.uwaterloo.sensortoy.PositionListener;

/**
 * Created by Tony Wang on 5/28/2016.
 */ //Rotation vector listener.

class StepCounter implements SensorEventListener {
    //2 textviews; both current and max are passed into the listener at the same time.
    protected int totalStepCount;
    private TextView stepView, dirView, locView;
    private Orientation orientation;
    private float direction;
    private float[] location = {0,0};
    private newListener positionHandler;
    private MapView mapView;
    private NavigationalMap map;
    private PathFinder pathFinder;

    public final float stepLength = 0.3f;

    //put in 2 textviews, first is current and second is max.
    public StepCounter(TextView[] viewComb, Button button, Orientation orientation, newListener positionHandler, MapView mapView, NavigationalMap map) {
        stepView = viewComb[0];
        dirView = viewComb[1];
        locView = viewComb[2];
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reset();
            }
        });
        this.orientation = orientation;
        this.positionHandler = positionHandler;
        this.mapView = mapView;
        this.map = map;
        reset();
        pathFinder = new PathFinder();
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
        location[0]=mapView.getOriginPoint().x;
        location[1]=mapView.getOriginPoint().y;
        locView.setText(String.format("(%.1f,%.1f)",location[0],location[1]));
        positionHandler.takeStep(mapView, location[0], location[1]);
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
        return (int)((temp+10)/10)*10;
    }






    protected float lastAcc = 0;
    public void onSensorChanged(SensorEvent se) {
        float a = 0.9f;
        float current = a * se.values[2] + (1-a)*lastAcc;
        lastAcc = current;
        if((Math.abs(se.values[0])>10||Math.abs(se.values[1])>10||Math.abs(se.values[2])>10)){
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
            int temp = getDirection();
            dirView.setText(String.format("%d",temp));
            if (state == 0) {
                if (current > 0 && current < 0.5) {
                    state = 1;
                }
            } else if (state == 1) {
                if (current > 0.5 && current < 2) {
                    state = 2;
                } else if (Math.abs(current) > 6) {
                    state = 0;
                }
            }else if (state == 2) {
                if (current > 1.2 && current < 4) {
                    state = 3;
                } else if (Math.abs(current) > 6) {
                    state = 0;
                }
            }else if (state == 3) {
                if (current > -0.5 && current < 0.5) {
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
                if (current > -5 && current < -1.2) {
                    state = 7;
                } else if (Math.abs(current) > 6) {
                    state = 0;
                }

            } else if (state == 7) {
                if (current > -0.5 && current < 0) {
                    totalStepCount++;
                    state = 0;
                    location[1] -= Math.cos((double)temp*100/180/100*Math.PI)*stepLength;
                    location[0] += Math.sin((double)temp*100/180/100*Math.PI)*stepLength;
                    locView.setText(String.format("(%.1f,%.1f)",location[0],location[1]));

                    PointF pTemp = new PointF();
                    pTemp.set(location[0],location[1]);
                    pathFinder.setCurentLoc(pTemp);
                    System.out.println("abc "+ mapView.getDestinationPoint());
                    pathFinder.setUserEnd(mapView.getDestinationPoint());
                    mapView.setUserPath(pathFinder.findPath(map));








                    positionHandler.takeStep(mapView, location[0], location[1]);
                } else if (Math.abs(current) > 5) {
                    state = 0;
                }

            }
        }
            stepView.setText(Integer.toString(totalStepCount));
        
    }








}
