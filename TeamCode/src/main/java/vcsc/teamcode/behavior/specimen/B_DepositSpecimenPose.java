package vcsc.teamcode.behavior.specimen;

import vcsc.core.abstracts.behavior.Behavior;
import vcsc.core.abstracts.task.TaskSequence;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionPose;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionState;
import vcsc.teamcode.cmp.arm.extension.actions.A_SetArmExtensionPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationState;
import vcsc.teamcode.cmp.arm.rotation.actions.A_SetArmRotationPose;
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

public class B_DepositSpecimenPose extends Behavior {
    TaskSequence _taskSequence;

    public B_DepositSpecimenPose() {
        super();

        addRequirement(ElbowState.class);
        addRequirement(WristHingeState.class);
        addRequirement(WristTwistState.class);
        addRequirement(ArmExtensionState.class);
        addRequirement(ArmRotationState.class);


        // Establish needed actions
        A_SetElbowPose elbowOut = new A_SetElbowPose(ElbowPose.DEPOSIT_SPECIMEN);
        A_SetWristHingePose hingeBack = new A_SetWristHingePose(WristHingePose.DEPOSIT_SPECIMEN);
        A_SetWristTwistPose twist = new A_SetWristTwistPose(WristTwistPose.DEPOSIT_SPECIMEN);

        A_SetArmExtensionPose extendSlides = new A_SetArmExtensionPose(ArmExtensionPose.DEPOSIT_SPECIMEN);
        A_SetArmRotationPose rotateArmBack = new A_SetArmRotationPose(ArmRotationPose.DEPOSIT_SPECIMEN);

        // Create Task Sequence
        _taskSequence = new TaskSequence();
        _taskSequence.then(
                rotateArmBack,
                extendSlides
        ).then(elbowOut,hingeBack,twist);
    }

    @Override
    public boolean start() {
        super.start();
        RobotState.getInstance().setMode(GlobalPose.DEPOSIT_SPECIMEN);
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
