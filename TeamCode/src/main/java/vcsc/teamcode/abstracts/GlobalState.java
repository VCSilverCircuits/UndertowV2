package vcsc.teamcode.abstracts;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.config.GlobalPose;

public interface GlobalState {
    void setGlobalPose(Action<?> action, GlobalPose pose);
}
