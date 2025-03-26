package vcsc.teamcode.cmp.wrist.twist;

import com.qualcomm.robotcore.hardware.HardwareMap;

import vcsc.core.abstracts.templates.rotator.RotatorActuator;

public class WristTwistActuator extends RotatorActuator<WristTwistState, WristTwistPose> {

    public WristTwistActuator(HardwareMap hardwareMap) {
        super(hardwareMap, "wristTwist");
    }

    @Override
    public void loop() {
        double rotPosition = angle; // Convert angle to a value between 0 and 1 for the servo
        servo.setPosition(angle);
    }

}
