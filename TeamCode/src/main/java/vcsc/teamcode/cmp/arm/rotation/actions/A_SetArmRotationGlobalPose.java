package vcsc.teamcode.cmp.arm.rotation.actions;

import vcsc.teamcode.actions.A_SetGlobalPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationState;
import vcsc.teamcode.config.GlobalPose;

public class A_SetArmRotationGlobalPose extends A_SetGlobalPose<ArmRotationState> {
    public A_SetArmRotationGlobalPose(GlobalPose pose) {
        super(ArmRotationState.class, pose);
        action = new A_SetArmRotationAngle(pose.getArmRotationAngle());
    }
}
