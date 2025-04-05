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
import vcsc.teamcode.cmp.elbow.ElbowPose;
import vcsc.teamcode.cmp.elbow.ElbowState;
import vcsc.teamcode.cmp.elbow.actions.A_SetElbowPose;
import vcsc.teamcode.cmp.robot.RobotState;
import vcsc.teamcode.cmp.wrist.hinge.WristHingePose;
import vcsc.teamcode.cmp.wrist.hinge.WristHingeState;
import vcsc.teamcode.cmp.wrist.hinge.actions.A_SetWristHingeAngle;
import vcsc.teamcode.cmp.wrist.hinge.actions.A_SetWristHingePose;
import vcsc.teamcode.cmp.wrist.twist.WristTwistPose;
import vcsc.teamcode.cmp.wrist.twist.WristTwistState;
import vcsc.teamcode.cmp.wrist.twist.actions.A_SetWristTwistPose;
import vcsc.teamcode.config.GlobalPose;

public class B_DepositSampleUpper extends Behavior {
    TaskSequence _taskSequence;
    Pose initialPose;

    public B_DepositSampleUpper() {
        super();

        addRequirement(ElbowState.class);
        addRequirement(WristHingeState.class);
        addRequirement(WristTwistState.class);
        addRequirement(ArmExtensionState.class);
        addRequirement(ArmRotationState.class);


        // Establish needed actions
        A_SetElbowPose elbowOut = new A_SetElbowPose(ElbowPose.DEPOSIT_SAMPLE_UPPER);
        A_SetWristHingePose hingeBack = new A_SetWristHingePose(WristHingePose.DEPOSIT_SAMPLE_UPPER);
        A_SetWristTwistPose twist = new A_SetWristTwistPose(WristTwistPose.DEPOSIT_SAMPLE_UPPER);
        A_SetWristHingeAngle hingeAway = new A_SetWristHingeAngle(0.35);

        A_SetArmExtensionPose extendSlides = new A_SetArmExtensionPose(ArmExtensionPose.DEPOSIT_SAMPLE_UPPER);
        A_SetArmExtensionPose slidesIn = new A_SetArmExtensionPose(ArmExtensionPose.STOW_SAMPLE);
        A_SetArmRotationPose rotateArmBack = new A_SetArmRotationPose(ArmRotationPose.DEPOSIT_SAMPLE_UPPER);

        ArmExtensionState extState = StateRegistry.getInstance().getState(ArmExtensionState.class);
        ArmRotationState rotState = StateRegistry.getInstance().getState(ArmRotationState.class);

        // Create Task Sequence
        _taskSequence = new TaskSequence();
        _taskSequence.then(slidesIn, hingeAway).thenAsync(
                rotateArmBack,
                elbowOut,
                twist
        ).thenWaitUntil(() -> rotState.getRealAngle() > 80).thenAsync(extendSlides).thenWaitUntil(() -> extState.getRealLength() > extState.getTargetLength() - 5).then(hingeBack);
    }


    @Override
    public boolean start() {
        super.start();
        RobotState.getInstance().setMode(GlobalPose.DEPOSIT_SAMPLE_UPPER);
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
