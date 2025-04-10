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
    double x_offset, y_offset, heading_offset;

    PIDController xController, yController, headingController;
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
    TaskSequence s;

    public static PIDCoefficients xCoeffs = new PIDCoefficients(0.003, 0, 0.00001);
    public static PIDCoefficients yCoeffs = new PIDCoefficients(0.002, 0, 0.0001);
    public static PIDCoefficients headingCoeffs = new PIDCoefficients(1.5, 0, 0.0005);

    public static double strafeSpeed = 0.15;

    B_IntakeSampleGrabLock sampleGrab = new B_IntakeSampleGrabLock();
    A_OpenClaw openClaw;

    boolean grabbing = false;


    public B_LockOn() {
        follower = FollowerWrapper.getFollower();
        camera = StateRegistry.getInstance().getState(Camera.class);
        xController = new PIDController(xCoeffs.p, xCoeffs.i, xCoeffs.d);
        yController = new PIDController(yCoeffs.p, yCoeffs.i, yCoeffs.d);
        headingController = new PIDController(headingCoeffs.p, headingCoeffs.i, headingCoeffs.d);
        angleList = new ArrayList<>();
        telem = GlobalTelemetry.getInstance();
        wristState = StateRegistry.getInstance().getState(WristTwistState.class);

        A_SetElbowPose elbowUp = new A_SetElbowPose(ElbowPose.INTAKE_SAMPLE_CAMERA_SEARCH);
        A_SetWristHingePose hingeUp = new A_SetWristHingePose(WristHingePose.INTAKE_SAMPLE_CAMERA_SEARCH);

        openClaw = new A_OpenClaw();

        s = new TaskSequence();
        s.then(elbowUp, hingeUp, openClaw);
    }

    @Override
    public boolean start() {
        s.start();
        RobotState.getInstance().setMode(GlobalPose.INTAKE_SAMPLE_CAMERA_SEARCH);

        grabbing = false;

        finished = false;
        failure = false;
        start_heading = follower.getPose().getHeading();
        xController.setSetPoint(0);
        yController.setSetPoint(0);
        xController.setTolerance(10);
        yController.setTolerance(15);
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

        xController.setPID(xCoeffs.p, xCoeffs.i, xCoeffs.d);
        yController.setPID(yCoeffs.p, yCoeffs.i, yCoeffs.d);
        headingController.setPID(headingCoeffs.p, headingCoeffs.i, headingCoeffs.d);

        Block block = camera.getBlock();
        heading_offset = follower.getPose().getHeading() - Math.toRadians(270);

        sampleGrab.loop();

        if (grabbing) {
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

            follower.setTeleOpMovementVectors(xController.calculate(x_offset), yController.calculate(-y_offset), headingController.calculate(heading_offset));

            if (xController.atSetPoint() && yController.atSetPoint()) {
                if (!grabbing) {
                    grabbing = true;
                    sampleGrab.start();
                }
                return;
            }

            double angle = block.getAngle();
            if (Math.abs(angle) > 85) {
                angle = Math.abs(angle);
            }

            if (angleAverage.isPresent() && Math.abs(angleAverage.getAsDouble() - lastAngle) > 10 || Math.abs(angle - lastAngle) > 20) {

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
        } else {
            telem.addLine("No blocks detected.");
            follower.setTeleOpMovementVectors(0, strafeSpeed, headingController.calculate(heading_offset));
            angleList.clear();
        }
    }

    protected void end() {
        super.end();
        sampleGrab.cancel();
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
        s.cancel();
    }
}
