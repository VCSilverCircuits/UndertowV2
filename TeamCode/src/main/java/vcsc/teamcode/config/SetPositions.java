package vcsc.teamcode.config;

import vcsc.teamcode.cmp.arm.extension.ArmExtensionPose;
import vcsc.teamcode.cmp.claw.ClawPose;

public enum SetPositions {
    STOW_SAMPLE(ArmExtensionPose.RETRACT.getLength(), null, null, null, null, ClawPose.CLOSED),
    STOW_SPECIMEN(null, null, null, null, null, ClawPose.CLOSED),
    PREGRAB_SAMPLE(null, null, null, null, null, ClawPose.OPEN),
    INTAKE_SAMPLE(null, null, null, null, null, null),
    INTAKE_SPECIMEN(null, null, null, null, null, null),
    DEPOSIT_SAMPLE(null, null, null, null, null, null),
    DEPOSIT_SPECIMEN(null, null, null, null, null, null);
    private final double armExtensionLength;
    private final double armRotationAngle;
    private final double elbowAngle;
    private final double wristHingeAngle;
    private final double wristTwistAngle;
    private final ClawPose clawPose;

    SetPositions(Double armExtensionLength, Double armRotationAngle, Double elbowAngle, Double wristHingeAngle, Double wristTwistAngle, ClawPose clawPose) {
        this.armExtensionLength = armExtensionLength;
        this.armRotationAngle = armRotationAngle;
        this.elbowAngle = elbowAngle;
        this.wristHingeAngle = wristHingeAngle;
        this.wristTwistAngle = wristTwistAngle;
        this.clawPose = clawPose;
    }

    public double getArmExtensionLength() {
        return armExtensionLength;
    }

    public double getArmRotationAngle() {
        return armRotationAngle;
    }

    public double getElbowAngle() {
        return elbowAngle;
    }

    public double getWristHingeAngle() {
        return wristHingeAngle;
    }

    public double getWristTwistAngle() {
        return wristTwistAngle;
    }

    public ClawPose getClawPose() {
        return clawPose;
    }


}
