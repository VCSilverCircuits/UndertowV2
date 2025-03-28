package vcsc.teamcode.cmp.elbow.actions;

import vcsc.teamcode.actions.A_SetGlobalPose;
import vcsc.teamcode.cmp.elbow.ElbowState;
import vcsc.teamcode.config.GlobalPose;

public class A_SetElbowGlobalPose extends A_SetGlobalPose<ElbowState> {
    public A_SetElbowGlobalPose(GlobalPose pose) {
        super(ElbowState.class, pose);
        action = new A_SetElbowAngle(pose.getElbowAngle());
    }
}
