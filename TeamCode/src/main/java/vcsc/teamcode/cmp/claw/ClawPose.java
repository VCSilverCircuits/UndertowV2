package vcsc.teamcode.cmp.claw;

import vcsc.teamcode.config.GlobalPose;

public enum ClawPose {
    OPEN(0.9),
    MOSTLY_CLOSED(0.32), // idk bro what this is, we never use it
    CLOSED(0.11), // This is basically mostly closed at this point
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

    public static final double MIN = 0.25;
    public static final double MAX = 0.5;


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
