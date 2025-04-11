package vcsc.teamcode.cmp.claw.actions;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.cmp.claw.ClawPose;
import vcsc.teamcode.cmp.claw.ClawState;

public class A_ToggleClaw extends Action<ClawState> {
    ClawPose pose;
    A_SetClawPose setClawPose;

    public A_ToggleClaw() {
        super(ClawState.class);

    }

    @Override
    public boolean start() {
        super.start();
        if (state.getPosition() < 0.5) {
            this.pose = ClawPose.OPEN;
        } else {
            this.pose = ClawPose.CLOSED;
        }
        setClawPose = new A_SetClawPose(this.pose);
        return setClawPose.start();
    }

    @Override
    public void loop() {
        setClawPose.loop();
    }

    @Override
    public boolean isFinished() {
        return setClawPose._finished;
    }

    @Override
    protected void end() {
        super.end();
    }

    @Override
    public void cancel() {
        super.cancel();
        setClawPose.cancel();
        end();
    }
}