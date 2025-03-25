package vcsc.teamcode.cmp.wrist.twist;

import vcsc.core.abstracts.pose.RotatorPose;

public enum WristTwistPose implements RotatorPose {
    FORWARD(0),
    LEFT(90),
    BACKWARD(180),
    RIGHT(270);

    final double angle;

    WristTwistPose(double ang) {
        this.angle = ang;
    }

    public double getAngle() {
        return angle;
    }
}
