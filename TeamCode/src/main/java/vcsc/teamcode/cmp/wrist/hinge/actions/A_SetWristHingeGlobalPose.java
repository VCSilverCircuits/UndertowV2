package vcsc.teamcode.cmp.wrist.hinge.actions;

import vcsc.teamcode.actions.A_SetGlobalPose;
import vcsc.teamcode.cmp.wrist.hinge.WristHingeState;
import vcsc.teamcode.config.GlobalPose;

public class A_SetWristHingeGlobalPose extends A_SetGlobalPose<WristHingeState> {
    public A_SetWristHingeGlobalPose(GlobalPose pose) {
        super(WristHingeState.class, pose);
        action = new A_SetWristHingeAngle(pose.getWristHingeAngle());
    }
}
