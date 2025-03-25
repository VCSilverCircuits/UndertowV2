package vcsc.teamcode.cmp.arm.extension;

import static vcsc.teamcode.cmp.arm.extension.ArmExtensionActuator.CM_PER_TICK;

import vcsc.core.abstracts.templates.poweredPIDF.PoweredPIDFPose;
import vcsc.teamcode.config.SetPositions;

public enum ArmExtensionPose implements PoweredPIDFPose {
    RETRACT(0),
    DEPOSIT_SAMPLE(SetPositions.DEPOSIT_SAMPLE.getArmExtensionLength());

    final double length;

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
