package vcsc.teamcode.cmp.arm.rotation.actions;

import vcsc.core.abstracts.templates.poweredPIDF.actions.A_SetPoweredPIDFTargetPose;
import vcsc.core.abstracts.templates.poweredPIDF.actions.PIDMode;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationState;

public class A_SetArmRotationPose extends A_SetPoweredPIDFTargetPose<ArmRotationState, ArmRotationPose> {
    public A_SetArmRotationPose(ArmRotationPose targetPose, PIDMode PIDMode) {
        super(ArmRotationState.class, targetPose, PIDMode, 0.0);
    }

    public A_SetArmRotationPose(ArmRotationPose targetPose) {
        super(ArmRotationState.class, targetPose);
    }
}
