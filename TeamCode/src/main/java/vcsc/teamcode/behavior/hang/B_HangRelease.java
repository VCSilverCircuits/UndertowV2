package vcsc.teamcode.behavior.hang;

import vcsc.core.abstracts.behavior.Behavior;
import vcsc.core.abstracts.task.TaskSequence;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionPose;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionState;
import vcsc.teamcode.cmp.arm.extension.actions.A_SetArmExtensionPose;
import vcsc.teamcode.cmp.arm.extension.actions.A_SetArmExtensionPower;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationState;
import vcsc.teamcode.cmp.arm.rotation.actions.A_SetArmRotationPose;
import vcsc.teamcode.cmp.robot.RobotState;
import vcsc.teamcode.config.GlobalPose;

public class B_HangRelease extends Behavior {
    TaskSequence _taskSequence;

    public B_HangRelease() {
        super();

        addRequirement(ArmExtensionState.class);
        addRequirement(ArmRotationState.class);

        // Establish needed actions
        A_SetArmExtensionPose extendSlides = new A_SetArmExtensionPose(ArmExtensionPose.HANG_RELEASE);
        A_SetArmRotationPose rotateArmBack = new A_SetArmRotationPose(ArmRotationPose.HANG_RELEASE);
        A_SetArmRotationPose rotateArmLv2 = new A_SetArmRotationPose(ArmRotationPose.HANG_LV2_P2);
        A_SetArmExtensionPower slidesInStop = new A_SetArmExtensionPower(0);

        // Create Task Sequence
        _taskSequence = new TaskSequence();
        _taskSequence.thenAsync(rotateArmBack).thenDelay(750).then(slidesInStop).then(extendSlides);
    }

    @Override
    public boolean start() {
        super.start();
        RobotState.getInstance().setMode(GlobalPose.HANG_RELEASE);
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
