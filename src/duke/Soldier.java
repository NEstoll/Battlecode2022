package duke;

import battlecode.common.*;
import static duke.RobotPlayer.*;

public class Soldier extends Droid{
    MapLocation target = null;

    public Soldier(RobotController rc) {
        super(rc);
    }

    @Override
    public void act() throws GameActionException {
        if (target != null && rc.canSenseLocation(target)) {
            if (rc.senseRobotAtLocation(target) == null) {
                rc.writeSharedArray(rc.getArchonCount()+1, 0);
                target = null;
            }
        }

        // Try to attack someone
        int radius = rc.getType().actionRadiusSquared;
        Team opponent = rc.getTeam().opponent();
        RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
        for (RobotInfo r: enemies) {
            //look for HQ's
            if (r.getType() == RobotType.ARCHON) {
                int loc =r.getLocation().x << 6;
                loc += r.getLocation().y;
                rc.writeSharedArray(rc.getArchonCount()+1, loc);
            }
        }
        if (target != null && rc.canAttack(target)) {
            rc.attack(target);
        }
        if (enemies.length > 0) {
            MapLocation toAttack = enemies[0].location;
            if (rc.canAttack(toAttack)) {
                rc.attack(toAttack);
            }
        }

        //pathfinding
        if (moveQueue.isEmpty() || target == null) {
            //need new location to move to
            target = null;
            //check if target known
            int n =rc.readSharedArray(rc.getArchonCount()+1);
            if (n != 0) {
                target = new MapLocation(((n >> 6) & 63), n & 63);
            }
            if (target == null) {
                //no target found, search for one
                int index = rng.nextInt(rc.getArchonCount());
                int num = rc.readSharedArray(index);
                int rand = rng.nextInt(3);
                if (rand == 0) {
                    //check rotational symmetry
                    target = new MapLocation(rc.getMapWidth() - ((num >> 6) & 63), rc.getMapHeight() - num & 63);
                } else if (rand == 1) {
                    //horizontal
                    target = new MapLocation(rc.getMapWidth() - ((num >> 6) & 63), num & 63);
                } else {
                    //vertical
                    target =new MapLocation(((num >> 6) & 63), rc.getMapHeight() - num & 63);
                }
            }
            pathfind(target);
        }
        if (moveQueue.peek() != null && rc.canMove(moveQueue.peek())) {
            rc.move(moveQueue.poll());
        }
    }
}
