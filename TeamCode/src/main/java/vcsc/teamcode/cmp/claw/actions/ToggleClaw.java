package vcsc.teamcode.cmp.claw.actions;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.cmp.claw.ClawPose;
import vcsc.teamcode.cmp.claw.ClawState;

public class ToggleClaw extends Action<ClawState> {
    ClawPose pose;
    public ToggleClaw() {
        super(ClawState.class);
        if (state.getPose() == ClawPose.CLOSED) {
            this.pose = ClawPose.OPEN;
        } else {
            this.pose = ClawPose.CLOSED;
        }
    }

    @Override
    public boolean start() {
        boolean started = super.start();
        if (!started) {
            return false; // If the action cannot start, return false
        }
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