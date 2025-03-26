package vcsc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import vcsc.core.util.gamepad.BindingSet;
import vcsc.core.util.gamepad.GamepadButton;
import vcsc.core.util.gamepad.RobotMode;
import vcsc.teamcode.bx.B_DepositSample;
import vcsc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(name = "Tele Test", group = "Test")
public class TeleTest extends BaseOpMode {
    @Override
    public void init() {
        super.init();
        BindingSet defaultBindings = new BindingSet();
        defaultBindings.bind(GamepadButton.LEFT_TRIGGER, new B_DepositSample());

        bindManager.setGamepad1Bindings(RobotMode.DEFAULT, defaultBindings);
    }
}
