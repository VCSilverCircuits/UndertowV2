package vcsc.teamcode.behavior.sample;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;

import vcsc.core.abstracts.behavior.Behavior;
import vcsc.core.abstracts.state.StateRegistry;
import vcsc.core.abstracts.task.TaskSequence;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionPose;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionState;
import vcsc.teamcode.cmp.arm.extension.actions.A_SetArmExtensionPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationState;
import vcsc.teamcode.cmp.arm.rotation.actions.A_SetArmRotationPose;
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

public class B_ReleaseSampleAndStow extends Behavior {
    TaskSequence _taskSequence;
    Pose initialPose;
    Follower follower;

    public B_ReleaseSampleAndStow() {
        super();

        addRequirement(ElbowState.class);
        addRequirement(WristHingeState.class);
        addRequirement(WristTwistState.class);
        addRequirement(ArmExtensionState.class);
        addRequirement(ArmRotationState.class);
        addRequirement(ClawState.class);


        // Establish needed actions
        A_SetElbowPose elbowStow = new A_SetElbowPose(ElbowPose.STOW_SAMPLE);
        A_SetElbowPose elbowStraight = new A_SetElbowPose(ElbowPose.STRAIGHT);
        A_SetWristHingePose hingeBack = new A_SetWristHingePose(WristHingePose.STOW_SAMPLE);
        A_SetWristHingePose hingeStraight = new A_SetWristHingePose(WristHingePose.STRAIGHT);
        A_SetWristTwistPose twist = new A_SetWristTwistPose(WristTwistPose.STOW_SAMPLE);
        A_SetClawPose openClaw = new A_SetClawPose(ClawPose.OPEN);

        A_SetArmExtensionPose retractSlides = new A_SetArmExtensionPose(ArmExtensionPose.STOW_SAMPLE);
        A_SetArmRotationPose rotateArmDown = new A_SetArmRotationPose(ArmRotationPose.STOW_SAMPLE);

        ArmExtensionState extState = StateRegistry.getInstance().getState(ArmExtensionState.class);

        follower = FollowerWrapper.getFollower();

        // Create Task Sequence
        _taskSequence = new TaskSequence();
        _taskSequence.then(openClaw)
                .thenWaitUntil(() -> drivenDistance() > 5 || follower.isLocalizationNAN())
                .then(elbowStow, hingeStraight)
                .then(retractSlides, twist)
//                .thenWaitUntil(() -> extState.getRealLength() < 30)
                .then(rotateArmDown, hingeBack)
                .thenRunnable(() -> RobotState.getInstance().setMode(GlobalPose.STOW_SAMPLE))
        ;
    }

    private double drivenDistance() {
        Pose deltaPose = follower.getPose();
        deltaPose.subtract(initialPose);
        return deltaPose.getVector().getMagnitude();
    }

    @Override
    public boolean start() {
        super.start();
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
