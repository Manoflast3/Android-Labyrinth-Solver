package ca.uwaterloo.lab3_201_03;

/**
 * Created by Tony Wang on 7/11/2016.
 */

import ca.uwaterloo.sensortoy.*;

import android.graphics.PointF;
import android.util.ArraySet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class newListener implements PositionListener {

    private PointF originPoint = new PointF();
    private PointF destinationPoint = new PointF();

    private NavigationalMap navMap;

    public newListener(){};


    public newListener(NavigationalMap map){
//        userLocation = this.userLocation;
        this.navMap = map;
    }

    public void takeStep(MapView source, Float relativeEW, Float relativeNS){
        source.setUserPoint(relativeEW, relativeNS);
    }

    public void calculatePath(MapView source) {


    }

    public void getRoute(PointF point,  List<PointF> displayPath){
    }

    //recursive function which adds all valid adjacent nodes, as well as valid adjacnet nodes of those adjacent nodes etc.. until it reaches starting point
    public void addPoints(PointF checkPoint, int stepcounter, PointF previousPoint){

    }

    public void destinationChanged(MapView source, PointF dest) {

        source.setDestinationPoint(dest);
        destinationPoint = source.getDestinationPoint();

        PathFinder pathFinder = new PathFinder();
        pathFinder.setCurentLoc(source.getUserPoint());

        pathFinder.setUserEnd(source.getDestinationPoint());
        source.setUserPath(pathFinder.findPath(navMap));

    }

    public void originChanged(MapView source, PointF loc){

        source.setUserPoint(loc);
        source.setOriginPoint(loc);
        originPoint = source.getOriginPoint();
//
//        PathFinder pathFinder = new PathFinder();
//        pathFinder.setCurentLoc(source.getOriginPoint());
//
//        pathFinder.setUserEnd(source.getDestinationPoint());
//        source.setUserPath(pathFinder.findPath(navMap));

    }

    public void getPathButton(MapView source){
        calculatePath(source);
        source.setUserPoint(1f,1f);
    }
}
