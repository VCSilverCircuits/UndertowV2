package vcsc.teamcode.behavior.hang;

import vcsc.core.abstracts.behavior.Behavior;
import vcsc.core.abstracts.state.StateRegistry;
import vcsc.core.abstracts.task.DelayTask;
import vcsc.core.abstracts.task.TaskSequence;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionPose;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionState;
import vcsc.teamcode.cmp.arm.extension.actions.A_SetArmExtensionPose;
import vcsc.teamcode.cmp.arm.extension.actions.A_SetArmExtensionPower;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationPose;
import vcsc.teamcode.cmp.arm.rotation.actions.A_SetArmRotationPose;
import vcsc.teamcode.cmp.robot.RobotState;
import vcsc.teamcode.config.GlobalPose;

public class B_HangLv2P2 extends Behavior {
    TaskSequence _taskSequence;

    public B_HangLv2P2() {
        super();

        addRequirement(ArmExtensionState.class);
//        addRequirement(ArmRotationState.class);

        // Establish needed actions
        A_SetArmExtensionPose extendSlides = new A_SetArmExtensionPose(ArmExtensionPose.HANG_LV2_P2);
        A_SetArmRotationPose rotateArmBack = new A_SetArmRotationPose(ArmRotationPose.HANG_LV2_P2);

        A_SetArmExtensionPower slidesInPower = new A_SetArmExtensionPower(-1);
        A_SetArmExtensionPower slidesInStop = new A_SetArmExtensionPower(0);

        DelayTask delay = new DelayTask(1700);

        ArmExtensionState extState = StateRegistry.getInstance().getState(ArmExtensionState.class);

        // Create Task Sequence
        _taskSequence = new TaskSequence();
        _taskSequence.thenAsync(slidesInPower, delay)
                .thenWaitUntil(
                        () -> extState.getRealLength() <= ArmExtensionPose.HANG_LV2_P2.getLength() || extState.isTouching() || delay.isFinished()
                );
    }

    @Override
    public boolean start() {
        super.start();
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
