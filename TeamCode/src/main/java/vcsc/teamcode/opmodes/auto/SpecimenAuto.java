package vcsc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Timer;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import java.util.List;

import pedroPathing.constants.FConstantsSpecimenAuto;
import pedroPathing.constants.LConstants;
import vcsc.core.abstracts.state.StateRegistry;
import vcsc.core.abstracts.task.DelayTask;
import vcsc.core.abstracts.task.FollowPathTask;
import vcsc.core.abstracts.task.Task;
import vcsc.core.abstracts.task.TaskManager;
import vcsc.core.abstracts.task.TaskSequence;
import vcsc.core.util.GlobalTelemetry;
import vcsc.teamcode.behavior.sample.B_StowSample;
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

    private static final double SCORE_X = 40.75;
    private static final double SCORE_Y_INITIAL = 80;
    private static final double SCORE_SPACING = -1;
    private static final double PUSH_X = 24;
    private static final double PRE_PUSH_X = 57;
    /**
     * Start Pose of our robot
     */
    private final Pose startPose = new Pose(7, 66, Math.toRadians(0));
    private final Pose intakePose = new Pose(7, 33, Math.toRadians(0));
    private final Pose preSprint = new Pose(8, 33, Math.toRadians(270));
    private final Pose push1Pose = new Pose(PUSH_X, 24, Math.toRadians(0));
    private final Pose push2Pose = new Pose(PUSH_X, 14, Math.toRadians(0));
    private final Pose prePush2Pose = new Pose(PRE_PUSH_X, 14, Math.toRadians(0));
    private final Pose push3Pose = new Pose(PUSH_X, 9.1, Math.toRadians(0));
    private final Pose prePush3Pose = new Pose(PRE_PUSH_X, 9.3, Math.toRadians(0));
    private final Pose basketScorePose = new Pose(8, 135, Math.toRadians(270));
    private final Pose parkPose = new Pose(7, 33, Math.toRadians(90));
    protected MultipleTelemetry telem;
    protected TaskManager taskManager = TaskManager.getInstance();
    protected RobotState robotState = RobotState.getInstance();
    protected ClawState clawState;


    /* Create and Define Poses + Paths
     * Poses are built with three constructors: x, y, and heading (in Radians).
     * Pedro uses 0 - 144 for x and y, with 0, 0 being on the bottom left.
     * (For Into the Deep, this would be Blue Observation Zone (0,0) to Red Observation Zone (144,144).)
     * Even though Pedro uses a different coordinate system than RR, you can convert any roadrunner pose by adding +72 both the x and y.
     * This visualizer is very easy to use to find and create paths/pathchains/poses: <https://pedro-path-generator.vercel.app/>
     * Lets assume our robot is 18 by 18 inches
     * Lets assume the Robot is facing the human player and we want to score in the bucket */
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
    /* These are our Paths and PathChains that we will define in buildPaths() */
    PathChain pushPop, intakeToBasket, intakeToPark;
    TaskSequence auto = new TaskSequence();
    List<LynxModule> allHubs;
    PathChain sprint, sprintTurn;
    PIDFController headingController;
    boolean sprinting = false;
    private Follower follower;
    private Timer opmodeTimer;
    private int specimenNum = 0;
    private boolean over = false;

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

        Path push1 = new Path(
                new BezierCurve(
                        new Point(getSpecimenScorePose(0)),
                        new Point(26, 35, Point.CARTESIAN),
                        new Point(28, 30, Point.CARTESIAN),
                        new Point(105, 28, Point.CARTESIAN),
                        new Point(push1Pose)
                ));

        Path prePush2 = new Path(
                new BezierCurve(
                        new Point(push1Pose),
                        new Point(58, 26, Point.CARTESIAN),
                        new Point(prePush2Pose)
                ));

        Path push2 = linearInterpolateLinePath(prePush2Pose, push2Pose);

        Path prePush3 = new Path(
                new BezierCurve(
                        new Point(push2Pose),
                        new Point(60, 15, Point.CARTESIAN),
                        new Point(prePush3Pose)
                ));

        Path push3 = linearInterpolateLinePath(prePush3Pose, push3Pose);

        Path push3ToIntake = linearInterpolateLinePath(push3Pose, intakePose);

        pushPop = follower.pathBuilder()
                .addPath(push1)
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(prePush2)
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(push2)
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(prePush3)
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(push3)
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(push3ToIntake)
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        intakeToBasket = linearInterpolateLine(intakePose, basketScorePose);

        sprint = follower.pathBuilder()
                .addPath(linearInterpolateLinePath(intakePose, preSprint))
                .addPath(linearInterpolateLinePath(preSprint, basketScorePose)).build();

        sprintTurn = linearInterpolateLine(intakePose, preSprint);

        intakeToPark = linearInterpolateLine(intakePose, parkPose);
    }

    private PathChain linearInterpolateLine(Pose start, Pose end) {
        return follower.pathBuilder().addBezierLine(
                new Point(start),
                new Point(end)
        ).setLinearHeadingInterpolation(start.getHeading(), end.getHeading()).build();
    }

    private Path linearInterpolateLinePath(Pose start, Pose end) {
        Path path = new Path(new BezierLine(
                new Point(start),
                new Point(end)
        ));
        path.setLinearHeadingInterpolation(start.getHeading(), end.getHeading());
        return path;
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

        System.out.println("===================================");
        System.out.println("Overall States:");
        System.out.println("Robot Mode: " + robotState.getMode());
        System.out.println("Claw Pose: " + clawState.getPose());
        System.out.println("Arm Extension Pose: " + armExtState.getTargetPose());
        System.out.println("Ext actuator touch sensor: " + armExtActuator.isTouching());
        System.out.println("Arm Rotation Pose: " + armRotState.getTargetPose());
        System.out.println("Elbow Pose: " + elbowState.getPose());
        System.out.println("Wrist Hinge Pose: " + wristHingeState.getPose());
        System.out.println("Wrist Twist Pose: " + wristTwistState.getPose());
        System.out.println("---------- Task Manager -------------");

        // Feedback to Driver Hub
        telemetry.addLine("Current tasks:");
        for (Task task : auto.getTasks()) {
            telemetry.addLine("     " + task.getClass().getSimpleName());
        }

        if (opmodeTimer.getElapsedTime() > 29750 && !over) {
            over = true;
//            taskManager.clearTasks();
            taskManager.runTask(new TaskSequence(new B_StowSample()), true);
        }
    }

    /**
     * This is the main loop of the OpMode, it will run repeatedly after clicking "Play".
     **/
    @Override
    public void loop() {
        normalLoop();
//        auto.loop();

        if (sprinting) {
            follower.setTeleOpMovementVectors(-1, 0, headingController.calculate(follower.getPose().getHeading()));
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

        reg.registerStates(clawState, armExtState, armRotState, elbowState, wristHingeState, wristTwistState);
    }

    public TaskSequence scoreSpecimen(Pose startPose) {
        DelayTask delay = new DelayTask(1200);
        B_DepositSpecimenPose depositSpecimenPose = new B_DepositSpecimenPose();
        FollowPathTask followPathTask = scoreSpecimenFollowPathTask(startPose);
        return new TaskSequence()
                .thenAsync(followPathTask, depositSpecimenPose, delay)
                .thenWaitUntil(() -> (delay.isFinished() && follower.getPose().getX() > SCORE_X - 1.5) || followPathTask.isFinished())
                .thenAsync(new B_ReleaseSpecimenAndIntakeSpecimen()).thenDelay(100);
    }

    public TaskSequence scoreSpecimen() {
        return scoreSpecimen(intakePose);
    }

    public TaskSequence intakeSpecimen() {
        return new TaskSequence()
                .thenAsync(new B_IntakeSpecimen(), new FollowPathTask(follower, getIntakePathChain()))
                .thenWaitUntil(() -> follower.getPose().getX() < intakePose.getX() + 1 && follower.getVelocity().getXComponent() < 0.2 || !follower.isBusy())
                .thenAsync(new B_GrabSpecimenAndStow()).thenDelay(100);
    }

    public TaskSequence specimenLoop(int count) {
        TaskSequence sequence = new TaskSequence();
        for (int i = 0; i < count; i++) {
            sequence.then(scoreSpecimen())
                    .then(intakeSpecimen());
        }
        return sequence;
    }

    /**
     * This method is called once at the init of the OpMode.
     **/
    @Override
    public void init() {
        normalInit();
        A_CloseClaw closeClaw = new A_CloseClaw();
        A_SetArmRotationPose rotateSlidesUp = new A_SetArmRotationPose(ArmRotationPose.DEPOSIT_SPECIMEN);
        A_SetElbowPose elbowOut = new A_SetElbowPose(ElbowPose.DEPOSIT_SPECIMEN);
        A_SetWristHingePose wristHingeOut = new A_SetWristHingePose(WristHingePose.DEPOSIT_SPECIMEN);
        A_SetWristTwistPose wristTwistOut = new A_SetWristTwistPose(WristTwistPose.DEPOSIT_SPECIMEN);
        taskManager.runTask(new TaskSequence(closeClaw, wristHingeOut)
                .then(rotateSlidesUp, wristTwistOut)
                .then(elbowOut)
        );
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        follower = new Follower(hardwareMap, FConstantsSpecimenAuto.class, LConstants.class);
        follower.setStartingPose(startPose);
        FollowerWrapper.setFollower(follower);
        buildPaths();

        headingController = new PIDFController(1, 0, 0.05, 0);

        auto.then(scoreSpecimen(startPose))
                // Intake specimen feels like it shouldn't be necessary here, but
                // maybe the wait for movement isn't working in B_ReleaseSpecAndIntakeSpec
                .then(new B_IntakeSpecimen(), new FollowPathTask(follower, pushPop))
                .thenDelay(150)
                .then(new B_GrabSpecimenAndStow())
                .then(specimenLoop(4))
                .thenFollowPath(follower, intakeToPark)
                .then(intakeSpecimen());
    }

    /**
     * This method is called continuously after Init while waiting for "play".
     **/
    @Override
    public void init_loop() {
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }

        System.out.println("===================================");
        System.out.println("Overall States:");
        System.out.println("Robot Mode: " + robotState.getMode());
        System.out.println("Claw Pose: " + clawState.getPose());
        System.out.println("Arm Extension Pose: " + armExtState.getTargetPose());
        System.out.println("Ext actuator touch sensor: " + armExtActuator.isTouching());
        System.out.println("Arm Rotation Pose: " + armRotState.getTargetPose());
        System.out.println("Elbow Pose: " + elbowState.getPose());
        System.out.println("Wrist Hinge Pose: " + wristHingeState.getPose());
        System.out.println("Wrist Twist Pose: " + wristTwistState.getPose());
        System.out.println("---------- Task Manager -------------");

        // Feedback to Driver Hub
        telemetry.addLine("Current tasks:");
        for (Task task : auto.getTasks()) {
            telemetry.addLine("     " + task.getClass().getSimpleName());
        }

        taskManager.loop();
        elbowActuator.loop();
        wristHingeActuator.loop();
        wristTwistActuator.loop();
        armRotActuator.loop();
        clawActuator.loop();
    }

    /**
     * This method is called once at the start of the OpMode.
     * It runs all the setup actions, including building paths and starting the path system
     **/
    @Override
    public void start() {
        opmodeTimer.resetTimer();
        taskManager.runTask(auto);
    }

    /**
     * We do not use this because everything should automatically disable
     **/
    @Override
    public void stop() {
    }
}

