package vcsc.teamcode.cmp.elbow;

import com.qualcomm.robotcore.hardware.HardwareMap;

import vcsc.core.abstracts.templates.rotator.RotatorActuator;

public class ElbowActuator extends RotatorActuator<ElbowState, ElbowPose> {
    public ElbowActuator(HardwareMap hardwareMap) {
        // TODO: Use both servos
        super(hardwareMap, "elbow1");
    }

    @Override
    public void loop() {
        double rotPosition = angle / 370.0; // Convert angle to a value between 0 and 1 for the servo
        servo.setPosition(angle);
    }

}
