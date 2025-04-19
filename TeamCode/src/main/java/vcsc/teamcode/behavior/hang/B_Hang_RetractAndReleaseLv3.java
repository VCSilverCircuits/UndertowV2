package vcsc.teamcode.behavior.hang;

import vcsc.core.abstracts.behavior.Behavior;
import vcsc.core.abstracts.task.TaskSequence;
import vcsc.teamcode.cmp.robot.RobotState;
import vcsc.teamcode.config.GlobalPose;

public class B_Hang_RetractAndReleaseLv3 extends Behavior {
    TaskSequence _taskSequence;

    public B_Hang_RetractAndReleaseLv3() {
        super();

        B_HangLv3P2 hangLv3P2 = new B_HangLv3P2();
        B_HangRelease hangRelease = new B_HangRelease();

        addRequirement(hangLv3P2);
        addRequirement(hangRelease);

        // Create Task Sequence
        _taskSequence = new TaskSequence();
        _taskSequence.then(
                hangLv3P2
        );//.then(hangRelease);
    }

    @Override
    public boolean start() {
        super.start();
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
