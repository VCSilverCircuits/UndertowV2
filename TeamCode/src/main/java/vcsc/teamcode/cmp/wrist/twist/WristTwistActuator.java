package vcsc.teamcode.cmp.wrist.twist;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import vcsc.core.abstracts.actuator.Actuator;
import vcsc.core.abstracts.actuator.RotatorActuator;
import vcsc.core.abstracts.state.State;

public class WristTwistActuator extends RotatorActuator {

    public WristTwistActuator(HardwareMap hardwareMap) {
        super(hardwareMap, "wristTwist");
    }

}
