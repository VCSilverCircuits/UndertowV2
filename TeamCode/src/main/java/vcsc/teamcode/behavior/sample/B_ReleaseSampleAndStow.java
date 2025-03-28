package vcsc.teamcode.behavior.sample;

import vcsc.core.abstracts.behavior.Behavior;
import vcsc.core.abstracts.task.TaskSequence;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionState;
import vcsc.teamcode.cmp.arm.extension.actions.A_SetArmExtensionGlobalPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationState;
import vcsc.teamcode.cmp.arm.rotation.actions.A_SetArmRotationGlobalPose;
import vcsc.teamcode.cmp.claw.ClawPose;
import vcsc.teamcode.cmp.claw.ClawState;
import vcsc.teamcode.cmp.claw.actions.A_SetClawPose;
import vcsc.teamcode.cmp.elbow.ElbowPose;
import vcsc.teamcode.cmp.elbow.ElbowState;
import vcsc.teamcode.cmp.elbow.actions.A_SetElbowGlobalPose;
import vcsc.teamcode.cmp.elbow.actions.A_SetElbowPose;
import vcsc.teamcode.cmp.robot.RobotState;
import vcsc.teamcode.cmp.wrist.hinge.WristHingePose;
import vcsc.teamcode.cmp.wrist.hinge.WristHingeState;
import vcsc.teamcode.cmp.wrist.hinge.actions.A_SetWristHingeGlobalPose;
import vcsc.teamcode.cmp.wrist.hinge.actions.A_SetWristHingePose;
import vcsc.teamcode.cmp.wrist.twist.WristTwistState;
import vcsc.teamcode.cmp.wrist.twist.actions.A_SetWristTwistGlobalPose;
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
        A_SetElbowGlobalPose elbowOut = new A_SetElbowGlobalPose(GlobalPose.STOW_SAMPLE);
        A_SetElbowPose elbowStraight = new A_SetElbowPose(ElbowPose.STRAIGHT);
        A_SetWristHingeGlobalPose hingeBack = new A_SetWristHingeGlobalPose(GlobalPose.STOW_SAMPLE);
        A_SetWristHingePose hingeStraight = new A_SetWristHingePose(WristHingePose.STRAIGHT);
        A_SetWristTwistGlobalPose twist = new A_SetWristTwistGlobalPose(GlobalPose.STOW_SAMPLE);
        A_SetClawPose openClaw = new A_SetClawPose(ClawPose.OPEN);

        A_SetArmExtensionGlobalPose retractSlides = new A_SetArmExtensionGlobalPose(GlobalPose.STOW_SAMPLE);
        A_SetArmRotationGlobalPose rotateArmDown = new A_SetArmRotationGlobalPose(GlobalPose.STOW_SAMPLE);

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
