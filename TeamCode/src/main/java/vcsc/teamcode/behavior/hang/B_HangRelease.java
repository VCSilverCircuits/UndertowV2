package vcsc.teamcode.behavior.hang;

import vcsc.core.abstracts.behavior.Behavior;
import vcsc.core.abstracts.task.TaskSequence;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionState;
import vcsc.teamcode.cmp.arm.extension.actions.A_SetArmExtensionGlobalPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationState;
import vcsc.teamcode.cmp.arm.rotation.actions.A_SetArmRotationGlobalPose;
import vcsc.teamcode.cmp.robot.RobotState;
import vcsc.teamcode.config.GlobalPose;

public class B_HangRelease extends Behavior {
    TaskSequence _taskSequence;

    public B_HangRelease() {
        super();

        addRequirement(ArmExtensionState.class);
        addRequirement(ArmRotationState.class);

        // Establish needed actions
        A_SetArmExtensionGlobalPose extendSlides = new A_SetArmExtensionGlobalPose(GlobalPose.HANG_RELEASE);
        A_SetArmRotationGlobalPose rotateArmBack = new A_SetArmRotationGlobalPose(GlobalPose.HANG_RELEASE);

        // Create Task Sequence
        _taskSequence = new TaskSequence();
        _taskSequence.then(
                rotateArmBack,
                extendSlides
        );
    }

    @Override
    public boolean start() {
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
