package vcsc.teamcode.cmp.claw;

import vcsc.teamcode.config.GlobalPose;

public enum ClawPose {
    OPEN(0.14),
    MOSTLY_CLOSED(0.108),
    CLOSED(-0.36),
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

    public static final double MIN = 0.45;
    public static final double MAX = 0.9;


    final Double pos;

    ClawPose(Double position) {
        pos = position;
    }

    ClawPose(GlobalPose pose) {
        pos = pose.getClawPosition();
    }

    public Double getPos() {
        return pos;
    }
}