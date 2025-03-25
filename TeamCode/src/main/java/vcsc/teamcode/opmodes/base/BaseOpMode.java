package vcsc.teamcode.opmodes.base;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import vcsc.core.abstracts.state.StateRegistry;

public class BaseOpMode extends OpMode {
    @Override
    public void init() {
        StateRegistry reg = StateRegistry.getInstance();
        reg.registerState(new vcsc.teamcode.cmp.claw.ClawState());
    }

    @Override
    public void loop() {

    }
}
