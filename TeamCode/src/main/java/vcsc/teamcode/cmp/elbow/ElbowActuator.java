package vcsc.teamcode.cmp.elbow;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import vcsc.core.abstracts.actuator.Actuator;
import vcsc.core.abstracts.actuator.RotatorActuator;
import vcsc.core.abstracts.state.State;

public class ElbowActuator extends RotatorActuator {
    public ElbowActuator(HardwareMap hardwareMap) {
        super(hardwareMap, "elbow");
    }

}
