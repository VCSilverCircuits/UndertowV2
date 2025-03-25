package vcsc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import vcsc.core.util.DcMotorGroup;

@TeleOp(name = "Arm Test", group = "Test")
public class ArmTest extends OpMode {
    DcMotorEx arm;
    @Override
    public void init() {
        arm = hardwareMap.get(DcMotorEx.class, "armRot");
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {
        arm.setPower(-gamepad1.right_stick_y);
        telemetry.addData("Arm position", arm.getCurrentPosition());
    }
}
