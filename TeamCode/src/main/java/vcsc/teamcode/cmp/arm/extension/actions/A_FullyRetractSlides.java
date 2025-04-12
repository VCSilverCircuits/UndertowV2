package vcsc.teamcode.cmp.arm.extension.actions;

public class A_FullyRetractSlides extends A_SetArmExtensionPower {
    boolean finished = true;

    public A_FullyRetractSlides(double power) {
        super(-power);
        if (power < 0) {
            throw new IllegalArgumentException("Power must be positive for fully retracting slides.");
        }
    }

    @Override
    public boolean start() {
        finished = false;
        return super.start();
    }

    @Override
    public void loop() {
        if (!finished) {
            System.out.println("[A_FullyRetractSlides::loop] Trying to retract slides....");
            if (state.isTouching()) {
                System.out.println("[A_FullyRetractSlides::loop] Retracted, ending.");
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
