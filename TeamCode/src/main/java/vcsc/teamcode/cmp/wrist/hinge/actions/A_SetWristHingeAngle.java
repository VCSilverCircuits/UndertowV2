package vcsc.teamcode.cmp.wrist.hinge.actions;

import vcsc.core.abstracts.templates.rotator.actions.A_SetRotatorAngle;
import vcsc.teamcode.cmp.wrist.hinge.WristHingePose;
import vcsc.teamcode.cmp.wrist.hinge.WristHingeState;

public class A_SetWristHingeAngle extends A_SetRotatorAngle<WristHingeState, WristHingePose> {
    public A_SetWristHingeAngle(double angle) {
        super(WristHingeState.class, angle);
    }
}
