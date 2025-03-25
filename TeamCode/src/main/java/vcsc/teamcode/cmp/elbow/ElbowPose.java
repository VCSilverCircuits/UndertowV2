package vcsc.teamcode.cmp.elbow;

import vcsc.core.abstracts.templates.rotator.RotatorPose;
import vcsc.teamcode.config.SetPositions;

public enum ElbowPose implements RotatorPose {
    STOW(0),
    DEPOSIT_SAMPLE(SetPositions.DEPOSIT_SAMPLE.getElbowAngle());

    final double angle;

    ElbowPose(double ang) {
        this.angle = ang;
    }

    public double getAngle() {
        return angle;
    }
}
