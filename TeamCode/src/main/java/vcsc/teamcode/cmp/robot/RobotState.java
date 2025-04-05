package vcsc.teamcode.cmp.robot;

import vcsc.core.abstracts.state.State;
import vcsc.teamcode.config.GlobalPose;

public class RobotState extends State<RobotState> {
    private static final RobotState instance = new RobotState();
    final double BASE_DRIVE_SPEED = 1.0;
    final double BASE_TURN_SPEED = 0.5;
    double driveSpeed = BASE_DRIVE_SPEED;
    double turnSpeed = BASE_TURN_SPEED;

    GlobalPose mode = GlobalPose.STOW_SAMPLE;

    public RobotState() {
    }

    public static RobotState getInstance() {
        return instance;
    }

    public GlobalPose getMode() {
        return mode;
    }

    public void setMode(GlobalPose mode) {
        System.out.println("[RobotState::setMode] Setting mode to " + mode);
        this.mode = mode;

        if (mode == GlobalPose.INTAKE_SAMPLE_STRAIGHT
                || mode == GlobalPose.INTAKE_SAMPLE_GRAB
                || mode == GlobalPose.INTAKE_SAMPLE_HOVER) {
            driveSpeed = 0.5;
            turnSpeed = 0.15;
        } else if (mode == GlobalPose.DEPOSIT_SAMPLE_LOWER
                || mode == GlobalPose.DEPOSIT_SAMPLE_UPPER) {
            driveSpeed = 0.5;
            turnSpeed = BASE_TURN_SPEED;
        } else if (mode == GlobalPose.HANG_PRE
                || mode == GlobalPose.HANG_LV2_P2
                || mode == GlobalPose.HANG_RELEASE) {
            driveSpeed = 0.75;
            turnSpeed = 0.3;
        } else {
            driveSpeed = BASE_DRIVE_SPEED;
            turnSpeed = BASE_TURN_SPEED;
        }
    }

    public double getDriveSpeed() {
        return driveSpeed;
    }

    public void setDriveSpeed(double driveSpeed) {
        this.driveSpeed = driveSpeed;
    }

    public double getTurnSpeed() {
        return turnSpeed;
    }

    public void setTurnSpeed(double turnSpeed) {
        this.turnSpeed = turnSpeed;
    }
}
