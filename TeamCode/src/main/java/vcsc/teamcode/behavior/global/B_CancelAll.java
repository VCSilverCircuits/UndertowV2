package vcsc.teamcode.behavior.global;

import vcsc.core.abstracts.action.A_Cancel;
import vcsc.core.abstracts.behavior.Behavior;
import vcsc.core.abstracts.task.TaskManager;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionState;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationState;
import vcsc.teamcode.cmp.claw.ClawState;
import vcsc.teamcode.cmp.elbow.ElbowState;
import vcsc.teamcode.cmp.wrist.hinge.WristHingeState;
import vcsc.teamcode.cmp.wrist.twist.WristTwistState;

public class B_CancelAll extends Behavior {
    A_Cancel<ArmExtensionState> cancelExtension;
    A_Cancel<ArmRotationState> cancelRotation;
    A_Cancel<ElbowState> cancelElbow;
    A_Cancel<WristHingeState> cancelHinge;
    A_Cancel<WristTwistState> cancelTwist;
    A_Cancel<ClawState> cancelClaw;

    public B_CancelAll() {
        cancelExtension = new A_Cancel<>(ArmExtensionState.class);
        cancelRotation = new A_Cancel<>(ArmRotationState.class);
        cancelElbow = new A_Cancel<>(ElbowState.class);
        cancelHinge = new A_Cancel<>(WristHingeState.class);
        cancelTwist = new A_Cancel<>(WristTwistState.class);
        cancelClaw = new A_Cancel<>(ClawState.class);
    }

    @Override
    public boolean start() {
        super.start();
        cancelExtension.start();
        cancelRotation.start();
        cancelElbow.start();
        cancelHinge.start();
        cancelTwist.start();
        cancelClaw.start();
        TaskManager.getInstance().clearTasks();
        return true;
    }

    @Override
    public void loop() {

    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
