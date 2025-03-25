package vcsc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import vcsc.core.abstracts.state.StateRegistry;
import vcsc.core.util.DcMotorGroup;
import vcsc.teamcode.cmp.claw.ClawState;

@TeleOp(name = "Motor Test", group = "Test")
public class MotorTest extends OpMode {
    DcMotorEx zero, one, two, three;
    DcMotorEx[] motorArr;
    DcMotorGroup motors;
    boolean deb = false;
    int motor = 0;
    @Override
    public void init() {
        zero = hardwareMap.get(DcMotorEx.class, "zero");
        one = hardwareMap.get(DcMotorEx.class, "one");
        two = hardwareMap.get(DcMotorEx.class, "two");
        three = hardwareMap.get(DcMotorEx.class, "three");
        motors = new DcMotorGroup(zero, one, two, three);
        motors.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorArr = new DcMotorEx[]{zero, one, two, three};
    }

    @Override
    public void loop() {
        motors.setPower(0);
        motorArr[motor].setPower(0.5);
        telemetry.addData("Outputting power to motor", motor);
        telemetry.update();
        if (gamepad1.a) {
            if (!deb) {
                motor = (motor + 1) % 4;
                deb = true;
            }
        } else {
            deb = false;
        }
    }
}
