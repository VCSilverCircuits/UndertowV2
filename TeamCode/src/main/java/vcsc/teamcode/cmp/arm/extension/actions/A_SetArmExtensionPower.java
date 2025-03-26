package vcsc.teamcode.cmp.arm.extension.actions;

import vcsc.core.abstracts.templates.poweredPIDF.actions.A_SetPoweredPIDFPower;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionPose;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionState;

public class A_SetArmExtensionPower extends A_SetPoweredPIDFPower<ArmExtensionState, ArmExtensionPose> {
    public A_SetArmExtensionPower(double power) {
        super(ArmExtensionState.class, power);
    }
}
