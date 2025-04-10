package vcsc.teamcode.behavior.sample;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.PIDCoefficients;

import java.util.ArrayList;
import java.util.OptionalDouble;

import vcsc.core.abstracts.behavior.Behavior;
import vcsc.core.abstracts.state.StateRegistry;
import vcsc.core.abstracts.task.TaskSequence;
import vcsc.core.util.GlobalTelemetry;
import vcsc.teamcode.abstracts.abstracts.Block;
import vcsc.teamcode.cmp.camera.Camera;
import vcsc.teamcode.cmp.claw.actions.A_OpenClaw;
import vcsc.teamcode.cmp.elbow.ElbowPose;
import vcsc.teamcode.cmp.elbow.actions.A_SetElbowPose;
import vcsc.teamcode.cmp.robot.FollowerWrapper;
import vcsc.teamcode.cmp.robot.RobotState;
import vcsc.teamcode.cmp.wrist.hinge.WristHingePose;
import vcsc.teamcode.cmp.wrist.hinge.actions.A_SetWristHingePose;
import vcsc.teamcode.cmp.wrist.twist.WristTwistPose;
import vcsc.teamcode.cmp.wrist.twist.WristTwistState;
import vcsc.teamcode.cmp.wrist.twist.actions.A_SetWristTwistAngle;
import vcsc.teamcode.config.GlobalPose;

@Config
public class B_LockOn extends Behavior {
    private static final double strafeF = 0.005;
    private static final double driveF = 0.005;
    public static PIDCoefficients xCoeffs = new PIDCoefficients(0.0035, 0, 0.00001);
    public static PIDCoefficients yCoeffsCoarse = new PIDCoefficients(0.003, 0, 0.0001);
    public static PIDCoefficients yCoeffsFine = new PIDCoefficients(0.0018, 0, 0.0001);
    public static PIDCoefficients headingCoeffs = new PIDCoefficients(1.3, 0, 0.0005);
    public static double strafeSpeed = 0.15;
    double x_offset, y_offset, heading_offset;
    PIDController xController, yControllerCoarse, yControllerFine, headingController;
    Camera camera;
    Follower follower;
    ArrayList<Double> angleList;
    double lastAngle = 0;
    int MAX_ANGLE_LIST_SIZE = 50;
    boolean finished = true;
    boolean failure = false;
    double start_heading = 0;
    MultipleTelemetry telem;
    WristTwistState wristState;
    TaskSequence adjustArmPosition;
    B_IntakeSampleGrabLock sampleGrab = new B_IntakeSampleGrabLock();
    A_OpenClaw openClaw;

    boolean grabbing = false;


    public B_LockOn() {
        follower = FollowerWrapper.getFollower();
        camera = StateRegistry.getInstance().getState(Camera.class);

        xController = new PIDController(xCoeffs.p, xCoeffs.i, xCoeffs.d);
        yControllerCoarse = new PIDController(yCoeffsCoarse.p, yCoeffsCoarse.i, yCoeffsCoarse.d);
        yControllerFine = new PIDController(yCoeffsFine.p, yCoeffsFine.i, yCoeffsFine.d);
        headingController = new PIDController(headingCoeffs.p, headingCoeffs.i, headingCoeffs.d);

        angleList = new ArrayList<>();

        telem = GlobalTelemetry.getInstance();

        wristState = StateRegistry.getInstance().getState(WristTwistState.class);

        A_SetElbowPose elbowUp = new A_SetElbowPose(ElbowPose.INTAKE_SAMPLE_CAMERA_SEARCH);
        A_SetWristHingePose hingeUp = new A_SetWristHingePose(WristHingePose.INTAKE_SAMPLE_CAMERA_SEARCH);
        openClaw = new A_OpenClaw();

        adjustArmPosition = new TaskSequence();
        adjustArmPosition.then(elbowUp, hingeUp, openClaw).setDebugName("AdjustArmPosition");
    }

    @Override
    public boolean start() {
        adjustArmPosition.start();
        RobotState.getInstance().setMode(GlobalPose.INTAKE_SAMPLE_CAMERA_SEARCH);

        grabbing = false;


        finished = false;
        failure = false;
        start_heading = follower.getPose().getHeading();
        xController.setSetPoint(0);
        yControllerCoarse.setSetPoint(0);
        yControllerFine.setSetPoint(0);
        xController.setTolerance(20);
        yControllerCoarse.setTolerance(30);
        yControllerFine.setTolerance(15);
        follower.startTeleopDrive();
        angleList.clear();
        return true;
    }

    public boolean failed() {
        return failure;
    }

    @Override
    public void loop() {
        if (finished) {
            return;
        }

        adjustArmPosition.loop();

        xController.setPID(xCoeffs.p, xCoeffs.i, xCoeffs.d);
        yControllerCoarse.setPID(yCoeffsCoarse.p, yCoeffsCoarse.i, yCoeffsCoarse.d);
        yControllerFine.setPID(yCoeffsFine.p, yCoeffsFine.i, yCoeffsFine.d);
        headingController.setPID(headingCoeffs.p, headingCoeffs.i, headingCoeffs.d);

        Block block = camera.getBlock();
        heading_offset = follower.getPose().getHeading() - Math.toRadians(270);

        if (grabbing) {
            sampleGrab.loop();
            if (sampleGrab.isFinished()) {
                end();
            }
            return;
        }

        if (block != null) {
            x_offset = 213 - block.getX();
            y_offset = 105 - block.getY();


            telem.addData("Block", block.getColor());
            telem.addData("X", block.getX());
            telem.addData("Y", block.getY());
            telem.addData("X Offset", x_offset);
            telem.addData("Y Offset", y_offset);
            telem.addData("Angle", block.getAngle());

            OptionalDouble angleAverage = angleList.stream().mapToDouble(a -> a).average();

            double yUpdate;

            if (yControllerCoarse.atSetPoint()) {
                yControllerCoarse.calculate(-y_offset);
                yUpdate = yControllerFine.calculate(-y_offset);
            } else {
                yUpdate = yControllerCoarse.calculate(-y_offset);
                yControllerFine.calculate(-y_offset);
            }

            follower.setTeleOpMovementVectors(xController.calculate(x_offset) + driveF * Math.signum(x_offset), yUpdate + strafeF * Math.signum(y_offset), headingController.calculate(heading_offset));

            double angle = block.getAngle();
            if (Math.abs(angle) > 85) {
                angle = Math.abs(angle);
            }

            if (angleAverage.isPresent() && Math.abs(angleAverage.getAsDouble() - lastAngle) > 10 || Math.abs(angle - lastAngle) > 50) {

                double poseSpan = WristTwistPose.MAX - WristTwistPose.MIN; // 0.565
                double midPose = (WristTwistPose.MIN + WristTwistPose.MAX) / 2; // 0.3625
                double blockPose = midPose + angle / 90 * poseSpan / 2;

                A_SetWristTwistAngle wristAction = new A_SetWristTwistAngle(blockPose);

                wristAction.start();
                wristAction.loop();

                lastAngle = angle;
                angleList.clear();
                for (int i = 0; i < MAX_ANGLE_LIST_SIZE; ++i) {
                    angleList.add(angle);
                }
            }

            angleList.add(angle);
            while (angleList.size() > MAX_ANGLE_LIST_SIZE) {
                angleList.remove(0);
            }

            if (xController.atSetPoint() && yControllerFine.atSetPoint()) {
                if (!grabbing) {
                    grabbing = true;
                    sampleGrab.start();
                }
            }

        } else {
            telem.addLine("No blocks detected.");
            follower.setTeleOpMovementVectors(0, strafeSpeed, headingController.calculate(heading_offset));
            angleList.clear();
        }
    }

    protected void end() {
        super.end();
        if (!sampleGrab.isFinished()) {
            sampleGrab.cancel();
        }
        finished = true;
        follower.setTeleOpMovementVectors(0, 0, 0);
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void cancel() {
        super.cancel();
        end();
        adjustArmPosition.cancel();
    }
}
