package vcsc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
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
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;
import vcsc.core.abstracts.state.StateRegistry;
import vcsc.core.abstracts.task.FollowPathTask;
import vcsc.core.abstracts.task.Task;
import vcsc.core.abstracts.task.TaskManager;
import vcsc.core.abstracts.task.TaskSequence;
import vcsc.core.util.GlobalTelemetry;
import vcsc.teamcode.behavior.sample.B_IntakeSample;
import vcsc.teamcode.behavior.sample.B_IntakeSampleGrab;
import vcsc.teamcode.behavior.specimen.B_DepositSpecimenPose;
import vcsc.teamcode.behavior.specimen.B_GrabSpecimenAndStow;
import vcsc.teamcode.behavior.specimen.B_IntakeSpecimen;
import vcsc.teamcode.behavior.specimen.B_ReleaseSpecimenAndIntakeSpecimen;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionActuator;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionState;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationActuator;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationState;
import vcsc.teamcode.cmp.arm.rotation.actions.A_SetArmRotationPose;
import vcsc.teamcode.cmp.claw.ClawActuator;
import vcsc.teamcode.cmp.claw.ClawState;
import vcsc.teamcode.cmp.claw.actions.A_CloseClaw;
import vcsc.teamcode.cmp.elbow.ElbowActuator;
import vcsc.teamcode.cmp.elbow.ElbowPose;
import vcsc.teamcode.cmp.elbow.ElbowState;
import vcsc.teamcode.cmp.elbow.actions.A_SetElbowPose;
import vcsc.teamcode.cmp.robot.FollowerWrapper;
import vcsc.teamcode.cmp.robot.RobotState;
import vcsc.teamcode.cmp.wrist.hinge.WristHingeActuator;
import vcsc.teamcode.cmp.wrist.hinge.WristHingePose;
import vcsc.teamcode.cmp.wrist.hinge.WristHingeState;
import vcsc.teamcode.cmp.wrist.hinge.actions.A_SetWristHingePose;
import vcsc.teamcode.cmp.wrist.twist.WristTwistActuator;
import vcsc.teamcode.cmp.wrist.twist.WristTwistPose;
import vcsc.teamcode.cmp.wrist.twist.WristTwistState;
import vcsc.teamcode.cmp.wrist.twist.actions.A_SetWristTwistPose;
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

@Autonomous(name = "Specimen Auto", group = "Auto Test")
public class SpecimenAuto extends OpMode {

    private Follower follower;
    private Timer opmodeTimer;

    protected MultipleTelemetry telem;
    protected TaskManager taskManager = TaskManager.getInstance();
    protected RobotState robotState = RobotState.getInstance();
    protected ClawState clawState;
    protected ArmExtensionState armExtState;
    protected ArmRotationState armRotState;
    protected ElbowState elbowState;
    protected WristHingeState wristHingeState;
    protected WristTwistState wristTwistState;
    ClawActuator clawActuator;
    ArmExtensionActuator armExtActuator;
    ArmRotationActuator armRotActuator;
    ElbowActuator elbowActuator;
    WristHingeActuator wristHingeActuator;
    WristTwistActuator wristTwistActuator;


    /* Create and Define Poses + Paths
     * Poses are built with three constructors: x, y, and heading (in Radians).
     * Pedro uses 0 - 144 for x and y, with 0, 0 being on the bottom left.
     * (For Into the Deep, this would be Blue Observation Zone (0,0) to Red Observation Zone (144,144).)
     * Even though Pedro uses a different coordinate system than RR, you can convert any roadrunner pose by adding +72 both the x and y.
     * This visualizer is very easy to use to find and create paths/pathchains/poses: <https://pedro-path-generator.vercel.app/>
     * Lets assume our robot is 18 by 18 inches
     * Lets assume the Robot is facing the human player and we want to score in the bucket */

    private final double SCORE_X = 40;
    private final double SCORE_Y_INITIAL = 64;
    private final double SCORE_SPACING = 2;

    private int specimenNum = 0;

    /** Start Pose of our robot */
    private final Pose startPose = new Pose(7, 66, Math.toRadians(0));
    private final Pose scorePosePreload = new Pose(startPose.getX(), 126, Math.toRadians(270));

    private final Pose grabSpikeMark1Pose = new Pose(24, 35, Math.toRadians(325));
    private final Pose grabSpikeMark1ControlPoint = new Pose(24, 56, Math.toRadians(325));
    private final Pose grabSpikeMark2Pose = new Pose(24, 24, Math.toRadians(325));
    private final Pose dropSpikeMarkPose = new Pose(grabSpikeMark2Pose.getX(), grabSpikeMark2Pose.getY(), Math.toRadians(325));

    private final Pose setupPushPose = new Pose(58, 6.25, Math.toRadians(0));
    private final Pose setupPushControlPoint = new Pose(50, 24, Math.toRadians(0));
    private final Pose pushedPose = new Pose(21, setupPushPose.getY(), Math.toRadians(0));

    private final Pose intakePose = new Pose(7, 33, Math.toRadians(0));
    private final Pose intake1ControlPoint = new Pose(25, 26, Math.toRadians(0));


    /* These are our Paths and PathChains that we will define in buildPaths() */
    PathChain ONE_spikeMark1Path, TWO_spikeMark1Drop, THREE_spikeMark2Path, FOUR_spikeMark2Drop, FIVE_pushPath, pushPop;

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
        ONE_spikeMark1Path = follower.pathBuilder()
                .addPath(new BezierCurve(
                        startPose,
                        grabSpikeMark1ControlPoint,
                        grabSpikeMark1Pose
                )).setLinearHeadingInterpolation(startPose.getHeading(), grabSpikeMark1Pose.getHeading()).build();

        TWO_spikeMark1Drop = linearInterpolateLine(grabSpikeMark1Pose, dropSpikeMarkPose);
        THREE_spikeMark2Path = linearInterpolateLine(dropSpikeMarkPose, grabSpikeMark2Pose);
        FOUR_spikeMark2Drop = linearInterpolateLine(grabSpikeMark2Pose, dropSpikeMarkPose);

            pushPop = follower.pathBuilder()
//                        .addPath(
//                                // Line 1
//                                new BezierLine(
//                                        new Point(7.000, 66.000, Point.CARTESIAN),
//                                        new Point(45.000, 64.000, Point.CARTESIAN)
//                                )
//                        )
//                        .setConstantHeadingInterpolation(Math.toRadians(180))
//                        .addPath(
//                                // Line 2
//                                new BezierCurve(
//                                        new Point(45.000, 64.000, Point.CARTESIAN),
//                                        new Point(24.000, 56.000, Point.CARTESIAN),
//                                        new Point(23.883, 35.298, Point.CARTESIAN)
//                                )
//                        )
//                        .setConstantHeadingInterpolation(Math.toRadians(180))
                    .addPath(new BezierCurve(
                new Point(23.883, 35.298, Point.CARTESIAN),
                new Point(101.678, 27.922, Point.CARTESIAN),
                new Point(24.234, 23.707, Point.CARTESIAN)
        )
      )
      .setConstantHeadingInterpolation(Math.toRadians(180))
                .addPath(
                        // Line 4
                        new BezierCurve(
                                new Point(24.234, 23.707, Point.CARTESIAN),
                                new Point(106.946, 21.249, Point.CARTESIAN),
                                new Point(23.532, 13.171, Point.CARTESIAN)
                        )
                )
                    .setConstantHeadingInterpolation(Math.toRadians(180))
                    .addPath(
                            new BezierCurve(
                                    new Point(23.532, 13.171, Point.CARTESIAN),
                                    new Point(108.527, 8.429, Point.CARTESIAN),
                                    new Point(24.410, 9.307, Point.CARTESIAN)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(180)).build();
        Path setupPush = new Path(
                new BezierCurve(
                        dropSpikeMarkPose,
                        setupPushControlPoint,
                        setupPushPose
                )
        );

        setupPush.setLinearHeadingInterpolation(dropSpikeMarkPose.getHeading(), setupPushPose.getHeading());

        Path pushPathSegment = new Path(
                new BezierCurve(
                        setupPushPose,
                        pushedPose
                )
        );

        pushPathSegment.setLinearHeadingInterpolation(setupPushPose.getHeading(), pushedPose.getHeading());

        Path setupIntake1 = new Path(
                new BezierCurve(
                        pushedPose,
                        intake1ControlPoint,
                        intakePose
                )
        );

        setupIntake1.setLinearHeadingInterpolation(pushedPose.getHeading(), intakePose.getHeading());

        FIVE_pushPath = follower.pathBuilder()
                .addPath(setupPush)
                .addPath(pushPathSegment)
                .addPath(setupIntake1)
                .build();
    }

    private PathChain linearInterpolateLine(Pose start, Pose end) {
        return follower.pathBuilder().addBezierLine(
                new Point(start),
                new Point(end)
        ).setLinearHeadingInterpolation(start.getHeading(), end.getHeading()).build();
    }

    public Pose getSpecimenScorePose(int specimenNum) {
        return new Pose(SCORE_X, SCORE_Y_INITIAL + (SCORE_SPACING * specimenNum), Math.toRadians(0));
    }

    public PathChain getSpecimenScorePathChain(int specimenNum, Pose startPose) {
        return follower.pathBuilder().addPath(new BezierLine(
                startPose,
                getSpecimenScorePose(specimenNum)
        )).setConstantHeadingInterpolation(Math.toRadians(0)).build();
    }

    public PathChain getSpecimenScorePathChain(Pose startPose) {
        return getSpecimenScorePathChain(specimenNum++, startPose);
    }

    public PathChain getSpecimenScorePathChain() {
        return getSpecimenScorePathChain(specimenNum++, intakePose);
    }

    public PathChain getIntakePathChain() {
        return follower.pathBuilder().addPath(new BezierLine(
                getSpecimenScorePose(specimenNum),
                intakePose
        )).setConstantHeadingInterpolation(Math.toRadians(0)).build();
    }

    public FollowPathTask scoreSpecimenFollowPathTask(Pose startPose) {
        return new FollowPathTask(follower, getSpecimenScorePathChain(startPose));
    }
    public FollowPathTask scoreSpecimenFollowPathTask() {
        return new FollowPathTask(follower, getSpecimenScorePathChain());
    }


    public void normalLoop() {
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

    /** This is the main loop of the OpMode, it will run repeatedly after clicking "Play". **/
    @Override
    public void loop() {
        normalLoop();
        auto.loop();

        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }

    public void normalInit() {
        GlobalTelemetry.init(telemetry);

        telem = GlobalTelemetry.getInstance();

        StateRegistry reg = StateRegistry.getInstance();
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

        reg.registerStates(clawState, armExtState, armRotState, elbowState, wristHingeState, wristTwistState);
    }

    public TaskSequence scoreSpecimen() {
        return new TaskSequence(scoreSpecimenFollowPathTask(), new B_DepositSpecimenPose())
                .then(new B_ReleaseSpecimenAndIntakeSpecimen());
    }

    public TaskSequence intakeSpecimen() {
        return new TaskSequence(new B_IntakeSpecimen(), new FollowPathTask(follower, getIntakePathChain()))
                .thenDelay(500)
                .then(new B_GrabSpecimenAndStow()).thenDelay(150);
    }

    public TaskSequence specimenLoop(int count) {
        TaskSequence sequence = new TaskSequence();
        for (int i = 0; i < count; i++) {
            sequence.then(scoreSpecimen())
                    .then(intakeSpecimen());
        }
        return sequence;
    }

    /** This method is called once at the init of the OpMode. **/
    @Override
    public void init() {
        normalInit();
        A_CloseClaw closeClaw = new A_CloseClaw();
        A_SetArmRotationPose rotateSlidesUp = new A_SetArmRotationPose(ArmRotationPose.DEPOSIT_SPECIMEN);
        A_SetElbowPose elbowOut = new A_SetElbowPose(ElbowPose.DEPOSIT_SPECIMEN);
        A_SetWristHingePose wristHingeOut = new A_SetWristHingePose(WristHingePose.DEPOSIT_SPECIMEN);
        A_SetWristTwistPose wristTwistOut = new A_SetWristTwistPose(WristTwistPose.DEPOSIT_SPECIMEN);
        taskManager.runTask(new TaskSequence(closeClaw)
            .thenDelay(200)
            .then(rotateSlidesUp, elbowOut, wristHingeOut, wristTwistOut));
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);
        FollowerWrapper.setFollower(follower);
        buildPaths();

        auto.then(new B_DepositSpecimenPose(), scoreSpecimenFollowPathTask(startPose))
                .then(new B_IntakeSpecimen(), new FollowPathTask(follower, pushPop))
                .thenDelay(500)
                .then(new B_GrabSpecimenAndStow())
                .then(specimenLoop(4));
    }

    /** This method is called continuously after Init while waiting for "play". **/
    @Override
    public void init_loop() {
        elbowActuator.loop();
        wristHingeActuator.loop();
        wristTwistActuator.loop();
        armRotActuator.loop();
        clawActuator.loop();
        taskManager.loop();
    }

    /** This method is called once at the start of the OpMode.
     * It runs all the setup actions, including building paths and starting the path system **/
    @Override
    public void start() {
        opmodeTimer.resetTimer();
        A_SetElbowPose stowElbow = new A_SetElbowPose(ElbowPose.STOW_SAMPLE);
        taskManager.runTask(stowElbow);
        auto.start();
    }

    /** We do not use this because everything should automatically disable **/
    @Override
    public void stop() {
    }
}

