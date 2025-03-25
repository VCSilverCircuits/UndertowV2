package vcsc.teamcode.cmp.claw.actions;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.cmp.claw.ClawPose;
import vcsc.teamcode.cmp.claw.ClawState;

public class A_SetClawPose extends Action<ClawState> {
    ClawPose pose;

    public A_SetClawPose(ClawPose pose) {
        super(ClawState.class);
        this.pose = pose;
    }

    @Override
    public boolean start() {
        boolean started = super.start();
        if (!started) {
            return false; // If the action cannot start, return false
        }
        this.state.setPose(this, pose);
        return true;
    }

    @Override
    public void loop() {

    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
