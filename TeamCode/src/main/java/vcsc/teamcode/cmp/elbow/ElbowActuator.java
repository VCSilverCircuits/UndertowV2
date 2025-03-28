package vcsc.teamcode.cmp.elbow;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import vcsc.core.abstracts.templates.rotator.RotatorActuator;

public class ElbowActuator extends RotatorActuator<ElbowState, ElbowPose> {
    ServoImplEx servo2;

    public ElbowActuator(HardwareMap hardwareMap) {
        // TODO: Use both servos
        super(hardwareMap, "elbow1");
        servo2 = hardwareMap.get(ServoImplEx.class, "elbow2");
    }

    public void loop() {
        double rotPosition = angle; // Convert angle to a value between 0 and 1 for the servo
        servo.setPosition(rotPosition);
        servo2.setPosition(rotPosition);
    }

}
