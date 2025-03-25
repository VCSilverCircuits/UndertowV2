package vcsc.teamcode.cmp.elbow.actions;

import vcsc.core.abstracts.templates.rotator.actions.A_SetRotatorPose;
import vcsc.teamcode.cmp.elbow.ElbowPose;
import vcsc.teamcode.cmp.elbow.ElbowState;

public class A_SetElbowPose extends A_SetRotatorPose<ElbowState, ElbowPose> {
    public A_SetElbowPose(ElbowPose pose) {
        super(ElbowState.class, pose);
    }
}
