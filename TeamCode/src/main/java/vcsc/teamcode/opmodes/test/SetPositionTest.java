package vcsc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import vcsc.teamcode.cmp.arm.extension.actions.A_SetArmExtensionLength;
import vcsc.teamcode.cmp.arm.rotation.actions.A_SetArmRotationAngle;
import vcsc.teamcode.cmp.claw.ClawPose;
import vcsc.teamcode.cmp.claw.actions.A_SetClawPose;
import vcsc.teamcode.cmp.claw.actions.A_SetClawPosition;
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
    public static double clawPosition = 0.5;

    A_SetArmExtensionLength armExtensionLengthAction;
    A_SetArmRotationAngle armRotationAngleAction;
    A_SetElbowAngle elbowAngleAction;
    A_SetWristHingeAngle wristHingeAngleAction;
    A_SetWristTwistAngle wristTwistAngleAction;
    A_SetClawPose clawPoseAction;
    A_SetClawPosition setClawPositionAction;

    @Override
    public void start() {
        super.start();
        clawPoseAction = new A_SetClawPose(SetPositionTest.clawOpen ? ClawPose.OPEN : ClawPose.CLOSED);
    }

    @Override
    public void loop() {
        super.loop();
        armExtensionLengthAction = new A_SetArmExtensionLength(SetPositionTest.armExtensionLength);
        armRotationAngleAction = new A_SetArmRotationAngle(SetPositionTest.armRotationAngle);
        elbowAngleAction = new A_SetElbowAngle(SetPositionTest.elbowAngle);
        wristHingeAngleAction = new A_SetWristHingeAngle(SetPositionTest.wristHingeAngle);
        wristTwistAngleAction = new A_SetWristTwistAngle(SetPositionTest.wristTwistAngle);
        setClawPositionAction = new A_SetClawPosition(SetPositionTest.clawPosition);

//        if (clawPoseAction.isFinished()) {
//            clawPoseAction = new A_SetClawPose(SetPositionTest.clawOpen ? ClawPose.OPEN : ClawPose.CLOSED);
//            clawPoseAction.start();
//        }

        if (SetPositionTest.armExtensionLength != armExtState.getTargetLength()) {
            armExtState.cancelAction();
        }
        if (SetPositionTest.armRotationAngle != armRotState.getTargetAngle()) {
            armRotState.cancelAction();
        }

        telem.addData("Arm Rotation Real Position", armRotState.getRealPosition());
        telem.addData("Arm Rotation Target Position", armRotState.getTargetPosition());

        armExtensionLengthAction.start();
        armRotationAngleAction.start();
        elbowAngleAction.start();
        wristHingeAngleAction.start();
        wristTwistAngleAction.start();

        setClawPositionAction.start();


        armExtensionLengthAction.loop();
        armRotationAngleAction.loop();
        elbowAngleAction.loop();
        wristHingeAngleAction.loop();
        wristTwistAngleAction.loop();
        setClawPositionAction.loop();
    }
}
