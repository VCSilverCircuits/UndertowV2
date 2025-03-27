package vcsc.teamcode.behavior.sample;

import vcsc.core.abstracts.behavior.Behavior;
import vcsc.core.abstracts.task.TaskSequence;
import vcsc.teamcode.cmp.robot.RobotState;
import vcsc.teamcode.config.GlobalPose;

public class B_IntakeSample extends Behavior {
    TaskSequence _taskSequence;

    public B_IntakeSample() {
        super();

        B_IntakeSampleStraight intakeSampleStraight = new B_IntakeSampleStraight();
        B_IntakeSampleHover intakeSampleHover = new B_IntakeSampleHover();

        addRequirement(intakeSampleStraight);
        addRequirement(intakeSampleHover);

        // Create Task Sequence
        _taskSequence = new TaskSequence();
        _taskSequence.then(intakeSampleStraight)
                .then(intakeSampleHover);
    }

    @Override
    public boolean start() {
        RobotState.getInstance().setMode(GlobalPose.INTAKE_SAMPLE_HOVER);
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
