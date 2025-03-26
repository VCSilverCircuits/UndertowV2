package vcsc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import vcsc.teamcode.cmp.arm.extension.actions.A_SetArmExtensionLength;
import vcsc.teamcode.cmp.arm.rotation.actions.A_SetArmRotationAngle;
import vcsc.teamcode.cmp.claw.ClawPose;
import vcsc.teamcode.cmp.claw.actions.A_SetClawPose;
import vcsc.teamcode.cmp.elbow.actions.A_SetElbowAngle;
import vcsc.teamcode.cmp.wrist.hinge.actions.A_SetWristHingeAngle;
import vcsc.teamcode.cmp.wrist.twist.actions.A_SetWristTwistAngle;
import vcsc.teamcode.opmodes.base.BaseOpMode;


@Config
@TeleOp(name = "SetPositionTest", group = "Test")
public class SetPositionTest extends BaseOpMode {
    public static double armExtensionLength = 0.5;
    public static double armRotationAngle = 0.5;
    public static double elbowAngle = 0.5;
    public static double wristHingeAngle = 0.5;
    public static double wristTwistAngle = 0.5;
    public static boolean clawOpen = true;

    @Override
    public void loop() {
        super.loop();
        A_SetArmExtensionLength armExtensionLength = new A_SetArmExtensionLength(SetPositionTest.armExtensionLength);
        A_SetArmRotationAngle armRotationAngle = new A_SetArmRotationAngle(SetPositionTest.armRotationAngle);
        A_SetElbowAngle elbowAngle = new A_SetElbowAngle(SetPositionTest.elbowAngle);
        A_SetWristHingeAngle wristHingeAngle = new A_SetWristHingeAngle(SetPositionTest.wristHingeAngle);
        A_SetWristTwistAngle wristTwistAngle = new A_SetWristTwistAngle(SetPositionTest.wristTwistAngle);
        A_SetClawPose clawPose = new A_SetClawPose(SetPositionTest.clawOpen ? ClawPose.OPEN : ClawPose.CLOSED);

        if (SetPositionTest.armExtensionLength != armExtState.getTargetLength()) {
            armExtState.cancelAction();
        }
        if (SetPositionTest.armRotationAngle != armRotState.getTargetAngle()) {
            armRotState.cancelAction();
        }

        telem.addData("Arm Rotation Real Position", armRotState.getRealPosition());
        telem.addData("Arm Rotation Target Position", armRotState.getTargetPosition());

        armExtensionLength.start();
        armRotationAngle.start();
        elbowAngle.start();
        wristHingeAngle.start();
        wristTwistAngle.start();
        clawPose.start();

        armExtensionLength.loop();
        armRotationAngle.loop();
        elbowAngle.loop();
        wristHingeAngle.loop();
        wristTwistAngle.loop();
        clawPose.loop();
    }
}
