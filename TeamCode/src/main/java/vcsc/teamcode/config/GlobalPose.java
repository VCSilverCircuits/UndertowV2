package vcsc.teamcode.config;

import vcsc.core.util.gamepad.RobotMode;


public enum GlobalPose implements RobotMode {
    DEFAULT(),
    STOW_SAMPLE(
            0.0,
            0.0,
            0.28,
            0.8,
            0.36,
            0.9
    ),
    STOW_SPECIMEN(
            0.0,
            60.0,
            0.5,
            0.5,
            0.36,
            0.11
    ),
    INTAKE_SAMPLE_STRAIGHT(
            35.0,
            0.0,
            0.4,
            0.46,
            0.36,
            0.9
    ),
    INTAKE_SAMPLE_CAMERA_SEARCH(
            35.0,
            0.0,
            0.47,
            0.42,
            0.36,
            0.9
    ),
    INTAKE_SAMPLE_HOVER(
            39.0,
            0.0,
            0.43,
            0.11,
            0.36,
            0.9
    ),
    INTAKE_SAMPLE_GRAB(
            35.0,
            0.0,
            0.31,
            0.20,
            0.36,
            0.11
    ),
    INTAKE_SPECIMEN(
            0.0,
            50.0,
            0.95,
            0.42,
            0.92,
            0.9
    ),
    DEPOSIT_SAMPLE_UPPER(
            71.0,
            94.5,
            0.45,
            0.55,
            0.36,
            null
    ),
    DEPOSIT_SAMPLE_LOWER(
            22.0,
            90.0,
            0.45,
            0.55,
            0.36,
            null
    ),
    DEPOSIT_SPECIMEN(
            27.0,
            68.0,
            0.17,
            0.70,
            0.36,
            null
    ),
    HANG_PRE(
            44.0,
            115.0,
            0.38,
            0.13,
            0.36,
            0.9
    ),
    HANG_LV2_P2(
            0.0,
            45.0,
            0.38,
            0.15,
            0.36,
            0.9
    ),
    HANG_RELEASE(
            6.0,
            80.0,
            0.38,
            0.15,
            0.36,
            0.9
    ),
    PRE_LV3_HANG(
            53.0,
            140.0,
            0.9,
            0.68,
            0.36,
            0.9
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
