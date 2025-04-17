package vcsc.teamcode.cmp.arm.extension;

import static vcsc.teamcode.cmp.arm.extension.ArmExtensionActuator.CM_PER_TICK;

import vcsc.core.abstracts.action.Action;
import vcsc.core.abstracts.templates.poweredPIDF.PoweredPIDFState;

public class ArmExtensionState extends PoweredPIDFState<ArmExtensionState, ArmExtensionPose> {
    ArmExtensionPose currentPose;

    public double getRealLength() {
        return getRealPosition() * CM_PER_TICK;
    }

    public double getTargetLength() {
        return getTargetPosition() * CM_PER_TICK;
    }

    public void setLength(Action<ArmExtensionState> action, double length) {
        assertLock(action);
        setTargetPosition(action, length / CM_PER_TICK);
    }

    public ArmExtensionPose getPose() {
        return currentPose;
    }

    public boolean isTouching() {
        return ((ArmExtensionActuator) primaryActuator).isTouching();
    }

    public void brake() {
        ((ArmExtensionActuator) primaryActuator).brake();
    }
}

