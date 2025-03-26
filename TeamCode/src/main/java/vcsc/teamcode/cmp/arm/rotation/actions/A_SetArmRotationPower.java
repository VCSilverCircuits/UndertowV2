package vcsc.teamcode.cmp.arm.rotation.actions;

import vcsc.core.abstracts.templates.poweredPIDF.actions.A_SetPoweredPIDFPower;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationState;

public class A_SetArmRotationPower extends A_SetPoweredPIDFPower<ArmRotationState, ArmRotationPose> {
    public A_SetArmRotationPower(double power) {
        super(ArmRotationState.class, power);
    }
}
