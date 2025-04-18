package vcsc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Timer;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import java.util.List;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;
import vcsc.core.abstracts.state.StateRegistry;
import vcsc.core.abstracts.task.FollowPathTask;
import vcsc.core.abstracts.task.Task;
import vcsc.core.abstracts.task.TaskManager;
import vcsc.core.abstracts.task.TaskSequence;
import vcsc.core.util.GlobalTelemetry;
import vcsc.teamcode.behavior.sample.B_DepositSampleUpperAuto;
import vcsc.teamcode.behavior.sample.B_IntakeSampleGrab;
import vcsc.teamcode.behavior.sample.B_IntakeSampleHover;
import vcsc.teamcode.behavior.sample.B_IntakeSampleStraight;
import vcsc.teamcode.behavior.sample.B_LockOn;
import vcsc.teamcode.behavior.sample.B_ReleaseSampleAndPreGrabAutoShort;
import vcsc.teamcode.behavior.sample.B_ReleaseSampleAndStow;
import vcsc.teamcode.behavior.sample.B_StowSample;
import vcsc.teamcode.behavior.sample.B_StowSampleAfterIntake;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionActuator;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionState;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationActuator;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationState;
import vcsc.teamcode.cmp.arm.rotation.actions.A_SetArmRotationPose;
import vcsc.teamcode.cmp.camera.Camera;
import vcsc.teamcode.cmp.claw.ClawActuator;
import vcsc.teamcode.cmp.claw.ClawState;
import vcsc.teamcode.cmp.claw.actions.A_CloseClaw;
import vcsc.teamcode.cmp.claw.actions.A_OpenClaw;
import vcsc.teamcode.cmp.elbow.ElbowActuator;
import vcsc.teamcode.cmp.elbow.ElbowPose;
import vcsc.teamcode.cmp.elbow.ElbowState;
import vcsc.teamcode.cmp.elbow.actions.A_SetElbowPose;
import vcsc.teamcode.cmp.robot.FollowerWrapper;
import vcsc.teamcode.cmp.robot.RobotState;
import vcsc.teamcode.cmp.wrist.hinge.WristHingeActuator;
import vcsc.teamcode.cmp.wrist.hinge.WristHingeState;
import vcsc.teamcode.cmp.wrist.twist.WristTwistActuator;
import vcsc.teamcode.cmp.wrist.twist.WristTwistState;
import vcsc.teamcode.cmp.wrist.twist.actions.A_SetWristTwistAngle;
import vcsc.teamcode.config.GlobalConfig;

/**
 * This is an example auto that showcases movement and control of two servos autonomously.
 * It is a 0+4 (Specimen + Sample) bucket auto. It scores a neutral preload and then pickups 3 samples from the ground and scores them before parking.
 * There are examples of different ways to build paths.
 * A path progression method has been created and can advance based on time, position, or other factors.
 *
 * @author Baron Henderson - 20077 The Indubitables
 * @version 2.0, 11/28/2024
 */

@Autonomous(name = "Sample Auto", group = "Auto", preselectTeleOp = "Tele")
public class SampleAuto extends OpMode {

    private final Pose startPose = new Pose(6, 112, Math.toRadians(270));
    /**
     * Scoring Pose of our robot. It is facing the submersible at a -45 degree (315 degree) angle.
     */
    private final Pose scorePose = new Pose(15, 129, Math.toRadians(315)); // NOT USED FOR ANYTHING I THINK
    private final Pose scorePose1 = new Pose(12.25, 135.75, Math.toRadians(-22));
    //    private final Pose scorePose = new Pose(15, 134.0, Math.toRadians(0));
    private final Pose scorePose2 = new Pose(13.5, 140.0, Math.toRadians(-10));
    private final Pose scorePose3 = new Pose(15.75, 141.75, Math.toRadians(0));

    private final Pose scorePosePreload = new Pose(6, 129, Math.toRadians(270));
    /**
     * Lowest (First) Sample from the Spike Mark
     */
//    private final Pose pickup1Pose = new Pose(21, 120.5, Math.toRadians(0)); // 33
    private final Pose pickup1Pose = new Pose(23, 128, Math.toRadians(-18));
    /**
     * Middle (Second) Sample from the Spike Mark
     */
//    private final Pose pickup2Pose = new Pose(21, 130, Math.toRadians(0));
    private final Pose pickup2Pose = new Pose(22.3, 135, Math.toRadians(-10));
    /**
     * Highest (Third) Sample from the Spike Mark
     */
    private final Pose pickup3Pose = new Pose(22.25, 134.5, Math.toRadians(12));
    /**
     * Park Pose for our robot, after we do all of the scoring.
     */
    private final Pose submersible = new Pose(65, 102, Math.toRadians(270));
    private final Pose submersibleExit = new Pose(60, 105, Math.toRadians(270));
    /**
     * Park Control Pose for our robot, this is used to manipulate the bezier curve that we will create for the parking.
     * The Robot will not go to this pose, it is used a control point for our bezier curve.
     */
    private final Pose parkControlPose = new Pose(69, 135, Math.toRadians(270));
    protected MultipleTelemetry telem;
    protected TaskManager taskManager = TaskManager.getInstance();
    protected RobotState robotState = RobotState.getInstance();
    protected ClawState clawState;
    protected ArmExtensionState armExtState;
    protected ArmRotationState armRotState;
    protected ElbowState elbowState;
    protected WristHingeState wristHingeState;
    protected WristTwistState wristTwistState;
    /**
     * Start Pose of our robot
     */

    boolean over = false;
    /* Create and Define Poses + Paths
     * Poses are built with three constructors: x, y, and heading (in Radians).
     * Pedro uses 0 - 144 for x and y, with 0, 0 being on the bottom left.
     * (For Into the Deep, this would be Blue Observation Zone (0,0) to Red Observation Zone (144,144).)
     * Even though Pedro uses a different coordinate system than RR, you can convert any roadrunner pose by adding +72 both the x and y.
     * This visualizer is very easy to use to find and create paths/pathchains/poses: <https://pedro-path-generator.vercel.app/>
     * Lets assume our robot is 18 by 18 inches
     * Lets assume the Robot is facing the human player and we want to score in the bucket */
    ClawActuator clawActuator;
    ArmExtensionActuator armExtActuator;
    ArmRotationActuator armRotActuator;
    ElbowActuator elbowActuator;
    WristHingeActuator wristHingeActuator;
    WristTwistActuator wristTwistActuator;
    TaskSequence auto = new TaskSequence();
    List<LynxModule> allHubs;
    private Follower follower;
    private Timer opmodeTimer;
    /* These are our Paths and PathChains that we will define in buildPaths() */
    private PathChain scorePreload, park, grabPickup1, submersibleToScore, grabPickup2, grabPickup3, scorePickup1, scorePickup2, scorePickup3;

    /**
     * Build the paths for the auto (adds, for example, constant/linear headings while doing paths)
     * It is necessary to do this so that all the paths are built before the auto starts.
     **/
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
                .addPath(new BezierLine(new Point(startPose), new Point(scorePosePreload)))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePosePreload.getHeading())
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

        submersibleToScore = linearInterpolateLine(submersibleExit, scorePose);

        /* This is our park path. We are using a BezierCurve with 3 points, which is a curved line that is curved based off of the control point */
        park = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scorePose), /* Control Point */ new Point(parkControlPose), new Point(submersible)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), submersible.getHeading())
                .build();
    }

    private PathChain linearInterpolateLine(Pose start, Pose end) {
        return follower.pathBuilder().addBezierLine(
                new Point(start),
                new Point(end)
        ).setLinearHeadingInterpolation(start.getHeading(), end.getHeading()).build();
    }


    public void normalLoop() {
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }

        follower.update();
        taskManager.loop();
        clawActuator.loop();
        armExtActuator.loop();
        armRotActuator.loop();
        elbowActuator.loop();
        wristHingeActuator.loop();
        wristTwistActuator.loop();

        // Feedback to Driver Hub
        telemetry.addLine("Current tasks:");
        for (Task task : auto.getTasks()) {
            telemetry.addLine("     " + task.getClass().getSimpleName());
        }
    }

    /**
     * This is the main loop of the OpMode, it will run repeatedly after clicking "Play".
     **/
    @Override
    public void loop() {
        normalLoop();
        auto.loop();

        if (opmodeTimer.getElapsedTime() > 29600 && !over) {
            over = true;
            taskManager.clearTasks();
            auto.cancel();
            if (armExtState.getRealLength() > 60) {
                taskManager.runTask(new TaskSequence(new A_OpenClaw(), new B_StowSample()));
            } else {
                taskManager.runTask(new TaskSequence(new B_StowSample()));
            }
        }

        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }

    public void normalInit() {
        GlobalTelemetry.init(telemetry);

        telem = GlobalTelemetry.getInstance();

        allHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        StateRegistry reg = StateRegistry.getInstance();
        reg.clearStates();

        clawState = new ClawState();
        clawActuator = new ClawActuator(hardwareMap);
        clawState.registerActuator(clawActuator);

        armExtState = new ArmExtensionState();
        armExtActuator = new ArmExtensionActuator(hardwareMap, new PIDFCoefficients(0.015, 0, 0, 0));
        armExtState.registerActuator(armExtActuator);

        armRotState = new ArmRotationState();
        armRotActuator = new ArmRotationActuator(hardwareMap, GlobalConfig.rotationCoeffs);
        armRotState.registerActuator(armRotActuator);

        elbowState = new ElbowState();
        elbowActuator = new ElbowActuator(hardwareMap);
        elbowState.registerActuator(elbowActuator);

        wristHingeState = new WristHingeState();
        wristHingeActuator = new WristHingeActuator(hardwareMap);
        wristHingeState.registerActuator(wristHingeActuator);

        wristTwistState = new WristTwistState();
        wristTwistActuator = new WristTwistActuator(hardwareMap);
        wristTwistState.registerActuator(wristTwistActuator);

        Camera camera = new Camera(hardwareMap);

        reg.registerStates(clawState, armExtState, armRotState, elbowState, wristHingeState, wristTwistState, camera);
    }


    /**
     * This method is called once at the init of the OpMode.
     **/
    @Override
    public void init() {
        normalInit();
        A_CloseClaw closeClaw = new A_CloseClaw();
        A_SetArmRotationPose rotateSlidesUp = new A_SetArmRotationPose(ArmRotationPose.DEPOSIT_SAMPLE_UPPER);
        taskManager.runTask(new TaskSequence(closeClaw, rotateSlidesUp));

        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);
        FollowerWrapper.setFollower(follower);
        buildPaths();

        long DROP_DELAY = 80;
        long PRE_GRAB_DELAY = 100;
        long POST_GRAB_DELAY = 50;

        follower.setMaxPower(0.8);

        auto.thenLog("[AUTO] DepositUpper & Go to scorePreload")
                // SCORE PRELOAD
                .thenAsync(new B_DepositSampleUpperAuto(), new FollowPathTask(follower, scorePreload))
                .thenWaitUntil(() -> follower.getPose().getY() > 123 && armExtState.idle() || !follower.isBusy())
                .thenDelay(DROP_DELAY)

                // GRAB PICKUP 1
                .thenLog("[AUTO] (Stow then Intake) & Go to grabPickup1")
                .then(new B_ReleaseSampleAndPreGrabAutoShort(), new A_SetWristTwistAngle(0.38), new FollowPathTask(follower, grabPickup1))
                .thenDelay(PRE_GRAB_DELAY)
                .thenLog("[AUTO] Grab Sample")
                .then(new B_IntakeSampleGrab())
                .thenDelay(POST_GRAB_DELAY)

                // SCORE PICKUP 1
                .thenLog("[AUTO] Deposit Upper & Go to scorePickup1")
                .then(new B_DepositSampleUpperAuto(), new FollowPathTask(follower, scorePickup1))
                .thenDelay(DROP_DELAY)

                // GRAB PICKUP 2
                .thenLog("[AUTO] (Stow then Intake) & Go to grabPickup2")
                .then(new B_ReleaseSampleAndPreGrabAutoShort(), new FollowPathTask(follower, grabPickup2))
                .thenDelay(PRE_GRAB_DELAY)
                .thenLog("[AUTO] Grab Sample")
                .then(new B_IntakeSampleGrab())
                .thenDelay(POST_GRAB_DELAY)

                // SCORE PICKUP 2
                .thenLog("[AUTO] Deposit Upper & Go to scorePickup2")
                .then(new B_DepositSampleUpperAuto(), new FollowPathTask(follower, scorePickup2))
                .thenDelay(DROP_DELAY)

                // GRAB PICKUP 3
                .thenLog("[AUTO] (Stow then Intake) & Go to grabPickup3")
                .then(new B_ReleaseSampleAndStow(), new A_SetWristTwistAngle(0.42), new FollowPathTask(follower, grabPickup3))
                .then(new B_IntakeSampleHover())
                .thenDelay(80)
                .thenLog("[AUTO] Grab Sample")
                .then(new B_IntakeSampleGrab())
                .thenDelay(300)

                // SCORE PICKUP 3
                .thenLog("[AUTO] Deposit Upper & Go to scorePickup3")
                .then(new B_DepositSampleUpperAuto(), new FollowPathTask(follower, scorePickup3))
                .thenDelay(DROP_DELAY)

                // GO TO SUBMERSIBLE
                .thenLog("[AUTO] (Stow then Intake) & Go to park")
                .thenAsync(new TaskSequence(new B_ReleaseSampleAndStow()), new FollowPathTask(follower, park))
                .thenWaitUntil(() -> follower.getPose().getY() < 120)
                .then(new B_IntakeSampleStraight())

                // Intake from submersible
//                .then(new B_IntakeSampleStraight())
                .then(new B_LockOn())
                .then(new B_StowSampleAfterIntake())
                .thenAsync(new B_DepositSampleUpperAuto())
                .thenFollowPath(follower, submersibleToScore)
                .thenDelay(DROP_DELAY)
                .thenAsync(new TaskSequence(new B_ReleaseSampleAndStow()), new FollowPathTask(follower, park))
                .thenWaitUntil(() -> follower.getPose().getY() < 120)
                .then(new B_IntakeSampleStraight())

//                .then(new B_IntakeSampleStraight())
                .then(new B_LockOn())
                .then(new B_StowSampleAfterIntake())
                .thenAsync(new B_DepositSampleUpperAuto())
                .thenFollowPath(follower, submersibleToScore)
                .thenDelay(DROP_DELAY)
                .thenAsync(new TaskSequence(new B_ReleaseSampleAndStow()), new FollowPathTask(follower, park))
                .thenWaitUntil(() -> follower.getPose().getY() < 120)
                .then(new B_IntakeSampleStraight())

//                .then(new B_IntakeSampleStraight())
                .then(new B_LockOn())
                .then(new B_StowSampleAfterIntake())
                .thenAsync(new B_DepositSampleUpperAuto())
                .thenFollowPath(follower, submersibleToScore)
                .thenDelay(DROP_DELAY)
                .then(new B_ReleaseSampleAndStow(), new FollowPathTask(follower, park));
    }

    /**
     * This method is called continuously after Init while waiting for "play".
     **/
    @Override
    public void init_loop() {
        armRotActuator.loop();
        clawActuator.loop();
        taskManager.loop();
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }
    }

    /**
     * This method is called once at the start of the OpMode.
     * It runs all the setup actions, including building paths and starting the path system
     **/
    @Override
    public void start() {
        opmodeTimer.resetTimer();
        A_SetElbowPose stowElbow = new A_SetElbowPose(ElbowPose.STOW_SAMPLE);
        taskManager.runTask(stowElbow);
        auto.start();
    }

    /**
     * We do not use this because everything should automatically disable
     **/
    @Override
    public void stop() {
    }
}

