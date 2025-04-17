package vcsc.teamcode.behavior.specimen;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;

import vcsc.core.abstracts.behavior.Behavior;
import vcsc.core.abstracts.task.TaskSequence;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionPose;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionState;
import vcsc.teamcode.cmp.arm.extension.actions.A_SetArmExtensionPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationState;
import vcsc.teamcode.cmp.arm.rotation.actions.A_SetArmRotationAngle;
import vcsc.teamcode.cmp.claw.ClawPose;
import vcsc.teamcode.cmp.claw.ClawState;
import vcsc.teamcode.cmp.claw.actions.A_SetClawPose;
import vcsc.teamcode.cmp.elbow.ElbowPose;
import vcsc.teamcode.cmp.elbow.ElbowState;
import vcsc.teamcode.cmp.elbow.actions.A_SetElbowPose;
import vcsc.teamcode.cmp.robot.FollowerWrapper;
import vcsc.teamcode.cmp.robot.RobotState;
import vcsc.teamcode.cmp.wrist.hinge.WristHingePose;
import vcsc.teamcode.cmp.wrist.hinge.WristHingeState;
import vcsc.teamcode.cmp.wrist.hinge.actions.A_SetWristHingePose;
import vcsc.teamcode.cmp.wrist.twist.WristTwistPose;
import vcsc.teamcode.cmp.wrist.twist.WristTwistState;
import vcsc.teamcode.cmp.wrist.twist.actions.A_SetWristTwistPose;
import vcsc.teamcode.config.GlobalPose;

public class B_ReleaseSpecimenAndIntakeSpecimenAuto extends Behavior {
    TaskSequence _taskSequence;
    Pose initialPose;
    Follower follower;

    public B_ReleaseSpecimenAndIntakeSpecimenAuto() {
        super();

        addRequirement(ElbowState.class);
        addRequirement(WristHingeState.class);
        addRequirement(WristTwistState.class);
        addRequirement(ArmExtensionState.class);
        addRequirement(ArmRotationState.class);
        addRequirement(ClawState.class);


        // Establish needed actions
        A_SetElbowPose elbowOut = new A_SetElbowPose(ElbowPose.INTAKE_SPECIMEN);
        A_SetWristHingePose hingeBack = new A_SetWristHingePose(WristHingePose.INTAKE_SPECIMEN);
        A_SetWristTwistPose twist = new A_SetWristTwistPose(WristTwistPose.INTAKE_SPECIMEN);
        A_SetClawPose openClaw = new A_SetClawPose(ClawPose.OPEN);

        A_SetArmExtensionPose extendSlides = new A_SetArmExtensionPose(ArmExtensionPose.INTAKE_SPECIMEN);
        A_SetArmRotationAngle rotateArmBack = new A_SetArmRotationAngle(53);

        follower = FollowerWrapper.getFollower();

        // Create Task Sequence
        _taskSequence = new TaskSequence();
        _taskSequence.then(openClaw).then(
                        extendSlides,
                        elbowOut,
                        hingeBack,
                        twist
                ).thenWaitUntil(() -> initialPose.getX() - follower.getPose().getX() > 2)
                .then(rotateArmBack);
    }

    @Override
    public boolean start() {
        super.start();
        RobotState.getInstance().setMode(GlobalPose.INTAKE_SPECIMEN);
        initialPose = follower.getPose();
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
