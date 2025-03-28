package vcsc.teamcode.behavior.sample;

import vcsc.core.abstracts.behavior.Behavior;
import vcsc.core.abstracts.task.TaskSequence;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionState;
import vcsc.teamcode.cmp.arm.extension.actions.A_SetArmExtensionGlobalPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationState;
import vcsc.teamcode.cmp.arm.rotation.actions.A_SetArmRotationGlobalPose;
import vcsc.teamcode.cmp.robot.RobotState;
import vcsc.teamcode.config.GlobalPose;

public class B_StowSampleAfterIntake extends Behavior {
    TaskSequence _taskSequence;

    public B_StowSampleAfterIntake() {
        super();

        B_IntakeSampleStraight intakeSampleStraight = new B_IntakeSampleStraight();
        B_StowSample stow = new B_StowSample();

        addRequirement(ArmExtensionState.class);
        addRequirement(ArmRotationState.class);
        addRequirement(intakeSampleStraight);
        addRequirement(stow);


        // Establish needed actions
        A_SetArmExtensionGlobalPose retractSlides = new A_SetArmExtensionGlobalPose(GlobalPose.STOW_SAMPLE);
        A_SetArmRotationGlobalPose rotateArmBack = new A_SetArmRotationGlobalPose(GlobalPose.STOW_SAMPLE);

        // Create Task Sequence
        _taskSequence = new TaskSequence();
        _taskSequence.then(intakeSampleStraight).then(
                rotateArmBack,
                retractSlides
        ).then(stow);
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
