package duke;

import battlecode.common.*;

import java.util.Random;

/**
 * RobotPlayer is the class that describes your main robot strategy.
 * The run() method inside this class is like your main function: this is what we'll call once your robot
 * is created!
 */
public strictfp class RobotPlayer {

    /**
     * We will use this variable to count the number of turns this robot has been alive.
     * You can use static variables like this to save any information you want. Keep in mind that even though
     * these variables are static, in Battlecode they aren't actually shared between your robots.
     */
    static int turnCount = 0;

    /**
     * A random number generator.
     * We will use this RNG to make some random moves. The Random class is provided by the java.util.Random
     * import at the top of this file. Here, we *seed* the RNG with a constant number (6147); this makes sure
     * we get the same sequence of numbers every time this code is run. This is very useful for debugging!
     */
    static final Random rng = new Random(6147);

    /**
     * Array containing all the possible movement directions.
     */
    static final Direction[] directions = {
            Direction.NORTH,
            Direction.NORTHEAST,
            Direction.EAST,
            Direction.SOUTHEAST,
            Direction.SOUTH,
            Direction.SOUTHWEST,
            Direction.WEST,
            Direction.NORTHWEST,
    };

    /**
     * run() is the method that is called when a robot is instantiated in the Battlecode world.
     * It is like the main function for your robot. If this method returns, the robot dies!
     *
     * @param rc The RobotController object. You use it to perform actions from this robot, and to get
     *           information on its current status. Essentially your portal to interacting with the world.
     **/
    @SuppressWarnings("unused")
    public static void run(RobotController rc) throws GameActionException {

        Robot me = null;

        switch (rc.getType()) {
            case ARCHON:
                me = new Archon(rc);
                break;
            case MINER:
                me = new Miner(rc);
                break;
            case SOLDIER:
                me = new Soldier(rc);
                break;
            case LABORATORY:
                me = new Laboratory(rc);
                break;
            case WATCHTOWER:
                me = new Watchtower(rc);
                break;
            case BUILDER:
                me = new Builder(rc);
                break;
            case SAGE:
                me = new Sage(rc);
                break;
        }
        while (true) {
            try {
                me.run();
            } catch (Exception e) {
                //uncaught exception
                e.printStackTrace();
                rc.resign();
            }
        }
        // Your code should never reach here (unless it's intentional)! Self-destruction imminent...
    }
}
