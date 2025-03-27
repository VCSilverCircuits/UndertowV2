package vcsc.teamcode.cmp.claw;


import vcsc.core.abstracts.action.Action;
import vcsc.core.abstracts.state.State;

public class ClawState extends State<ClawState> {
    double position = 0;
    ClawPose pose;

    public ClawState() {
        super();
    }

    public double getPosition() {
        return position;
    }

    public void setPosition(Action<ClawState> action, double position) throws IllegalStateException {
        assertLock(action);
        this.position = position;
        notifyActuators();
    }

    public void setPose(Action<ClawState> action, ClawPose pose) {
        setPosition(action, pose.getPos());
        this.pose = pose;
    }

    public ClawPose getPose() {
        return pose;
    }

    public double getDelay() {
        ClawActuator clawActuator = (ClawActuator) this.actuators.get(0);
        return clawActuator.getDelay();
    }
}


