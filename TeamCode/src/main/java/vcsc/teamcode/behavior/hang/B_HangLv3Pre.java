package vcsc.teamcode.behavior.hang;

import vcsc.core.abstracts.behavior.Behavior;
import vcsc.core.abstracts.state.StateRegistry;
import vcsc.core.abstracts.task.TaskSequence;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionPose;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionState;
import vcsc.teamcode.cmp.arm.extension.actions.A_SetArmExtensionPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationPose;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationState;
import vcsc.teamcode.cmp.arm.rotation.actions.A_SetArmRotationPose;
import vcsc.teamcode.cmp.arm.rotation.actions.A_SetArmRotationPower;
import vcsc.teamcode.cmp.claw.ClawState;
import vcsc.teamcode.cmp.claw.actions.A_CloseClaw;
import vcsc.teamcode.cmp.elbow.ElbowPose;
import vcsc.teamcode.cmp.elbow.ElbowState;
import vcsc.teamcode.cmp.elbow.actions.A_SetElbowAngle;
import vcsc.teamcode.cmp.elbow.actions.A_SetElbowPose;
import vcsc.teamcode.cmp.wrist.hinge.WristHingePose;
import vcsc.teamcode.cmp.wrist.hinge.WristHingeState;
import vcsc.teamcode.cmp.wrist.hinge.actions.A_SetWristHingePose;

public class B_HangLv3Pre extends Behavior {
    TaskSequence _taskSequence;

    public B_HangLv3Pre() {
        super();
        addRequirement(ArmExtensionState.class);
        addRequirement(ArmRotationState.class);
        addRequirement(ElbowState.class);
        addRequirement(WristHingeState.class);
        addRequirement(ClawState.class);


        // Establish needed actions
        A_SetArmExtensionPose extendSlides = new A_SetArmExtensionPose(ArmExtensionPose.PRE_LV3_HANG);
        A_SetArmRotationPose rotateArmBack = new A_SetArmRotationPose(ArmRotationPose.PRE_LV3_HANG);
        A_SetWristHingePose wristHingeBack = new A_SetWristHingePose(WristHingePose.PRE_LV3_HANG);
        A_SetElbowPose elbowBack = new A_SetElbowPose(ElbowPose.PRE_LV3_HANG);

        A_CloseClaw closeClaw = new A_CloseClaw();

        A_SetElbowAngle elbowStraight = new A_SetElbowAngle(0.75);
        A_SetWristHingePose hingeStraight = new A_SetWristHingePose(WristHingePose.STRAIGHT);

        A_SetArmRotationPower rotateBackPower = new A_SetArmRotationPower(1);
        A_SetArmRotationPower rotateBackStop = new A_SetArmRotationPower(0);

        ArmRotationState armRotationState = StateRegistry.getInstance().getState(ArmRotationState.class);

        // Create Task Sequence
        _taskSequence = new TaskSequence();
        _taskSequence.then(closeClaw, rotateBackPower, rotateBackPower)
                .thenWaitUntil(
                        () -> armRotationState.getRealAngle() > 110.0
                )
                .then(
                        extendSlides
                )
                .thenWaitUntil(
                        () -> armRotationState.getRealAngle() > 130.0
                )
                .then(elbowStraight, hingeStraight)
                .thenDelay(1500)
                .then(elbowBack, wristHingeBack)
                .then(rotateBackStop);
    }

    @Override
    public boolean start() {
        boolean started = super.start();
        if (started) {
            _taskSequence.start();
        }
        return started;
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
