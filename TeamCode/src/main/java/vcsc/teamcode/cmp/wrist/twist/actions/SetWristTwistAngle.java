package vcsc.teamcode.cmp.wrist.twist.actions;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.cmp.wrist.twist.WristTwistState;

public class SetWristTwistAngle extends Action<WristTwistState> {
    double angle;
    public SetWristTwistAngle(double angle) {
        super(WristTwistState.class);
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
