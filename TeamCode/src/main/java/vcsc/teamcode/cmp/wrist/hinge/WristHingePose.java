package vcsc.teamcode.cmp.wrist.hinge;

import vcsc.core.abstracts.templates.rotator.RotatorPose;
import vcsc.teamcode.config.SetPositions;

public enum WristHingePose implements RotatorPose {
    STOW(0),
    DEPOSIT_SAMPLE(SetPositions.DEPOSIT_SAMPLE.getWristHingeAngle());

    final double angle;

    WristHingePose(double ang) {
        this.angle = ang;
    }

    public double getAngle() {
        return angle;
    }
}
