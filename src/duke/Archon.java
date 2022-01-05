package duke;

import battlecode.common.*;
import static duke.RobotPlayer.*;

public class Archon extends Building{
    public Archon(RobotController rc) {
        super(rc);
    }

    @Override
    public void act() throws GameActionException {
        if (rc.getRoundNum() == 1) {
            int loc =rc.getLocation().x << 6;
            loc += rc.getLocation().y;
            loc += rc.getLocation().x < rc.getMapWidth()/2?1 << 13:0;
            loc += rc.getLocation().y < rc.getMapHeight()/2?1 << 12:0;
            int i= -1;
            while (rc.readSharedArray(++i) != 0);
            rc.writeSharedArray(i, loc);
        }

        // Pick a direction to build in.
        Direction dir = directions[rng.nextInt(directions.length)];
        if (rng.nextBoolean()) {
            // Let's try to build a miner.
            rc.setIndicatorString("Trying to build a miner");
            if (rc.canBuildRobot(RobotType.MINER, dir)) {
                rc.buildRobot(RobotType.MINER, dir);
            }
        } else {
            // Let's try to build a soldier.
            rc.setIndicatorString("Trying to build a soldier");
            if (rc.canBuildRobot(RobotType.SOLDIER, dir)) {
                rc.buildRobot(RobotType.SOLDIER, dir);
            }
        }
    }
}
