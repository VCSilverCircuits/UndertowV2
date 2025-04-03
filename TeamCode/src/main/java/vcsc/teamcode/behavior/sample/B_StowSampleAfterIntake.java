package vcsc.teamcode.behavior.sample;

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

public class B_StowSampleAfterIntake extends Behavior {
    TaskSequence _taskSequence;

    public B_StowSampleAfterIntake() {
        super();

//        B_IntakeSampleStraight intakeSampleStraight = new B_IntakeSampleStraight();
        B_StowSample stow = new B_StowSample();

        addRequirement(ArmExtensionState.class);
        addRequirement(ArmRotationState.class);
        addRequirement(ElbowState.class);
        addRequirement(WristHingeState.class);
        addRequirement(WristTwistState.class);
        addRequirement(stow);

        A_SetElbowPose elbowOut = new A_SetElbowPose(ElbowPose.INTAKE_SAMPLE_STRAIGHT);
        A_SetWristHingePose hingeBack = new A_SetWristHingePose(WristHingePose.INTAKE_SAMPLE_STRAIGHT);
        A_SetWristTwistPose twist = new A_SetWristTwistPose(WristTwistPose.INTAKE_SAMPLE_STRAIGHT);


        // Establish needed actions
        A_SetArmExtensionPose retractSlides = new A_SetArmExtensionPose(ArmExtensionPose.STOW_SAMPLE);
        A_SetArmRotationPose rotateArmBack = new A_SetArmRotationPose(ArmRotationPose.STOW_SAMPLE);

        // Create Task Sequence
        _taskSequence = new TaskSequence();
        _taskSequence.then(elbowOut, hingeBack, twist).then(
                rotateArmBack,
                retractSlides
        ).then(stow);
    }

    @Override
    public boolean start() {
        super.start();
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
