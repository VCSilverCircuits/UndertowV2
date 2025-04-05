package vcsc.teamcode.cmp.arm.extension.actions;

import static vcsc.teamcode.cmp.arm.extension.ArmExtensionActuator.CM_PER_TICK;

import vcsc.core.abstracts.templates.poweredPIDF.actions.A_SetPoweredPIDFTargetPose;
import vcsc.core.abstracts.templates.poweredPIDF.actions.PIDMode;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionPose;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionState;

public class A_SetArmExtensionPose extends A_SetPoweredPIDFTargetPose<ArmExtensionState, ArmExtensionPose> {
    public A_SetArmExtensionPose(ArmExtensionPose targetPose, PIDMode PIDMode, double tolerance) {
        super(ArmExtensionState.class, targetPose, PIDMode, tolerance / CM_PER_TICK);
    }

    public A_SetArmExtensionPose(ArmExtensionPose targetPose) {
        this(targetPose, PIDMode.EXCEED, 5.0);
    }
}
