package vcsc.teamcode.behavior.sample;

import vcsc.core.abstracts.behavior.Behavior;
import vcsc.core.abstracts.task.TaskSequence;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionPose;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionState;
import vcsc.teamcode.cmp.arm.extension.actions.A_SetArmExtensionPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationState;
import vcsc.teamcode.cmp.arm.rotation.actions.A_SetArmRotationPose;
import vcsc.teamcode.cmp.claw.ClawPose;
import vcsc.teamcode.cmp.claw.ClawState;
import vcsc.teamcode.cmp.claw.actions.A_SetClawPose;
import vcsc.teamcode.cmp.elbow.ElbowPose;
import vcsc.teamcode.cmp.elbow.ElbowState;
import vcsc.teamcode.cmp.elbow.actions.A_SetElbowPose;
import vcsc.teamcode.cmp.robot.RobotState;
import vcsc.teamcode.cmp.wrist.hinge.WristHingePose;
import vcsc.teamcode.cmp.wrist.hinge.WristHingeState;
import vcsc.teamcode.cmp.wrist.hinge.actions.A_SetWristHingePose;
import vcsc.teamcode.cmp.wrist.twist.WristTwistPose;
import vcsc.teamcode.cmp.wrist.twist.WristTwistState;
import vcsc.teamcode.cmp.wrist.twist.actions.A_SetWristTwistPose;
import vcsc.teamcode.config.GlobalPose;

public class B_ReleaseSampleAndStow extends Behavior {
    TaskSequence _taskSequence;

    public B_ReleaseSampleAndStow() {
        super();

        addRequirement(ElbowState.class);
        addRequirement(WristHingeState.class);
        addRequirement(WristTwistState.class);
        addRequirement(ArmExtensionState.class);
        addRequirement(ArmRotationState.class);
        addRequirement(ClawState.class);


        // Establish needed actions
        A_SetElbowPose elbowOut = new A_SetElbowPose(ElbowPose.STOW_SAMPLE);
        A_SetElbowPose elbowStraight = new A_SetElbowPose(ElbowPose.STRAIGHT);
        A_SetWristHingePose hingeBack = new A_SetWristHingePose(WristHingePose.STOW_SAMPLE);
        A_SetWristHingePose hingeStraight = new A_SetWristHingePose(WristHingePose.STRAIGHT);
        A_SetWristTwistPose twist = new A_SetWristTwistPose(WristTwistPose.STOW_SAMPLE);
        A_SetClawPose openClaw = new A_SetClawPose(ClawPose.OPEN);

        A_SetArmExtensionPose retractSlides = new A_SetArmExtensionPose(ArmExtensionPose.STOW_SAMPLE);
        A_SetArmRotationPose rotateArmDown = new A_SetArmRotationPose(ArmRotationPose.STOW_SAMPLE);

        // Create Task Sequence
        _taskSequence = new TaskSequence();
        _taskSequence.then(openClaw).then(elbowStraight, hingeStraight).then(
                retractSlides,
                twist
        ).then(rotateArmDown, elbowOut, hingeBack);
    }

    @Override
    public boolean start() {
        RobotState.getInstance().setMode(GlobalPose.STOW_SAMPLE);
        return _taskSequence.start();
    }

    @Override
    public void loop() {
        _taskSequence.loop();
    }

    @Override
    public boolean isFinished() {
        return _taskSequence.isFinished();
    }


}
