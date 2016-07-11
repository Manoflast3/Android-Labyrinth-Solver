package ca.uwaterloo.lab3_201_03;

import android.graphics.PointF;

import java.util.List;

import ca.uwaterloo.map.LineSegment;
import ca.uwaterloo.map.NavigationalMap;
import ca.uwaterloo.map.NavigationalMap.*;
import ca.uwaterloo.map.LineSegment.*;
import ca.uwaterloo.map.VectorUtils.*;

/**
 * Created by manof on 2016-07-10.
 *
 * This is a findPath algorithm using the Pledge Algorithm
 */
public class PathFinder {
    private List<PointF> userPath;
    private List<LineSegment> nearestParallelWalls;
    private String cDirection = "up";
    private int sumofTurns = 0;

    // I need the value of the preset user location!
    private PointF userStart;
    private PointF userEnd;
    private PointF lastpoint = userStart;
    private PointF temppoint = lastpoint;
    private final float stepValue = 0.5f;

    // I also need the angle of the map. This angle is measured from the vertical, from 0 to 180 degrees.
    // (doesn't go to 180 though).
    private final double angle = 0.0f;


    public List<PointF> findPath(NavigationalMap source){

        userPath.add(userStart);

        // If a direct path can be made.
        if(source.calculateIntersections(userStart, userEnd).isEmpty()){
            userPath.add(userEnd);
        }
        else{
            setupPath(userPath, source);
        }

        return userPath;
    }

    public void setupPath(List<PointF> userPath, NavigationalMap source){
        while(source.calculateIntersections(lastpoint, temppoint).isEmpty()){
            takeStep(temppoint);
        }
    }

    public PointF takeStep(PointF temppoint){
        temppoint.offset((float)(Math.sin(angle)*stepValue), (float)(Math.cos(angle)*stepValue));
        return temppoint;
    }



}
