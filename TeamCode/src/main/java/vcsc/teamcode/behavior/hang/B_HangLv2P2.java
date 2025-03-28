package vcsc.teamcode.behavior.hang;

import vcsc.core.abstracts.behavior.Behavior;
import vcsc.core.abstracts.task.TaskSequence;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionState;
import vcsc.teamcode.cmp.arm.extension.actions.A_SetArmExtensionGlobalPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationState;
import vcsc.teamcode.cmp.robot.RobotState;
import vcsc.teamcode.config.GlobalPose;

public class B_HangLv2P2 extends Behavior {
    TaskSequence _taskSequence;

    public B_HangLv2P2() {
        super();

        addRequirement(ArmExtensionState.class);
        addRequirement(ArmRotationState.class);

        // Establish needed actions
        A_SetArmExtensionGlobalPose extendSlides = new A_SetArmExtensionGlobalPose(GlobalPose.HANG_LV2_P2);
        A_SetArmExtensionGlobalPose rotateArmBack = new A_SetArmExtensionGlobalPose(GlobalPose.HANG_LV2_P2);

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
