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

    private LineSegment followedWall;

    // I also need the angle of the map. This angle is measured from the vertical, from 0 to 180 degrees.
    // (doesn't go to 180 though).
    private final double angle = 0.0f;


    public List<PointF> findPath(NavigationalMap source){

        userPath.add(userStart);

        // If a direct path can be made.
        if(source.calculateIntersections(userStart, userEnd).isEmpty()){

        }
        else{
            setupPath(source);
            userPath.add(lastpoint);

            // Proceed with Wall following algorithm IF the solution has not yet been found.
            if (!source.calculateIntersections(lastpoint,userEnd).isEmpty()){
                while (!source.calculateIntersections(lastpoint, userEnd).isEmpty()) {
                    switch (cDirection) {
                        case "up":
                            takeStep(temppoint, cDirection);
                            // if the next step goes through a wall.
                            if (!source.calculateIntersections(lastpoint, temppoint).isEmpty()){
                                lastpoint.set(source.calculateIntersections(lastpoint, temppoint).get(0).getPoint());
                                followedWall = source.getGeometryAtPoint(lastpoint).get(0);
                                cDirection = "right";
                            }
                            // if the next step goes past the currently followed wall.
                            else if (temppoint.y > Math.max(followedWall.start.y, followedWall.end.y)){
                                lastpoint.set(Math.min(followedWall.start.x, followedWall.end.x), Math.max(followedWall.start.y, followedWall.end.y));
                                followedWall = source.getGeometryAtPoint(lastpoint).get(0);
                                cDirection = "left";
                            }

                            // take another step. 
                            else {
                                takeStep(temppoint, cDirection);
                            }
                            break;

                        case "down":

                            break;

                        case "right":

                            break;

                        case "left":

                            break;

                        default:
                    }
                }
            }
        }

        userPath.add(userEnd);

        return userPath;
    }

    public void setupPath( NavigationalMap source){
        while(source.calculateIntersections(lastpoint, temppoint).isEmpty()){
            takeStep(temppoint, cDirection);
            if (source.calculateIntersections(temppoint, userEnd).isEmpty()){
                lastpoint.set(temppoint);
                break;
            }
        }
        if (lastpoint!=temppoint) {
            lastpoint.set(source.calculateIntersections(lastpoint, temppoint).get(0).getPoint());
        }

        followedWall = source.getGeometryAtPoint(lastpoint).get(0);
        cDirection = "right";
        sumofTurns++;
    }

    public PointF takeStep(PointF temppoint, String cDirection){
        switch (cDirection){
            case "up":
                temppoint.offset((float)(-Math.sin(angle)*stepValue), (float)(Math.cos(angle)*stepValue));
                break;
            case "down":
                temppoint.offset((float)(Math.sin(angle)*stepValue), (float)(-Math.cos(angle)*stepValue));
                break;
            case "right":
                temppoint.offset((float)(Math.cos(angle)*stepValue), (float)(Math.sin(angle)*stepValue));
                break;
            case "left":
                temppoint.offset((float)(-Math.cos(angle)*stepValue), (float)(-Math.sin(angle)*stepValue));
                break;
        }
        return temppoint;
    }



}
