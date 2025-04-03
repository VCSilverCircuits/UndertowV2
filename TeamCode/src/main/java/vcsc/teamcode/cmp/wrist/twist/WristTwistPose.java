package vcsc.teamcode.cmp.wrist.twist;

import vcsc.core.abstracts.templates.rotator.RotatorPose;
import vcsc.teamcode.config.GlobalPose;

public enum WristTwistPose implements RotatorPose {
    FORWARD(0.37),
    BACKWARD(0.95),
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
    HANG_RELEASE(GlobalPose.HANG_RELEASE);

    public static final double MIN = 0.08;
    public static final double MAX = 0.645;
    final Double angle;


    WristTwistPose(Double ang) {
        this.angle = ang;
    }

    WristTwistPose(GlobalPose pose) {
        this.angle = pose.getWristTwistAngle();
    }

    public Double getAngle() {
        return angle;
    }


}