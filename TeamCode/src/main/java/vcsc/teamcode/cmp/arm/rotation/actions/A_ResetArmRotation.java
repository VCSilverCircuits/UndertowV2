package vcsc.teamcode.cmp.arm.rotation.actions;


import com.qualcomm.robotcore.util.ElapsedTime;

public class A_ResetArmRotation extends A_SetArmRotationPower {
    boolean finished = true;
    double RESET_DELAY = 600;
    ElapsedTime resetTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    public A_ResetArmRotation(double power) {
        super(-power);
        if (power < 0) {
            throw new IllegalArgumentException("Power must be positive for rotating down slides.");
        }
    }

    @Override
    public boolean start() {
        finished = false;
        resetTimer.reset();
        return super.start();
    }

    @Override
    public void loop() {
        if (!finished) {
            System.out.println("[A_ResetArmRotation::loop] Trying to rotate slides down....");
            if (resetTimer.time() > RESET_DELAY) {
                System.out.println("[A_ResetArmRotation::loop] Slides rotated down, ending.");
                end();
            }
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    protected void end() {
        state.setPower(this, 0);
        state.reset(this);
        finished = true;
        super.end();
    }
}
