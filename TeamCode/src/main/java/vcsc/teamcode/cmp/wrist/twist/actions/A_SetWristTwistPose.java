package vcsc.teamcode.cmp.wrist.twist.actions;

import vcsc.core.abstracts.templates.rotator.actions.A_SetRotatorPose;
import vcsc.teamcode.cmp.wrist.twist.WristTwistPose;
import vcsc.teamcode.cmp.wrist.twist.WristTwistState;

public class A_SetWristTwistPose extends A_SetRotatorPose<WristTwistState, WristTwistPose> {
    public A_SetWristTwistPose(WristTwistPose pose) {
        super(WristTwistState.class, pose);
    }
}