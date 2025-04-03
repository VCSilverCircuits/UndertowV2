package vcsc.teamcode.cmp.arm.rotation;

import static vcsc.teamcode.cmp.arm.rotation.ArmRotationActuator.DEGREES_PER_TICK;

import vcsc.core.abstracts.action.Action;
import vcsc.core.abstracts.templates.poweredPIDF.PoweredPIDFState;

public class ArmRotationState extends PoweredPIDFState<ArmRotationState, ArmRotationPose> {
    ArmRotationPose currentPose;

    public ArmRotationState() {
        this.power = 0;
    }

    public double getTargetAngle() {
        return getTargetPosition() * DEGREES_PER_TICK;
    }

    public void setAngle(Action<ArmRotationState> action, double angle) {
        setTargetPosition(action, angle / DEGREES_PER_TICK);
    }

    public ArmRotationPose getPose() {
        return currentPose;
    }
}