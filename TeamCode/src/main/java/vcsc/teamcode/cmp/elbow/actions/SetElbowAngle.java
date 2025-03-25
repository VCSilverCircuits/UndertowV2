package vcsc.teamcode.cmp.elbow.actions;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.cmp.elbow.ElbowState;

public class SetElbowAngle extends Action<ElbowState> {
    double angle;
    public SetElbowAngle(double angle) {
        super(ElbowState.class);
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
