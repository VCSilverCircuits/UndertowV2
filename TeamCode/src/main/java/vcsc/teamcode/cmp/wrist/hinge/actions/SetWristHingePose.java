package vcsc.teamcode.cmp.wrist.hinge.actions;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.cmp.wrist.hinge.WristHingePose;
import vcsc.teamcode.cmp.wrist.hinge.WristHingeState;

public class SetWristHingePose extends Action<WristHingeState> {
    WristHingePose pose;
    public SetWristHingePose(WristHingePose pose) {
        super(WristHingeState.class);
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
