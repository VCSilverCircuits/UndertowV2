package vcsc.teamcode.cmp.arm.extension;

import static vcsc.teamcode.cmp.arm.extension.ArmExtensionActuator.CM_PER_TICK;

import vcsc.core.abstracts.action.Action;
import vcsc.core.abstracts.templates.poweredPIDF.PoweredPIDFState;

public class ArmExtensionState extends PoweredPIDFState<ArmExtensionState, ArmExtensionPose> {
    ArmExtensionPose currentPose;

    public double getRealExtensionLength() {
        return getRealPosition() * CM_PER_TICK;
    }

    public double getExtensionLength() {
        return getTargetPosition() * CM_PER_TICK;
    }

    public void setExtensionLength(Action<ArmExtensionState> action, double length) {
        assertLock(action);
        setTargetPosition(action, length / CM_PER_TICK);
    }

    public ArmExtensionPose getPose() {
        return currentPose;
    }
}

