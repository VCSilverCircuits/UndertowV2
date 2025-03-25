package vcsc.teamcode.cmp.wrist.hinge.actions;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.cmp.wrist.hinge.WristHingeState;

public class SetWristHingeAngle extends Action<WristHingeState> {
    double angle;
    public SetWristHingeAngle(double angle) {
        super(WristHingeState.class);
        this.angle = angle;
    }

    @Override
    public boolean start() {
        boolean started = super.start();
        if (!started) {
            return false; // If the action cannot start, return false
        }
        // Set the angle to a default value (e.g., 0 degrees)
        this.state.setAngle(this, angle);
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
