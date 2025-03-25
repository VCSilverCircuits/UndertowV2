package vcsc.teamcode.cmp.wrist.hinge;

import vcsc.core.abstracts.pose.RotatorPose;

public enum WristHingePose implements RotatorPose {
    STOW(0);

    final double angle;

    WristHingePose(double ang) {
        this.angle = ang;
    }

    public double getAngle() {
        return angle;
    }
}
