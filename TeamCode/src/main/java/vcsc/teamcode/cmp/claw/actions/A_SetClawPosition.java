package vcsc.teamcode.cmp.claw.actions;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.cmp.claw.ClawState;

public class A_SetClawPosition extends Action<ClawState> {
    double pos;

    public A_SetClawPosition(double pos) {
        super(ClawState.class);
        this.pos = pos;
    }

    @Override
    public void loop() {

    }

    @Override
    public boolean start() {
        boolean started = super.start();
        if (!started) {
            return false;
        }

        this.state.setPosition(this, pos);

        return true;
    }

    @Override
    public boolean isFinished() {
        // TODO: Make this have delay
        return true;
    }

    @Override
    public void cancel() {
        super.cancel();
    }
}
