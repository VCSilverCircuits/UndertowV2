package vcsc.teamcode.cmp.claw.actions;

import com.qualcomm.robotcore.util.ElapsedTime;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.cmp.claw.ClawPose;
import vcsc.teamcode.cmp.claw.ClawState;

public class A_SetClawPose extends Action<ClawState> {
    double delay;
    ClawPose pose;
    ElapsedTime timer;
    boolean _finished = true;

    public A_SetClawPose(ClawPose pose) {
        super(ClawState.class);
        this.pose = pose;
        this.delay = this.state.getDelay();
        timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    }

    @Override
    public boolean start() {
        boolean started = super.start();
        if (!started || !_finished) {
            return false; // If the action cannot start, return false
        }
        this.state.setPose(this, pose);
        timer.reset();
        _finished = false;
        return true;
    }

    @Override
    public void loop() {
        if (state.idle() && state.getPosition() == pose.getPos() && timer.time() > delay) {
            end(); // End the action if the pose is reached
        }
    }

    @Override
    public boolean isFinished() {
        return _finished;
    }

    @Override
    protected void end() {
        super.end();
        _finished = true;
    }

    @Override
    public void cancel() {
        super.cancel();
        end();
    }
}
