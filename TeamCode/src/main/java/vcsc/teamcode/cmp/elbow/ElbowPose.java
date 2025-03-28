package vcsc.teamcode.cmp.elbow;

import vcsc.core.abstracts.templates.rotator.RotatorPose;

public enum ElbowPose implements RotatorPose {
    STRAIGHT(0.4);

    final Double angle;

    ElbowPose(double ang) {
        this.angle = ang;
    }

    public Double getAngle() {
        return angle;
    }
}
