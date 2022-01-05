package duke;

import battlecode.common.*;

import java.util.LinkedList;
import java.util.Queue;

public abstract class Droid extends Robot {
    Queue<Direction> moveQueue;

    public Droid(RobotController rc) {
        super(rc);
        moveQueue = new LinkedList<>();
    }

    void pathfind(MapLocation l) {
        rc.setIndicatorString("Pathing to " + l);
        moveQueue.clear();
        MapLocation here = rc.getLocation();
        while (!here.equals(l)) {
            moveQueue.add(here.directionTo(l));
            here = here.add(here.directionTo(l));
        }

    }
}
