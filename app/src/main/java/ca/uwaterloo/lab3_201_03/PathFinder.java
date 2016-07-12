package ca.uwaterloo.lab3_201_03;

import android.graphics.PointF;

import java.util.ArrayList;
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
    private List<PointF> userPath = new ArrayList<PointF>();
    // This is the favored direction for the Pledge Algorithm
    // Sum of turns is the net sum of turns. If it is 0 while we are headed the favored direction,
    // we stop following the current wall and keep going.
    private String cDirection = "up";
    private int sumofTurns = 0;

    // TODO need the value of the current user location, and the Destination (userend)!
    private PointF currentLocation;
    private PointF userEnd;

    private PointF lastpoint = currentLocation;
    private PointF temppoint = lastpoint;
    private PointF forwardPoint = lastpoint;
    private PointF refPoint = lastpoint;

    private final float stepValue = 0.5f;

    // TODO need the angle of the map. This angle is measured from the vertical, from 0 to 180 degrees.
    // (doesn't go to 180 though).
    private final double angle = 0.0f;


    public List<PointF> findPath(NavigationalMap source) {

        userPath.add(currentLocation);

        // If a direct path can be made.
        if (source.calculateIntersections(currentLocation, userEnd).isEmpty()) {

        } else {
            setupPath(source);
            userPath.add(lastpoint);
            takeStep(temppoint, cDirection);
            takeStep(forwardPoint, cDirection);

            // Proceed with Wall following algorithm IF the solution has not yet been found.
            while (!source.calculateIntersections(lastpoint, userEnd).isEmpty()) {
                switch (cDirection) {
                    case "up":
                        takeStep(forwardPoint, cDirection);
                        refPoint.set(forwardPoint);
                        takeStep(refPoint, "left");
                        // if the next step goes through a wall.
                        if (!source.calculateIntersections(temppoint, forwardPoint).isEmpty()) {
                            lastpoint.set(temppoint);
                            userPath.add(lastpoint);
                            cDirection = "right";
                            sumofTurns++;
                        }
                        // if the next step goes past the current wall.
                        else if (source.calculateIntersections(forwardPoint, refPoint).isEmpty()) {
                            if (sumofTurns == 0){
                                // Keep going the same direction.
                                takeStep(temppoint, cDirection);
                            }
                            else {
                                temppoint.set(forwardPoint);
                                lastpoint.set(temppoint);
                                userPath.add(lastpoint);
                                cDirection = "left";
                                sumofTurns--;
                            }
                        } else {
                            takeStep(temppoint, cDirection);
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

    public String setupPath(NavigationalMap source) {
        forwardPoint.set(temppoint);
        takeStep(forwardPoint, cDirection);
        while (source.calculateIntersections(temppoint, forwardPoint).isEmpty()) {
            takeStep(temppoint, cDirection);
            forwardPoint.set(temppoint);
            takeStep(forwardPoint, cDirection);
            if (source.calculateIntersections(temppoint, userEnd).isEmpty()) {
                lastpoint.set(temppoint);
                break;
            }
        }
        if (lastpoint.x != temppoint.x || lastpoint.y != temppoint.y) {
            lastpoint.set(temppoint);
        }
        cDirection = "right";
        sumofTurns++;
        return cDirection;
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
        takeStep(forwardPoint, cDirection);
        refPoint.set(forwardPoint);
        switch (cDirection) {
            case "down":
                takeStep(refPoint, "right");
                break;
            case "right":
                takeStep(refPoint, "up");
                break;
            case "left":
                takeStep(refPoint, "down");
                break;
        }
        // if the next step goes through a wall.
        if (!source.calculateIntersections(temppoint, forwardPoint).isEmpty()) {
            lastpoint.set(temppoint);
            userPath.add(lastpoint);
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
        else if (source.calculateIntersections(forwardPoint, userEnd).isEmpty()){
            takeStep(temppoint, cDirection);
            lastpoint.set(temppoint);
            userPath.add(temppoint);

        }
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
        } else {
            takeStep(temppoint, cDirection);
        }
    }
}


