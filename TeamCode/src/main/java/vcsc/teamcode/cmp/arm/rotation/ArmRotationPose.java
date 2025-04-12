package vcsc.teamcode.cmp.arm.rotation;

import static vcsc.teamcode.cmp.arm.rotation.ArmRotationActuator.DEGREES_PER_TICK;
import static vcsc.teamcode.config.GlobalPose.*;

import vcsc.core.abstracts.templates.poweredPIDF.PoweredPIDFPose;
import vcsc.teamcode.config.GlobalPose;

public enum ArmRotationPose implements PoweredPIDFPose {
    STOW_SAMPLE(GlobalPose.STOW_SAMPLE),
    STOW_SPECIMEN(GlobalPose.STOW_SPECIMEN),
    INTAKE_SAMPLE_STRAIGHT(GlobalPose.INTAKE_SAMPLE_STRAIGHT),
    INTAKE_SAMPLE_CAMERA_SEARCH(GlobalPose.INTAKE_SAMPLE_CAMERA_SEARCH),
    INTAKE_SAMPLE_HOVER(GlobalPose.INTAKE_SAMPLE_HOVER),
    INTAKE_SAMPLE_GRAB(GlobalPose.INTAKE_SAMPLE_GRAB),
    INTAKE_SPECIMEN(GlobalPose.INTAKE_SPECIMEN),
    DEPOSIT_SAMPLE_UPPER(GlobalPose.DEPOSIT_SAMPLE_UPPER),
    DEPOSIT_SAMPLE_LOWER(GlobalPose.DEPOSIT_SAMPLE_LOWER),
    DEPOSIT_SPECIMEN(GlobalPose.DEPOSIT_SPECIMEN),
    HANG_PRE(GlobalPose.HANG_PRE),
    HANG_LV2_P2(GlobalPose.HANG_LV2_P2),
    HANG_RELEASE(GlobalPose.HANG_RELEASE),
    PRE_LV3_HANG(GlobalPose.PRE_LV3_HANG);

    final double angle;

    ArmRotationPose(double angle) {
        this.angle = angle;
    }

    ArmRotationPose(GlobalPose pose) {
        this.angle = pose.getArmRotationAngle();
    }

    public double getAngle() {
        return angle;
    }

    @Override
    public double getPosition() {
        return getAngle() / DEGREES_PER_TICK;
    }
}
