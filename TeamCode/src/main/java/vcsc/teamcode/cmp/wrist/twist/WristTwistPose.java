package vcsc.teamcode.cmp.wrist.twist;

import vcsc.core.abstracts.templates.rotator.RotatorPose;
import vcsc.teamcode.config.SetPositions;

public enum WristTwistPose implements RotatorPose {
    FORWARD(0),
    LEFT(90),
    BACKWARD(180),
    RIGHT(270),
    DEPOSIT_SAMPLE(SetPositions.DEPOSIT_SAMPLE.getWristTwistAngle());

    final double angle;

    WristTwistPose(double ang) {
        this.angle = ang;
    }

    public double getAngle() {
        return angle;
    }
}
