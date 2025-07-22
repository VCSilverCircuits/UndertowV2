package vcsc.teamcode.cmp.arm.rotation.actions;


import com.qualcomm.robotcore.util.ElapsedTime;

public class A_ResetArmRotation extends A_SetArmRotationPower {
    boolean finished = true;
    boolean finishedMovement = true;
    double MOVEMENT_DELAY = 600;
    double RESET_DELAY = 80;
    ElapsedTime movementTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
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
        finishedMovement = false;
        movementTimer.reset();
        return super.start();
    }

    @Override
    public void loop() {
        if (!finished) {
            if (resetTimer.time() > MOVEMENT_DELAY && !finishedMovement) {
                movementTimer.reset();
                state.setPower(this, 0);
                finishedMovement = true;
                System.out.println("[A_ResetArmRotation::loop] Slides rotated down, starting reset timer.");
            } else if (finishedMovement && resetTimer.time() > RESET_DELAY) {
                System.out.println("[A_ResetArmRotation::loop] Reset timer elapsed, finishing reset..");
                end();
            } else {
                System.out.println("[A_ResetArmRotation::loop] Trying to rotate slides down....");
            }
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void cancel() {
        end();
    }

    @Override
    protected void end() {
        try {
            state.reset(this);
        }
        catch (IllegalStateException e) {
            System.out.println("[A_ResetArmRotation::end] Failed to reset state: " + e.getMessage());
        }
        finished = true;
        finishedMovement = true;
        super.end();
    }
}
