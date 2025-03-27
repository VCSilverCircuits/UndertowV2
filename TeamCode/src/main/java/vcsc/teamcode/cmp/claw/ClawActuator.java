package vcsc.teamcode.cmp.claw;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import vcsc.core.abstracts.actuator.Actuator;
import vcsc.core.abstracts.state.State;

public class ClawActuator extends Actuator<ClawState> {
    final double delay = 50;
    Servo servo;
    double targetPosition;

    public ClawActuator(HardwareMap hardwareMap) {
        servo = hardwareMap.get(ServoImplEx.class, "claw");
    }

    @Override
    public void loop() {
        servo.setPosition(ClawPose.MIN + (targetPosition * (ClawPose.MAX - ClawPose.MIN)));
    }

    @Override
    public void updateState(State<ClawState> newState) {
        ClawState clawState = (ClawState) newState;
        targetPosition = clawState.getPosition();
    }

    public double getDelay() {
        return delay;
    }


}
