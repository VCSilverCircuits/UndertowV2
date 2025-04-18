package vcsc.teamcode.cmp.arm.rotation;

import static vcsc.teamcode.cmp.arm.rotation.ArmRotationActuator.DEGREES_PER_TICK;

import vcsc.core.abstracts.action.Action;
import vcsc.core.abstracts.templates.poweredPIDF.PoweredPIDFState;

public class ArmRotationState extends PoweredPIDFState<ArmRotationState, ArmRotationPose> {
    ArmRotationPose currentPose;
    boolean disabled = false;

    public ArmRotationState() {
        this.power = 0;
    }

    public double getTargetAngle() {
        return getTargetPosition() * DEGREES_PER_TICK;
    }

    public void setAngle(Action<ArmRotationState> action, double angle) {
        setTargetPosition(action, angle / DEGREES_PER_TICK);
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public double getRealAngle() {
        return getRealPosition() * DEGREES_PER_TICK;
    }

    public ArmRotationPose getPose() {
        return currentPose;
    }

    public void brake() {
        ((ArmRotationActuator) primaryActuator).brake();
    }
}