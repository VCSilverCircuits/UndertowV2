package vcsc.teamcode.cmp.arm.extension;

import static vcsc.teamcode.cmp.arm.extension.ArmExtensionActuator.CM_PER_TICK;

import vcsc.core.abstracts.templates.poweredPIDF.PoweredPIDFPose;

public enum ArmExtensionPose implements PoweredPIDFPose {
    ;

    final Double length;

    ArmExtensionPose(double length) {
        this.length = length;
    }

    public double getLength() {
        return length;
    }


    @Override
    public double getPosition() {
        return getLength() / CM_PER_TICK;
    }

}
