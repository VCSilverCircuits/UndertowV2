package vcsc.teamcode.cmp.elbow.actions;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.cmp.elbow.ElbowPose;
import vcsc.teamcode.cmp.elbow.ElbowState;

public class SetElbowPose extends Action<ElbowState> {
    ElbowPose pose;
    public SetElbowPose(ElbowPose pose) {
        super(ElbowState.class);
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
