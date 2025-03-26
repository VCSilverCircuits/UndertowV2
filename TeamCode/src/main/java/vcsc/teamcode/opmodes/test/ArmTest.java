package vcsc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import vcsc.teamcode.cmp.arm.extension.actions.A_SetArmExtensionPower;
import vcsc.teamcode.cmp.arm.rotation.actions.A_SetArmRotationPower;
import vcsc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(name = "Arm Test", group = "Test")
public class ArmTest extends BaseOpMode {
    @Override
    public void loop() {
        super.loop();
        double extPower = -gamepad1.left_stick_y;
        double rotPower = gamepad1.right_stick_x;
        A_SetArmRotationPower setArmRotationPower = new A_SetArmRotationPower(rotPower);
        A_SetArmExtensionPower setArmExtensionPower = new A_SetArmExtensionPower(extPower);

        setArmRotationPower.start();
        setArmExtensionPower.start();

        setArmRotationPower.loop();
        setArmExtensionPower.loop();

        telem.addData("Arm Rotation Real Position", armRotState.getRealPosition());
        telem.addData("Arm Rotation Target Position", armRotState.getTargetPosition());
        telem.addData("Arm Extension Real Position", armExtState.getRealPosition());
        telem.addData("Arm Extension Target Position", armExtState.getTargetPosition());
    }
}
