package vcsc.teamcode.opmodes.base;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import vcsc.core.abstracts.state.StateRegistry;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionActuator;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionState;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationActuator;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationState;
import vcsc.teamcode.cmp.claw.ClawActuator;
import vcsc.teamcode.cmp.claw.ClawState;
import vcsc.teamcode.cmp.elbow.ElbowActuator;
import vcsc.teamcode.cmp.elbow.ElbowState;
import vcsc.teamcode.cmp.wrist.hinge.WristHingeActuator;
import vcsc.teamcode.cmp.wrist.hinge.WristHingeState;
import vcsc.teamcode.cmp.wrist.twist.WristTwistActuator;
import vcsc.teamcode.cmp.wrist.twist.WristTwistState;

public class BaseOpMode extends OpMode {
    @Override
    public void init() {
        StateRegistry reg = StateRegistry.getInstance();
        ClawState clawState = new ClawState();
        clawState.registerActuator(new ClawActuator(hardwareMap));

        ArmExtensionState armExtState = new ArmExtensionState();
        armExtState.registerActuator(new ArmExtensionActuator(hardwareMap, new PIDFCoefficients()));

        ArmRotationState armRotState = new ArmRotationState();
        armRotState.registerActuator(new ArmRotationActuator(hardwareMap, new PIDFCoefficients()));

        ElbowState elbowState = new ElbowState();
        elbowState.registerActuator(new ElbowActuator(hardwareMap));

        WristHingeState wristHingeState = new WristHingeState();
        wristHingeState.registerActuator(new WristHingeActuator(hardwareMap));

        WristTwistState wristTwistState = new WristTwistState();
        wristTwistState.registerActuator(new WristTwistActuator(hardwareMap));

        // Register all states
        reg.registerStates(clawState, armExtState, armRotState, elbowState, wristHingeState, wristTwistState);
    }

    @Override
    public void loop() {

    }
}
