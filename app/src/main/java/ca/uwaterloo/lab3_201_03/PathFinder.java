package ca.uwaterloo.lab3_201_03;

import android.graphics.PointF;

import java.util.List;

import ca.uwaterloo.map.LineSegment;
import ca.uwaterloo.map.NavigationalMap;

/**
 * Created by Derek on 2016-07-10.
 *
 * This is a findPath algorithm using the Pledge Algorithm
 */
public class PathFinder {
    // TODO These Lists need to be initialized
    private List<PointF> userPath;
    // This is the favored direction for the Pledge Algorithm
    private String cDirection = "up";
    private int sumofTurns = 0;

    // TODO need the value of the current user location, and the Destination (userend)!
    private PointF currentLocation;
    private PointF userEnd;

    private PointF lastpoint = currentLocation;
    private PointF temppoint = lastpoint;
    // TODO finish implementing forwardpoint logic
    private PointF forwardpoint = lastpoint;

    private final float stepValue = 0.5f;
    private final float smallOffset = 0.1f;

    private LineSegment followedWall;

    // TODO need the angle of the map. This angle is measured from the vertical, from 0 to 180 degrees.
    // (doesn't go to 180 though).
    private final double angle = 0.0f;


    public List<PointF> findPath(NavigationalMap source){

        userPath.add(currentLocation);

        // If a direct path can be made.
        if(source.calculateIntersections(currentLocation, userEnd).isEmpty()){

        }
        else{
            setupPath(source);
            userPath.add(lastpoint);
            takeStep(forwardpoint, cDirection);

            // Proceed with Wall following algorithm IF the solution has not yet been found.
            while (!source.calculateIntersections(lastpoint, userEnd).isEmpty()) {
                switch (cDirection) {
                    case "up":
                        takeStep(temppoint, cDirection);
                        // if the next step goes through a wall.
                        if (!source.calculateIntersections(lastpoint, temppoint).isEmpty()){
                            lastpoint.set(source.calculateIntersections(lastpoint, temppoint).get(0).getPoint());
                            temppoint.set(lastpoint);
                            userPath.add(lastpoint);
                            followedWall = source.getGeometryAtPoint(lastpoint).get(0);
                            cDirection = "right";
                            sumofTurns++;
                        }
                        // if the next step goes past the currently followed wall.
                        // TODO check if these values are correct for Max and Min fns.
                        else if (temppoint.y > Math.max(followedWall.start.y, followedWall.end.y)){
                            if (sumofTurns==0){
                                cDirection = "up";
                            }
                            else {
                                lastpoint.set(Math.min(followedWall.start.x, followedWall.end.x), Math.max(followedWall.start.y, followedWall.end.y));
                                List<LineSegment> wallsAtPoint = source.getGeometryAtPoint(lastpoint);
                                temppoint = lastpoint;
                                if (wallsAtPoint.size()==1) {
                                    followedWall = wallsAtPoint.get(0);
                                }
                                else if (wallsAtPoint.size()>1){
                                    followedWall = (!wallsAtPoint.get(0).theSame(followedWall)) ? wallsAtPoint.get(0) : wallsAtPoint.get(1);
                                }
                                else {
                                    System.out.println("Error!");
                                }
                                cDirection = "left";
                                userPath.add(lastpoint);
                                sumofTurns--;
                            }
                        }
                        break;

                    case "down":
                        followWall(source);
                        break;

                    case "right":
                        followWall(source);
                        break;

                    case "left":
                        followWall(source);
                        break;
                    default:
                        break;
                }
            }
        }

        userPath.add(userEnd);

        return userPath;
    }

    public String setupPath(NavigationalMap source){
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
        return cDirection;
    }

    public PointF takeStep(PointF point, String cDirection){
        switch (cDirection){
            case "up":
                point.offset((float)(-Math.sin(angle)*stepValue), (float)(Math.cos(angle)*stepValue));
                break;
            case "down":
                point.offset((float)(Math.sin(angle)*stepValue), (float)(-Math.cos(angle)*stepValue));
                break;
            case "right":
                point.offset((float)(Math.cos(angle)*stepValue), (float)(Math.sin(angle)*stepValue));
                break;
            case "left":
                point.offset((float)(-Math.cos(angle)*stepValue), (float)(-Math.sin(angle)*stepValue));
                break;
        }
        return point;
    }

    public void followWall(NavigationalMap source){
        takeStep(temppoint, cDirection);
        // if the next step goes through a wall.
        if (!source.calculateIntersections(lastpoint, temppoint).isEmpty()){
            lastpoint.set(source.calculateIntersections(lastpoint, temppoint).get(0).getPoint());
            temppoint.set(lastpoint.x + smallOffset, lastpoint.y + smallOffset);

            // TODO figure out of Temppoint or Lastpoint make more sense.
            userPath.add(lastpoint);
            followedWall = source.getGeometryAtPoint(lastpoint).get(0);
            switch (cDirection) {
                case "down":
                    cDirection = "left";
                    break;
                case "right":
                    cDirection = "down";
                    break;
                case "left":
                    cDirection = "up";
                    break;
            }
            sumofTurns++;
        }
        // if the next step goes past the currently followed wall.
        // TODO check if these values are correct for Max and Min fns.
        // TODO Implement wall following with another temp point
        switch (cDirection) {
            case "down":
                if (temppoint.y < Math.min(followedWall.start.y, followedWall.end.y)){
                    cDirection = "right";
                    lastpoint.set(Math.max(followedWall.start.x, followedWall.end.x), Math.min(followedWall.start.y, followedWall.end.y));
                    //TODO see if we need this offset or not.
                    temppoint.set(lastpoint.x, lastpoint.y);
                    userPath.add(lastpoint);
                }
                break;
            case "right":
                if (temppoint.y > Math.max(followedWall.start.x, followedWall.end.x)) {
                    cDirection = "up";
                    lastpoint.set(Math.max(followedWall.start.x, followedWall.end.x), Math.max(followedWall.start.y, followedWall.end.y));
                    temppoint.set(lastpoint.x, lastpoint.y);
                    userPath.add(lastpoint);
                }
                break;
            case "left":
                if (temppoint.y < Math.min(followedWall.start.y, followedWall.end.y)) {
                    cDirection = "down";
                    lastpoint.set(Math.min(followedWall.start.x, followedWall.end.x), Math.min(followedWall.start.y, followedWall.end.y));
                    temppoint.set(lastpoint.x, lastpoint.y);
                    userPath.add(lastpoint);
                }
                break;
        }

        // Update followedWall
        List<LineSegment> wallsAtPoint = source.getGeometryAtPoint(lastpoint);
        if (wallsAtPoint.size()==1) {
            followedWall = wallsAtPoint.get(0);
        }
        else if (wallsAtPoint.size()>1){
            followedWall = (!wallsAtPoint.get(0).theSame(followedWall)) ? wallsAtPoint.get(0) : wallsAtPoint.get(1);
        }
        else {
            System.out.println("Error!");
        }
        sumofTurns--;
        }
    }

