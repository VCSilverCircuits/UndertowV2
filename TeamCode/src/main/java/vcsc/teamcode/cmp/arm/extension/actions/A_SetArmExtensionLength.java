package vcsc.teamcode.cmp.arm.extension.actions;

import static vcsc.teamcode.cmp.arm.extension.ArmExtensionActuator.CM_PER_TICK;

import vcsc.core.abstracts.templates.poweredPIDF.actions.A_SetPoweredPIDFTargetPosition;
import vcsc.core.abstracts.templates.poweredPIDF.actions.PIDMode;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionPose;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionState;

public class A_SetArmExtensionLength extends A_SetPoweredPIDFTargetPosition<ArmExtensionState, ArmExtensionPose> {

    public A_SetArmExtensionLength(double length, PIDMode PIDMode, double tolerance) {
        super(ArmExtensionState.class, length / CM_PER_TICK, PIDMode, tolerance / CM_PER_TICK);
    }

    public A_SetArmExtensionLength(double length) {
        this(length / CM_PER_TICK, PIDMode.EXCEED, 5.0);
    }
}
