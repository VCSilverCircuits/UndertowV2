package vcsc.teamcode.cmp.arm.rotation.actions;

import static vcsc.teamcode.cmp.arm.rotation.ArmRotationActuator.DEGREES_PER_TICK;

import vcsc.core.abstracts.templates.poweredPIDF.actions.A_SetPoweredPIDFTargetPosition;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationState;

public class A_SetArmRotationAngle extends A_SetPoweredPIDFTargetPosition<ArmRotationState, ArmRotationPose> {
    public A_SetArmRotationAngle(double angle, vcsc.core.abstracts.templates.poweredPIDF.actions.PIDMode PIDMode) {
        super(ArmRotationState.class, angle / DEGREES_PER_TICK, PIDMode);
    }

    public A_SetArmRotationAngle(double angle) {
        super(ArmRotationState.class, angle / DEGREES_PER_TICK);
    }
}
