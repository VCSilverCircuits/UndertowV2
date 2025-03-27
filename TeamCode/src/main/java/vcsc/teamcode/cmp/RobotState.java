package vcsc.teamcode.cmp;

import vcsc.teamcode.config.GlobalPose;

public class RobotState {
    private static final RobotState instance = new RobotState();
    double driveSpeed = 1.0;
    double turnSpeed = 0.5;

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
        this.mode = mode;
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
