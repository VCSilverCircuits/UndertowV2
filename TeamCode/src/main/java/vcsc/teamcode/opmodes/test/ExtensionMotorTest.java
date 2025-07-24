package vcsc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import vcsc.core.util.DcMotorGroup;


@TeleOp(name = "Extension Motor Test", group = "Test")
public class ExtensionMotorTest extends OpMode {
    DcMotorEx zero, one, two, three;
    DcMotorEx[] motorArr;
    DcMotorGroup motors;
    boolean deb = false;
    int motor = 0;

    @Override
    public void init() {
        zero = hardwareMap.get(DcMotorEx.class, "armExtensionLeft");
        one = hardwareMap.get(DcMotorEx.class, "armExtensionCenter");
        two = hardwareMap.get(DcMotorEx.class, "armExtensionRight");
//        three = hardwareMap.get(DcMotorEx.class, "three");
        zero.setDirection(DcMotorSimple.Direction.REVERSE);
        motors = new DcMotorGroup(zero, one, two);
        motors.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorArr = new DcMotorEx[]{zero, one, two};
    }

    @Override
    public void loop() {
        motors.setPower(0);

        if (gamepad1.b) {
            telemetry.addLine("Outputting power to all motors");
            motors.setPower(-gamepad1.right_stick_y * 3/4);
        } else {
            telemetry.addData("Outputting power to motor", motor);
            motorArr[motor].setPower(-gamepad1.right_stick_y * 3/4);
        }


        telemetry.update();

        if (gamepad1.a) {
            if (!deb) {
                motor = (motor + 1) % 3;
                deb = true;
            }
        } else {
            deb = false;
        }
    }
}
