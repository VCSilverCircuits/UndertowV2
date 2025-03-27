package vcsc.teamcode.behavior.hang;

import vcsc.core.abstracts.behavior.Behavior;
import vcsc.core.abstracts.task.TaskSequence;
import vcsc.teamcode.cmp.RobotState;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionPose;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionState;
import vcsc.teamcode.cmp.arm.extension.actions.A_SetArmExtensionPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationState;
import vcsc.teamcode.cmp.arm.rotation.actions.A_SetArmRotationPose;
import vcsc.teamcode.config.GlobalPose;

public class B_HangLv2P2 extends Behavior {
    TaskSequence _taskSequence;

    public B_HangLv2P2() {
        super();

        addRequirement(ArmExtensionState.class);
        addRequirement(ArmRotationState.class);

        // Establish needed actions
        A_SetArmExtensionPose extendSlides = new A_SetArmExtensionPose(ArmExtensionPose.HANG_LV2_P2);
        A_SetArmRotationPose rotateArmBack = new A_SetArmRotationPose(ArmRotationPose.HANG_LV2_P2);

        // Create Task Sequence
        _taskSequence = new TaskSequence();
        _taskSequence.then(
                rotateArmBack,
                extendSlides
        );
    }

    @Override
    public boolean start() {
        RobotState.getInstance().setMode(GlobalPose.HANG_LV2_P2);
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
