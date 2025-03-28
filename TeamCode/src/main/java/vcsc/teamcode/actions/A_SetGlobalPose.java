package vcsc.teamcode.actions;

import vcsc.core.abstracts.action.Action;
import vcsc.core.abstracts.state.State;
import vcsc.teamcode.config.GlobalPose;

public class A_SetGlobalPose<S extends State<S>> extends Action<S> {
    protected Action<?> action;
    GlobalPose pose;

    public A_SetGlobalPose(Class<S> stateClass, GlobalPose pose) {
        super(stateClass);
        this.pose = pose;
    }

    @Override
    public boolean start() {
        super.start();
        return action.start();
    }

    @Override
    public void loop() {
        action.loop();
    }

    @Override
    public boolean isFinished() {
        return action.isFinished();
    }

    @Override
    public void cancel() {
        action.cancel();
    }
}
