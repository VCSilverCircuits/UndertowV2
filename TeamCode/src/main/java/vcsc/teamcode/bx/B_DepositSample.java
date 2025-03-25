package vcsc.teamcode.bx;

import vcsc.core.abstracts.behavior.Behavior;
import vcsc.core.abstracts.task.TaskSequence;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionPose;
import vcsc.teamcode.cmp.arm.extension.actions.A_SetArmExtensionPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationPose;
import vcsc.teamcode.cmp.arm.rotation.actions.A_SetArmRotationPose;
import vcsc.teamcode.cmp.elbow.ElbowPose;
import vcsc.teamcode.cmp.elbow.actions.A_SetElbowPose;
import vcsc.teamcode.cmp.wrist.hinge.WristHingePose;
import vcsc.teamcode.cmp.wrist.hinge.actions.A_SetWristHingePose;
import vcsc.teamcode.cmp.wrist.twist.WristTwistPose;
import vcsc.teamcode.cmp.wrist.twist.actions.A_SetWristTwistPose;

public class B_DepositSample extends Behavior {
    TaskSequence _taskSequence;

    public B_DepositSample() {
        super();

        // Establish needed actions
        A_SetElbowPose elbowOut = new A_SetElbowPose(ElbowPose.DEPOSIT_SAMPLE);
        A_SetWristHingePose hingeBack = new A_SetWristHingePose(WristHingePose.DEPOSIT_SAMPLE);
        A_SetWristTwistPose twist = new A_SetWristTwistPose(WristTwistPose.DEPOSIT_SAMPLE);

        A_SetArmExtensionPose extendSlides = new A_SetArmExtensionPose(ArmExtensionPose.DEPOSIT_SAMPLE);
        A_SetArmRotationPose rotateArmBack = new A_SetArmRotationPose(ArmRotationPose.DEPOSIT_SAMPLE);

        // Create Task Sequence
        _taskSequence = new TaskSequence();
        _taskSequence.then(
                rotateArmBack,
                extendSlides,
                elbowOut,
                hingeBack,
                twist
        );
    }

    @Override
    public boolean start() {
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
