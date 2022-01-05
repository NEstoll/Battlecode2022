package duke;

import battlecode.common.*;
import static duke.RobotPlayer.*;

public class Soldier extends Droid{
    MapLocation goal = null;

    public Soldier(RobotController rc) {
        super(rc);
    }

    @Override
    public void act() throws GameActionException {
        if (rc.getRoundNum() > 585) {
            boolean stop = true;
        }
        rc.setIndicatorString(goal!=null?goal.toString():"null");
        //sensing/high level
        if (goal != null && rc.canSenseLocation(goal) && (rc.senseRobotAtLocation(goal) == null || rc.senseRobotAtLocation(goal).getType() != RobotType.ARCHON)) {
            goal = null;
            moveQueue.clear();
        }
        RobotInfo[] enemies = rc.senseNearbyRobots(rc.getType().actionRadiusSquared, rc.getTeam().opponent());
        RobotInfo t = null;
        for (RobotInfo r: enemies) {
            if (r.getType() == RobotType.ARCHON) {
                for (int i = archonCount; i< archonCount*2; i++) {
                    int num = rc.readSharedArray(i);
                    if (num == 0 || decodeArchon(num).getID() == r.getID()) {
                        rc.writeSharedArray(i, encodeArchon(r));
                        System.out.println("[" + i + "]: " + encodeArchon(r));
                        break;
                    }
                }
                if (rc.canAttack(r.location)) {
                    rc.attack(r.location);
                }
            }
            if (r.getType() == RobotType.SAGE) {
                if (t==null || t.getType() != RobotType.SAGE) {
                    t = r;
                }
            }
            if (r.getType() == RobotType.SOLDIER) {
                if (t==null || (t.getType() != RobotType.SAGE && t.getType() != RobotType.SOLDIER)) {
                    t = r;
                }
            }
        }
        if (t != null && rc.canAttack(t.getLocation())) {
            rc.attack(t.getLocation());
        }

        move();
    }

    public void move() throws GameActionException {
        if (goal == null) {
            for (int i = archonCount; i < archonCount*2; i++) {
                if (rc.readSharedArray(i) != 0) {
                    RobotInfo robot = decodeArchon(rc.readSharedArray(i));
                    goal = robot.getLocation();
                    moveQueue.clear();
                    if (rc.canSenseLocation(goal) && (rc.senseRobotAtLocation(goal) == null || rc.senseRobotAtLocation(goal).getID() != robot.getID())) {
                        System.out.println("Archon missing (possible destroyed");
                        rc.writeSharedArray(i, 0);
                        goal = null;
                    }
                    break;
                }
            }
        } else {
            rc.setIndicatorLine(rc.getLocation(), goal, 0, 0, 200);
        }
        if (moveQueue.isEmpty()) {
            if (goal != null) {
                pathfind(goal);
            } else {
                MapLocation a = decodeLocation(rc.readSharedArray(rng.nextInt(archonCount)));
                //choose random symmetry to try
                pathfind(new MapLocation(rng.nextBoolean()?rc.getMapWidth()- a.x:a.x, rng.nextBoolean()?rc.getMapHeight()-a.y:a.y));
            }
        }
        if (moveQueue.peek() != null) {
            if (rc.canMove(moveQueue.peek())) {
                rc.move(moveQueue.poll());
            } else if (rc.canMove(moveQueue.peek().rotateLeft())) {
                rc.move(moveQueue.poll().rotateLeft());
            } else if (rc.canMove(moveQueue.peek().rotateRight())) {
                rc.move(moveQueue.poll().rotateRight());
            }
        }
    }

}
