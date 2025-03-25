package vcsc.teamcode.cmp.wrist.twist.actions;

import vcsc.core.abstracts.templates.rotator.actions.A_SetRotatorAngle;
import vcsc.teamcode.cmp.wrist.twist.WristTwistPose;
import vcsc.teamcode.cmp.wrist.twist.WristTwistState;

public class A_SetWristTwistAngle extends A_SetRotatorAngle<WristTwistState, WristTwistPose> {
    public A_SetWristTwistAngle(double angle) {
        super(WristTwistState.class, angle);
    }
}
