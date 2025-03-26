package vcsc.teamcode.cmp.wrist.hinge;

import com.qualcomm.robotcore.hardware.HardwareMap;

import vcsc.core.abstracts.templates.rotator.RotatorActuator;

public class WristHingeActuator extends RotatorActuator<WristHingeState, WristHingePose> {

    public WristHingeActuator(HardwareMap hardwareMap) {
        super(hardwareMap, "wristHinge");
    }

    @Override
    public void loop() {
        double rotPosition = angle; // Convert angle to a value between 0 and 1 for the servo
        servo.setPosition(angle);
    }
}
