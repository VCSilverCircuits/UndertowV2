package vcsc.teamcode.config;

import vcsc.core.util.gamepad.RobotMode;


public enum GlobalPose implements RobotMode {
    DEFAULT(),
    STOW_SAMPLE(
            0.0,
            0.0,
            0.28,
            0.8,
            0.38,
            1.0
    ),
    STOW_SPECIMEN(
            0.0,
            60.0,
            0.5,
            0.5,
            0.38,
            1.0
    ),
    INTAKE_SAMPLE_STRAIGHT(
            35.0,
            0.0,
            0.4,
            0.46,
            0.38,
            1.0
    ),
    INTAKE_SAMPLE_CAMERA_SEARCH(
            35.0,
            0.0,
            0.47,
            0.42,
            0.38,
            1.0
    ),
    INTAKE_SAMPLE_HOVER(
            35.0,
            0.0,
            0.4,
            0.11,
            0.38,
            1.0
    ),
    INTAKE_SAMPLE_GRAB(
            35.0,
            0.0,
            0.31,
            0.20,
            0.38,
            0.0
    ),
    INTAKE_SPECIMEN(
            0.0,
            50.0,
            0.88,
            0.45,
            0.92,
            1.0
    ),
    DEPOSIT_SAMPLE_UPPER(
            73.5,
            97.5,
            0.45,
            0.5,
            0.38,
            null
    ),
    DEPOSIT_SAMPLE_LOWER(
            35.0,
            90.0,
            0.45,
            0.5,
            0.38,
            null
    ),
    DEPOSIT_SPECIMEN(
            26.0,
            65.0,
            0.15,
            0.70,
            0.38,
            null
    ),
    HANG_PRE(
            44.0,
            110.0,
            0.38,
            0.13,
            0.38,
            1.0
    ),
    HANG_LV2_P2(
            0.0,
            57.5,
            0.38,
            0.15,
            0.38,
            1.0
    ),
    HANG_RELEASE(
            6.0,
            50.0,
            0.38,
            0.15,
            0.38,
            1.0
    );
    private final Double armExtensionLength;
    private final Double armRotationAngle;
    private final Double elbowAngle;
    private final Double wristHingeAngle;
    private final Double wristTwistAngle;
    private final Double clawPosition;

    GlobalPose(Double armExtensionLength, Double armRotationAngle, Double elbowAngle, Double wristHingeAngle, Double wristTwistAngle, Double clawPosition) {
        this.armExtensionLength = armExtensionLength;
        this.armRotationAngle = armRotationAngle;
        this.elbowAngle = elbowAngle;
        this.wristHingeAngle = wristHingeAngle;
        this.wristTwistAngle = wristTwistAngle;
        this.clawPosition = clawPosition;
    }

    GlobalPose() {
        this.armExtensionLength = null;
        this.armRotationAngle = null;
        this.elbowAngle = null;
        this.wristHingeAngle = null;
        this.wristTwistAngle = null;
        this.clawPosition = null;
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

    public Double getWristTwistAngle() {
        return wristTwistAngle;
    }

    public Double getClawPosition() {
        return clawPosition;
    }


}
