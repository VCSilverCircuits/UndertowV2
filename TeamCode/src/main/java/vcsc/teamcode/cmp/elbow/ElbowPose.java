package vcsc.teamcode.cmp.elbow;

import vcsc.core.abstracts.pose.RotatorPose;

public enum ElbowPose implements RotatorPose {
    STOW(0);

    final double angle;

    ElbowPose(double ang) {
        this.angle = ang;
    }

    public double getAngle() {
        return angle;
    }
}
