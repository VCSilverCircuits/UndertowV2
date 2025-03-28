package vcsc.teamcode.cmp.claw.actions;

import vcsc.teamcode.actions.A_SetGlobalPose;
import vcsc.teamcode.cmp.claw.ClawState;
import vcsc.teamcode.config.GlobalPose;

public class A_SetClawGlobalPose extends A_SetGlobalPose<ClawState> {
    public A_SetClawGlobalPose(GlobalPose pose) {
        super(ClawState.class, pose);
        action = new A_SetClawPose(pose.getClawPose());
    }
}
