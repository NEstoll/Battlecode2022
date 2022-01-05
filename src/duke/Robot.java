package duke;

import battlecode.common.*;

public abstract class Robot {
    RobotController rc;

    public Robot(RobotController rc) {
        this.rc = rc;
    }

    public void run() {
        while(true) {
            //game loop, don't return except to die
            try {
                act();
            } catch (GameActionException e) {
                //game exception
                e.printStackTrace();
            }
            Clock.yield();
        }
    }

    public int encodeLocation(MapLocation l) {
        return (l.x <<6) + l.y;
    }

    public MapLocation decodeLocation(int num) {
        return new MapLocation(num >> 6 & 63, num & 63);
    }

    public int encodeArchon(RobotInfo r) {
        int num = encodeLocation(r.getLocation());
        num += r.getID() << 12;
        return num;
    }

    public RobotInfo decodeArchon(int num) {
        return new RobotInfo(num >> 12, Team.NEUTRAL, RobotType.ARCHON, null, 0, 0, decodeLocation(num));
    }

    public abstract void act() throws GameActionException;
}
