package vcsc.teamcode.cmp.arm.extension.actions;

import vcsc.teamcode.actions.A_SetGlobalPose;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionState;
import vcsc.teamcode.config.GlobalPose;

public class A_SetArmExtensionGlobalPose extends A_SetGlobalPose<ArmExtensionState> {
    public A_SetArmExtensionGlobalPose(GlobalPose pose) {
        super(ArmExtensionState.class, pose);
        action = new A_SetArmExtensionLength(pose.getArmExtensionLength());
    }
}
