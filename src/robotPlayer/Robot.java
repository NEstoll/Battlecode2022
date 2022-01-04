package robotPlayer;

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

    public abstract void act() throws GameActionException;
}
