package vcsc.teamcode.behavior.specimen;

import vcsc.core.abstracts.behavior.Behavior;
import vcsc.core.abstracts.task.DelayTask;
import vcsc.core.abstracts.task.TaskSequence;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionPose;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionState;
import vcsc.teamcode.cmp.arm.extension.actions.A_SetArmExtensionPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationState;
import vcsc.teamcode.cmp.arm.rotation.actions.A_SetArmRotationAngle;
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

public class B_GrabSpecimenAndStowAuto extends Behavior {
    TaskSequence _taskSequence;

    public B_GrabSpecimenAndStowAuto() {
        super();

        addRequirement(ElbowState.class);
        addRequirement(WristHingeState.class);
        addRequirement(WristTwistState.class);
        addRequirement(ArmExtensionState.class);
        addRequirement(ArmRotationState.class);
        addRequirement(ClawState.class);


        // Establish needed actions
        A_SetElbowPose elbowOut = new A_SetElbowPose(ElbowPose.STOW_SPECIMEN);
        A_SetWristHingePose hingeBack = new A_SetWristHingePose(WristHingePose.STOW_SPECIMEN);
        A_SetWristTwistPose twist = new A_SetWristTwistPose(WristTwistPose.STOW_SPECIMEN);
        A_SetClawPose closeClaw = new A_SetClawPose(ClawPose.CLOSED);
        DelayTask armRotateDelay = new DelayTask(100);

        A_SetArmRotationAngle rotateArmIntoWall = new A_SetArmRotationAngle(53 + 5);
        A_SetArmExtensionPose extendSlides = new A_SetArmExtensionPose(ArmExtensionPose.STOW_SPECIMEN);
        A_SetArmRotationPose rotateArmBack = new A_SetArmRotationPose(ArmRotationPose.STOW_SPECIMEN);

        // Create Task Sequence
        _taskSequence = new TaskSequence();
        _taskSequence.thenAsync(rotateArmIntoWall, armRotateDelay)
                .thenWaitUntil(() -> rotateArmIntoWall.isFinished() || armRotateDelay.isFinished())
                .then(closeClaw)
                .thenDelay(100)
                .then(elbowOut)
                .thenDelay(100).then(
                        rotateArmBack,
                        extendSlides,
                        hingeBack,
                        twist
                );
    }

    @Override
    public boolean start() {
        super.start();
        RobotState.getInstance().setMode(GlobalPose.STOW_SPECIMEN);
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
