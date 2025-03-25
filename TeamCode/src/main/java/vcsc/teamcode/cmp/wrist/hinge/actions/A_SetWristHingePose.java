package vcsc.teamcode.cmp.wrist.hinge.actions;

import vcsc.core.abstracts.templates.rotator.actions.A_SetRotatorPose;
import vcsc.teamcode.cmp.wrist.hinge.WristHingePose;
import vcsc.teamcode.cmp.wrist.hinge.WristHingeState;

public class A_SetWristHingePose extends A_SetRotatorPose<WristHingeState, WristHingePose> {
    public A_SetWristHingePose(WristHingePose pose) {
        super(WristHingeState.class, pose);
    }
}
