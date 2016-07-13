package ca.uwaterloo.lab3_201_03;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.sensortoy.LineSegment;
import ca.uwaterloo.sensortoy.NavigationalMap;

/**
 * Created by Derek on 2016-07-10.
 *
 * This is a findPath algorithm using the Pledge Algorithm
 */
public class PathFinder {
    // TODO These Lists need to be initialized
    private List<PointF> userPath = new ArrayList<PointF>();
    // This is the favored direction for the Pledge Algorithm
    private String cDirection = "up";
    // private int sumofTurns = 0;

    // TODO need the value of the current user location, and the Destination (userend)!
    private PointF currentLocation = new PointF();
    private PointF userEnd = new PointF();

    public void setCurentLoc(PointF current){
        currentLocation = current;
    }
    public void setUserEnd(PointF destination){
        userEnd = destination;
    }


    private PointF lastpoint = new PointF(currentLocation.x , currentLocation.y);
    private PointF temppoint = new PointF(currentLocation.x , currentLocation.y);
    private PointF forwardPoint = new PointF(currentLocation.x , currentLocation.y);
    private PointF refPoint = new PointF(currentLocation.x , currentLocation.y);

    private final float stepValue = 1.0f;

    // TODO need the angle of the map. This angle is measured from the vertical, from 0 to 180 degrees.
    // (doesn't go to 180 though).
    private double angle = 0.0f;
    public void setAngle(double angle){
        this.angle = angle;
    }


    public List<PointF> findPath(NavigationalMap source) {

        userPath.add(currentLocation);

        // If a direct path can be made.
        if (source.calculateIntersections(currentLocation, userEnd).isEmpty()) {

        } else {
            // Setup path
            lastpoint.set(currentLocation);

            // Proceed with Wall following algorithm IF the solution has not yet been found.
            while (!source.calculateIntersections(temppoint, userEnd).isEmpty()) {
                switch (cDirection) {
                    case "up":
                        followWall(source);
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

    public PointF takeStep(PointF point, String cDirection) {
        switch (cDirection) {
            case "up":
                point.offset((float) (-Math.sin(angle) * stepValue), (float) (Math.cos(angle) * stepValue));
                break;
            case "down":
                point.offset((float) (Math.sin(angle) * stepValue), (float) (-Math.cos(angle) * stepValue));
                break;
            case "right":
                point.offset((float) (Math.cos(angle) * stepValue), (float) (Math.sin(angle) * stepValue));
                break;
            case "left":
                point.offset((float) (-Math.cos(angle) * stepValue), (float) (-Math.sin(angle) * stepValue));
                break;
        }
        return point;
    }

    public void followWall(NavigationalMap source) {
        // Forward point checks if the next step goes through a wall.
        // Refpoint checks if the next step goes beyond the current wall.
        forwardPoint.set(temppoint);
        takeStep(forwardPoint, cDirection);
        // if the next step goes through a wall.
        if (!source.calculateIntersections(temppoint, forwardPoint).isEmpty()) {
            lastpoint.set(temppoint);
            userPath.add(lastpoint);
            switch (cDirection) {
                case "up":
                    cDirection = "right";
                    break;
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
            // sumofTurns++;
        }
        else if (source.calculateIntersections(forwardPoint, userEnd).isEmpty()){
            takeStep(temppoint, cDirection);
            lastpoint.set(temppoint);
            userPath.add(temppoint);

        }
        /*
        // if the next step goes past the current wall.
        else if (source.calculateIntersections(forwardPoint, refPoint).isEmpty()) {
            temppoint.set(forwardPoint);
            switch (cDirection) {
                case "down":
                    cDirection = "right";
                    break;
                case "right":
                    cDirection = "down";
                    break;
                case "left":
                    cDirection = "up";
                    break;
            }
            lastpoint.set(temppoint);
            userPath.add(lastpoint);
            sumofTurns--;
        } */
        else {
            takeStep(temppoint, cDirection);
        }
    }
}