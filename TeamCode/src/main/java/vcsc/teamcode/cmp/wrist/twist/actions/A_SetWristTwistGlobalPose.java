package vcsc.teamcode.cmp.wrist.twist.actions;

import vcsc.teamcode.actions.A_SetGlobalPose;
import vcsc.teamcode.cmp.wrist.twist.WristTwistState;
import vcsc.teamcode.config.GlobalPose;

public class A_SetWristTwistGlobalPose extends A_SetGlobalPose<WristTwistState> {
    public A_SetWristTwistGlobalPose(GlobalPose pose) {
        super(WristTwistState.class, pose);
        action = new A_SetWristTwistPose(pose.getWristTwistPose());
    }
}
