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
            int loc = encodeLocation(rc.getLocation());
            for (int i = 0; i < archonCount; i++) {
                if (rc.readSharedArray(i) == 0) {
                    rc.writeSharedArray(i, loc);
                    break;
                }
            }
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
