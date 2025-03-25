package vcsc.teamcode.cmp.wrist.twist.actions;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.cmp.wrist.twist.WristTwistPose;
import vcsc.teamcode.cmp.wrist.twist.WristTwistState;

public class SetWristTwistPose extends Action<WristTwistState> {
    WristTwistPose pose;
    public SetWristTwistPose(WristTwistPose pose) {
        super(WristTwistState.class);
        this.pose = pose;
    }

    @Override
    public boolean start() {
        boolean started = super.start();
        if (!started) {
            return false; // If the action cannot start, return false
        }
        // Set the angle to a default value (e.g., 0 degrees)
        this.state.setPose(this, pose);
        return false;
    }

    @Override
    public void loop() {

    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
