package vcsc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ServoImplEx;

@Disabled
@Config
@TeleOp(name = "Servo Test", group = "Test")
public class ServoTest extends OpMode {
    public static double wristHingePos = 0.5;
    public static double wristTwistPos = 0.5;
    public static double elbowPos = 0.5;
    public static double clawPos = 0.5;
    ServoImplEx wristHinge, wristTwist, elbow1, elbow2, claw;
    ServoImplEx[] servos;

    @Override
    public void init() {
        wristHinge = hardwareMap.get(ServoImplEx.class, "wristHinge");
        wristTwist = hardwareMap.get(ServoImplEx.class, "wristTwist");
        elbow1 = hardwareMap.get(ServoImplEx.class, "elbow1");
        elbow2 = hardwareMap.get(ServoImplEx.class, "elbow2");
        claw = hardwareMap.get(ServoImplEx.class, "claw");
    }

    @Override
    public void loop() {
        wristHinge.setPosition(wristHingePos);
        wristTwist.setPosition(wristTwistPos);
        elbow1.setPosition(elbowPos);
        elbow2.setPosition(elbowPos);
        claw.setPosition(clawPos);
    }
}
