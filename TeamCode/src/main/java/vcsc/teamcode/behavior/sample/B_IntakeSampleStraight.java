package vcsc.teamcode.behavior.sample;

import vcsc.core.abstracts.behavior.Behavior;
import vcsc.core.abstracts.task.TaskSequence;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionState;
import vcsc.teamcode.cmp.arm.extension.actions.A_SetArmExtensionGlobalPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationState;
import vcsc.teamcode.cmp.arm.rotation.actions.A_SetArmRotationGlobalPose;
import vcsc.teamcode.cmp.elbow.ElbowState;
import vcsc.teamcode.cmp.elbow.actions.A_SetElbowGlobalPose;
import vcsc.teamcode.cmp.robot.RobotState;
import vcsc.teamcode.cmp.wrist.hinge.WristHingeState;
import vcsc.teamcode.cmp.wrist.hinge.actions.A_SetWristHingeGlobalPose;
import vcsc.teamcode.cmp.wrist.twist.WristTwistState;
import vcsc.teamcode.cmp.wrist.twist.actions.A_SetWristTwistGlobalPose;
import vcsc.teamcode.config.GlobalPose;

public class B_IntakeSampleStraight extends Behavior {
    TaskSequence _taskSequence;

    public B_IntakeSampleStraight() {
        super();

        addRequirement(ElbowState.class);
        addRequirement(WristHingeState.class);
        addRequirement(WristTwistState.class);
        addRequirement(ArmExtensionState.class);
        addRequirement(ArmRotationState.class);


        // Establish needed actions
        A_SetElbowGlobalPose elbowOut = new A_SetElbowGlobalPose(GlobalPose.INTAKE_SAMPLE_STRAIGHT);
        A_SetWristHingeGlobalPose hingeBack = new A_SetWristHingeGlobalPose(GlobalPose.INTAKE_SAMPLE_STRAIGHT);
        A_SetWristTwistGlobalPose twist = new A_SetWristTwistGlobalPose(GlobalPose.INTAKE_SAMPLE_STRAIGHT);

        A_SetArmExtensionGlobalPose extendSlides = new A_SetArmExtensionGlobalPose(GlobalPose.INTAKE_SAMPLE_STRAIGHT);
        A_SetArmRotationGlobalPose rotateArmBack = new A_SetArmRotationGlobalPose(GlobalPose.INTAKE_SAMPLE_STRAIGHT);

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
        RobotState.getInstance().setMode(GlobalPose.INTAKE_SAMPLE_STRAIGHT);
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
