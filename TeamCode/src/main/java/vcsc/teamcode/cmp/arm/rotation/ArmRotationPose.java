package vcsc.teamcode.cmp.arm.rotation;

import static vcsc.teamcode.cmp.arm.rotation.ArmRotationActuator.DEGREES_PER_TICK;

import vcsc.core.abstracts.templates.poweredPIDF.PoweredPIDFPose;

public enum ArmRotationPose implements PoweredPIDFPose {
    ;

    final double angle;

    ArmRotationPose(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }

    @Override
    public double getPosition() {
        return getAngle() / DEGREES_PER_TICK;
    }
}
