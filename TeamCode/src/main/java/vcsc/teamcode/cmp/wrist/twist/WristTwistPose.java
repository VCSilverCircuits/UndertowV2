package vcsc.teamcode.cmp.wrist.twist;

import vcsc.core.abstracts.templates.rotator.RotatorPose;

public enum WristTwistPose implements RotatorPose {
    FORWARD(0.95),
    BACKWARD(0.38);

    public static final double MIN = 0.1;
    public static final double MAX = 0.67;
    final Double angle;

    WristTwistPose(Double ang) {
        this.angle = ang;
    }

    public Double getAngle() {
        return angle;
    }
}