package vcsc.teamcode.cmp.wrist.hinge;

import vcsc.core.abstracts.templates.rotator.RotatorPose;

public enum WristHingePose implements RotatorPose {
    STRAIGHT(0.46);

    final Double angle;

    WristHingePose(double ang) {
        this.angle = ang;
    }

    public Double getAngle() {
        return angle;
    }
}
