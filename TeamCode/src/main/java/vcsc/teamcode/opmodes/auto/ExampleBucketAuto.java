package vcsc.teamcode.opmodes.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;
import vcsc.core.abstracts.task.FollowPathTask;
import vcsc.core.abstracts.task.Task;
import vcsc.core.abstracts.task.TaskSequence;
import vcsc.teamcode.behavior.sample.B_DepositSampleUpper;
import vcsc.teamcode.behavior.sample.B_IntakeSample;
import vcsc.teamcode.behavior.sample.B_IntakeSampleGrab;
import vcsc.teamcode.behavior.sample.B_ReleaseSampleAndStow;

/**
 * This is an example auto that showcases movement and control of two servos autonomously.
 * It is a 0+4 (Specimen + Sample) bucket auto. It scores a neutral preload and then pickups 3 samples from the ground and scores them before parking.
 * There are examples of different ways to build paths.
 * A path progression method has been created and can advance based on time, position, or other factors.
 *
 * @author Baron Henderson - 20077 The Indubitables
 * @version 2.0, 11/28/2024
 */

@Autonomous(name = "Test Auto", group = "Auto Test")
public class ExampleBucketAuto extends OpMode {

    private Follower follower;
    private Timer opmodeTimer;


    /* Create and Define Poses + Paths
     * Poses are built with three constructors: x, y, and heading (in Radians).
     * Pedro uses 0 - 144 for x and y, with 0, 0 being on the bottom left.
     * (For Into the Deep, this would be Blue Observation Zone (0,0) to Red Observation Zone (144,144).)
     * Even though Pedro uses a different coordinate system than RR, you can convert any roadrunner pose by adding +72 both the x and y.
     * This visualizer is very easy to use to find and create paths/pathchains/poses: <https://pedro-path-generator.vercel.app/>
     * Lets assume our robot is 18 by 18 inches
     * Lets assume the Robot is facing the human player and we want to score in the bucket */

    /** Start Pose of our robot */
    private final Pose startPose = new Pose(9, 111, Math.toRadians(270));

    /** Scoring Pose of our robot. It is facing the submersible at a -45 degree (315 degree) angle. */
    private final Pose scorePose = new Pose(14, 129, Math.toRadians(315));

    /** Lowest (First) Sample from the Spike Mark */
    private final Pose pickup1Pose = new Pose(37, 121, Math.toRadians(0));

    /** Middle (Second) Sample from the Spike Mark */
    private final Pose pickup2Pose = new Pose(43, 130, Math.toRadians(0));

    /** Highest (Third) Sample from the Spike Mark */
    private final Pose pickup3Pose = new Pose(49, 135, Math.toRadians(0));

    /** Park Pose for our robot, after we do all of the scoring. */
    private final Pose parkPose = new Pose(60, 98, Math.toRadians(90));

    /** Park Control Pose for our robot, this is used to manipulate the bezier curve that we will create for the parking.
     * The Robot will not go to this pose, it is used a control point for our bezier curve. */
    private final Pose parkControlPose = new Pose(60, 98, Math.toRadians(90));

    /* These are our Paths and PathChains that we will define in buildPaths() */
    private PathChain scorePreload, park, grabPickup1, grabPickup2, grabPickup3, scorePickup1, scorePickup2, scorePickup3;

    TaskSequence auto = new TaskSequence();

    /** Build the paths for the auto (adds, for example, constant/linear headings while doing paths)
     * It is necessary to do this so that all the paths are built before the auto starts. **/
    public void buildPaths() {

        /* There are two major types of paths components: BezierCurves and BezierLines.
         *    * BezierCurves are curved, and require >= 3 points. There are the start and end points, and the control points.
         *    - Control points manipulate the curve between the start and end points.
         *    - A good visualizer for this is [this](https://pedro-path-generator.vercel.app/).
         *    * BezierLines are straight, and require 2 points. There are the start and end points.
         * Paths have can have heading interpolation: Constant, Linear, or Tangential
         *    * Linear heading interpolation:
         *    - Pedro will slowly change the heading of the robot from the startHeading to the endHeading over the course of the entire path.
         *    * Constant Heading Interpolation:
         *    - Pedro will maintain one heading throughout the entire path.
         *    * Tangential Heading Interpolation:
         *    - Pedro will follows the angle of the path such that the robot is always driving forward when it follows the path.
         * PathChains hold Path(s) within it and are able to hold their end point, meaning that they will holdPoint until another path is followed.
         * Here is a explanation of the difference between Paths and PathChains <https://pedropathing.com/commonissues/pathtopathchain.html> */

        /* This is our scorePreload path. We are using a BezierLine, which is a straight line. */
        scorePreload = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(scorePose)))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading())
                .build();

        /* Here is an example for Constant Interpolation
        scorePreload.setConstantInterpolation(startPose.getHeading()); */

        /* This is our grabPickup1 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        grabPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(pickup1Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading())
                .build();

        /* This is our scorePickup1 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        scorePickup1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup1Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(pickup1Pose.getHeading(), scorePose.getHeading())
                .build();

        /* This is our grabPickup2 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        grabPickup2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(pickup2Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup2Pose.getHeading())
                .build();

        /* This is our scorePickup2 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        scorePickup2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup2Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(pickup2Pose.getHeading(), scorePose.getHeading())
                .build();

        /* This is our grabPickup3 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        grabPickup3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(pickup3Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup3Pose.getHeading())
                .build();

        /* This is our scorePickup3 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        scorePickup3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup3Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(pickup3Pose.getHeading(), scorePose.getHeading())
                .build();

        /* This is our park path. We are using a BezierCurve with 3 points, which is a curved line that is curved based off of the control point */
        park = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scorePose), /* Control Point */ new Point(parkControlPose), new Point(parkPose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), parkPose.getHeading())
                .build();
    }



    /** This is the main loop of the OpMode, it will run repeatedly after clicking "Play". **/
    @Override
    public void loop() {
        // These loop the movements of the robot
        follower.update();

        auto.loop();

        // Feedback to Driver Hub
        telemetry.addLine("Current tasks:");
        for (Task task : auto.getTasks()) {
            telemetry.addLine("     " + task.getClass().getSimpleName());
        }
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }

    /** This method is called once at the init of the OpMode. **/
    @Override
    public void init() {
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);
        buildPaths();

        auto.thenLog("[AUTO] DepositUpper & Go to scorePreload")
                // SCORE PRELOAD
                .then(new B_DepositSampleUpper(), new FollowPathTask(follower, scorePreload))
                .thenDelay(1000)

                // GRAB PICKUP 1
                .thenLog("[AUTO] (Stow then Intake) & Go to grabPickup1")
                .then(new TaskSequence(new B_ReleaseSampleAndStow(), new B_IntakeSample()), new FollowPathTask(follower, grabPickup1))
                .thenLog("[AUTO] Grab Sample")
                .then(new B_IntakeSampleGrab())

                // SCORE PICKUP 1
                .thenLog("[AUTO] Deposit Upper & Go to scorePickup1")
                .then(new B_DepositSampleUpper(), new FollowPathTask(follower, scorePickup1))
                .thenDelay(1000)

                // GRAB PICKUP 2
                .thenLog("[AUTO] (Stow then Intake) & Go to grabPickup2")
                .then(new TaskSequence(new B_ReleaseSampleAndStow(), new B_IntakeSample()), new FollowPathTask(follower, grabPickup2))
                .thenLog("[AUTO] Grab Sample")
                .then(new B_IntakeSampleGrab())

                // SCORE PICKUP 2
                .thenLog("[AUTO] Deposit Upper & Go to scorePickup2")
                .then(new B_DepositSampleUpper(), new FollowPathTask(follower, scorePickup2))
                .thenDelay(1000)

                // GRAB PICKUP 3
                .thenLog("[AUTO] (Stow then Intake) & Go to grabPickup3")
                .then(new TaskSequence(new B_ReleaseSampleAndStow(), new B_IntakeSample()), new FollowPathTask(follower, grabPickup3))
                .thenLog("[AUTO] Grab Sample")
                .then(new B_IntakeSampleGrab())

                // SCORE PICKUP 3
                .thenLog("[AUTO] Deposit Upper & Go to scorePickup3")
                .then(new B_DepositSampleUpper(), new FollowPathTask(follower, scorePickup3))
                .thenDelay(1000)

                // PARK
                .thenLog("[AUTO] (Stow then Intake) & Go to park")
                .then(new B_ReleaseSampleAndStow(), new FollowPathTask(follower, park));
    }

    /** This method is called continuously after Init while waiting for "play". **/
    @Override
    public void init_loop() {}

    /** This method is called once at the start of the OpMode.
     * It runs all the setup actions, including building paths and starting the path system **/
    @Override
    public void start() {
        opmodeTimer.resetTimer();
        auto.start();
    }

    /** We do not use this because everything should automatically disable **/
    @Override
    public void stop() {
    }
}

