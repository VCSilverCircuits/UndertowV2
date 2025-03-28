package vcsc.teamcode.config;

import vcsc.core.util.gamepad.RobotMode;
import vcsc.teamcode.cmp.claw.ClawPose;
import vcsc.teamcode.cmp.wrist.twist.WristTwistPose;


public enum GlobalPose implements RobotMode {
    DEFAULT(),
    STOW_SAMPLE(
            0.0,
            0.0,
            0.28,
            0.8,
            WristTwistPose.FORWARD,
            ClawPose.OPEN
    ),
    STOW_SPECIMEN(
            0.0,
            60.0,
            0.5,
            0.5,
            WristTwistPose.FORWARD,
            ClawPose.OPEN
    ),
    INTAKE_SAMPLE_STRAIGHT(
            35.0,
            0.0,
            0.4,
            0.46,
            WristTwistPose.FORWARD,
            ClawPose.OPEN
    ),
    INTAKE_SAMPLE_CAMERA_SEARCH(
            35.0,
            0.0,
            0.47,
            0.42,
            WristTwistPose.FORWARD,
            ClawPose.OPEN
    ),
    INTAKE_SAMPLE_HOVER(
            35.0,
            0.0,
            0.4,
            0.11,
            WristTwistPose.FORWARD,
            ClawPose.OPEN
    ),
    INTAKE_SAMPLE_GRAB(
            35.0,
            0.0,
            0.31,
            0.20,
            WristTwistPose.FORWARD,
            ClawPose.CLOSED
    ),
    INTAKE_SPECIMEN(
            0.0,
            50.0,
            0.88,
            0.45,
            WristTwistPose.BACKWARD,
            ClawPose.OPEN
    ),
    DEPOSIT_SAMPLE_UPPER(
            68.0,
            90.0,
            0.45,
            0.5,
            WristTwistPose.FORWARD,
            null
    ),
    DEPOSIT_SAMPLE_LOWER(
            25.0,
            90.0,
            0.45,
            0.6,
            WristTwistPose.FORWARD,
            null
    ),
    DEPOSIT_SPECIMEN(
            24.0,
            65.0,
            0.18,
            0.7,
            WristTwistPose.FORWARD,
            null
    ),
    HANG_PRE(
            44.0,
            110.0,
            0.38,
            0.13,
            WristTwistPose.FORWARD,
            ClawPose.OPEN
    ),
    HANG_LV2_P2(
            0.0,
            50.0,
            0.38,
            0.15,
            WristTwistPose.FORWARD,
            ClawPose.OPEN
    ),
    HANG_RELEASE(
            6.0,
            50.0,
            0.38,
            0.15,
            WristTwistPose.FORWARD,
            ClawPose.OPEN
    );
    private final Double armExtensionLength;
    private final Double armRotationAngle;
    private final Double elbowAngle;
    private final Double wristHingeAngle;
    private final WristTwistPose wristTwistPose;
    private final ClawPose clawPose;

    GlobalPose(Double armExtensionLength, Double armRotationAngle, Double elbowAngle, Double wristHingeAngle, WristTwistPose wristTwistPose, ClawPose clawPose) {
        this.armExtensionLength = armExtensionLength;
        this.armRotationAngle = armRotationAngle;
        this.elbowAngle = elbowAngle;
        this.wristHingeAngle = wristHingeAngle;
        this.wristTwistPose = wristTwistPose;
        this.clawPose = clawPose;
    }

    GlobalPose() {
        this.armExtensionLength = null;
        this.armRotationAngle = null;
        this.elbowAngle = null;
        this.wristHingeAngle = null;
        this.wristTwistPose = null;
        this.clawPose = null;
    }

    public Double getArmExtensionLength() {
        return armExtensionLength;
    }

    public Double getArmRotationAngle() {
        return armRotationAngle;
    }

    public Double getElbowAngle() {
        return elbowAngle;
    }

    public Double getWristHingeAngle() {
        return wristHingeAngle;
    }

    public WristTwistPose getWristTwistPose() {
        return wristTwistPose;
    }

    public ClawPose getClawPose() {
        return clawPose;
    }


}
