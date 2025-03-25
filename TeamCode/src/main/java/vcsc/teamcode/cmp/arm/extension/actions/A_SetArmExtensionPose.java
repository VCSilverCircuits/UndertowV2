package vcsc.teamcode.cmp.arm.extension.actions;

import vcsc.core.abstracts.templates.poweredPIDF.actions.A_SetPoweredPIDFTargetPose;
import vcsc.core.abstracts.templates.poweredPIDF.actions.PIDMode;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionPose;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionState;

public class A_SetArmExtensionPose extends A_SetPoweredPIDFTargetPose<ArmExtensionState, ArmExtensionPose> {
    public A_SetArmExtensionPose(ArmExtensionPose targetPose, PIDMode PIDMode) {
        super(ArmExtensionState.class, targetPose, PIDMode);
    }

    public A_SetArmExtensionPose(ArmExtensionPose targetPose) {
        super(ArmExtensionState.class, targetPose);
    }
}
