package vcsc.teamcode.cmp.elbow.actions;

import vcsc.core.abstracts.templates.rotator.actions.A_SetRotatorAngle;
import vcsc.teamcode.cmp.elbow.ElbowPose;
import vcsc.teamcode.cmp.elbow.ElbowState;

public class A_SetElbowAngle extends A_SetRotatorAngle<ElbowState, ElbowPose> {
    public A_SetElbowAngle(double angle) {
        super(ElbowState.class, angle);
    }
}
