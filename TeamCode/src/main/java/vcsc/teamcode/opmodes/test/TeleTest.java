package vcsc.teamcode.opmodes.test;

import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import vcsc.core.abstracts.task.TaskSequence;
import vcsc.core.util.gamepad.BindingSet;
import vcsc.core.util.gamepad.GamepadButton;
import vcsc.teamcode.behavior.global.B_CancelAll;
import vcsc.teamcode.behavior.hang.B_HangLv2Both;
import vcsc.teamcode.behavior.hang.B_HangPre;
import vcsc.teamcode.behavior.sample.B_DepositSampleLower;
import vcsc.teamcode.behavior.sample.B_DepositSampleUpper;
import vcsc.teamcode.behavior.sample.B_IntakeSample;
import vcsc.teamcode.behavior.sample.B_IntakeSampleGrab;
import vcsc.teamcode.behavior.sample.B_IntakeSampleHover;
import vcsc.teamcode.behavior.sample.B_ReleaseSampleAndStow;
import vcsc.teamcode.behavior.sample.B_StowSample;
import vcsc.teamcode.behavior.sample.B_StowSampleAfterIntake;
import vcsc.teamcode.behavior.specimen.B_DepositSpecimen;
import vcsc.teamcode.behavior.specimen.B_IntakeSpecimen;
import vcsc.teamcode.behavior.specimen.B_ReleaseSpecimenAndStow;
import vcsc.teamcode.behavior.specimen.B_StowAfterIntakeSpecimen;
import vcsc.teamcode.cmp.robot.RobotState;
import vcsc.teamcode.cmp.wrist.twist.WristTwistPose;
import vcsc.teamcode.cmp.wrist.twist.actions.A_SetWristTwistAngle;
import vcsc.teamcode.config.GlobalPose;
import vcsc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(name = "Tele Test", group = "Test")
public class TeleTest extends BaseOpMode {
    double wristRotateSpeed = 0.03;

    @Override
    public void init() {
        super.init();

        //region Controller 1

        // region Default Bindings

        /* =============
        DEFAULT BINDINGS
        ================ */

        BindingSet GP1_defaultBindings = new BindingSet();
        bindingManager.setGamepad1Bindings(GlobalPose.DEFAULT, GP1_defaultBindings);

        // ===== Intake & Output =====
        // Samples (triggers)
        GP1_defaultBindings.bindTask(GamepadButton.RIGHT_TRIGGER, new B_IntakeSample());
        GP1_defaultBindings.bindTask(GamepadButton.LEFT_TRIGGER, new B_DepositSampleUpper());
        GP1_defaultBindings.bindTask(GamepadButton.Y, new B_DepositSampleLower());

        // Specimens (bumpers)
        GP1_defaultBindings.bindTask(GamepadButton.RIGHT_BUMPER, new B_IntakeSpecimen());
        GP1_defaultBindings.bindTask(GamepadButton.LEFT_BUMPER, new B_DepositSpecimen());

        TaskSequence cancelAndStow = new TaskSequence();
        cancelAndStow.then(new B_CancelAll()).then(new B_StowSample());

        // Cancel Button
        GP1_defaultBindings.bindTasks(GamepadButton.B, cancelAndStow);

        //endregion

        //region Intake Sample Bindings

        /* ===================
        INTAKE SAMPLE BINDINGS
        ======================

        Inherits From: Default Bindings

        Active Modes:   INTAKE_SAMPLE_HOVER,
                        INTAKE_SAMPLE_STRAIGHT,
                        INTAKE_SAMPLE_CAMERA_SEARCH,
                        INTAKE_SAMPLE_GRAB

        */

        BindingSet GP1_intakeSampleBindings = new BindingSet(GP1_defaultBindings);
        GP1_intakeSampleBindings.bindTask(GamepadButton.RIGHT_TRIGGER, new B_StowSampleAfterIntake());
        GP1_intakeSampleBindings.bindTask(GamepadButton.A, new B_IntakeSampleGrab());

        bindingManager.setGamepad1Bindings(GlobalPose.INTAKE_SAMPLE_HOVER, GP1_intakeSampleBindings);
        bindingManager.setGamepad1Bindings(GlobalPose.INTAKE_SAMPLE_STRAIGHT, GP1_intakeSampleBindings);
        bindingManager.setGamepad1Bindings(GlobalPose.INTAKE_SAMPLE_CAMERA_SEARCH, GP1_intakeSampleBindings);
        bindingManager.setGamepad1Bindings(GlobalPose.INTAKE_SAMPLE_GRAB, GP1_intakeSampleBindings);

        //endregion

        //region Deposit Sample Bindings

        /* ===================
        DEPOSIT SAMPLE BINDINGS
        ======================

        Inherits From: Default Bindings

        Active Modes:   DEPOSIT_SAMPLE_UPPER,
                        DEPOSIT_SAMPLE_LOWER

        */

        BindingSet GP1_depositSampleBindings = new BindingSet(GP1_defaultBindings);
        GP1_depositSampleBindings.bindTask(GamepadButton.LEFT_TRIGGER, new B_ReleaseSampleAndStow());

        bindingManager.setGamepad1Bindings(GlobalPose.DEPOSIT_SAMPLE_UPPER, GP1_depositSampleBindings);
        bindingManager.setGamepad1Bindings(GlobalPose.DEPOSIT_SAMPLE_LOWER, GP1_depositSampleBindings);

        //endregion

        //region Intake Specimen Bindings

        /* =====================
        INTAKE SPECIMEN BINDINGS
        ========================

        Inherits From: Default Bindings

        Active Modes:   INTAKE_SPECIMEN

        */

        BindingSet GP1_intakeSpecimenBindings = new BindingSet(GP1_defaultBindings);
        GP1_intakeSpecimenBindings.bindTask(GamepadButton.RIGHT_BUMPER, new B_StowAfterIntakeSpecimen());

        bindingManager.setGamepad1Bindings(GlobalPose.INTAKE_SPECIMEN, GP1_intakeSpecimenBindings);

        //endregion

        //region Deposit Specimen Bindings

        /* ======================
        DEPOSIT SPECIMEN BINDINGS
        =========================

        Inherits From: Default Bindings

        Active Modes:   DEPOSIT_SPECIMEN

        */

        BindingSet GP1_depositSpecimenBindings = new BindingSet(GP1_defaultBindings);
        GP1_depositSpecimenBindings.bindTask(GamepadButton.LEFT_BUMPER, new B_ReleaseSpecimenAndStow());

        bindingManager.setGamepad1Bindings(GlobalPose.DEPOSIT_SPECIMEN, GP1_depositSpecimenBindings);

        //endregion

        //endregion

        //region Controller 2

        // region Default Bindings

        /* =============
        DEFAULT BINDINGS
        ================ */

        BindingSet GP2_defaultBindings = new BindingSet();
        bindingManager.setGamepad2Bindings(GlobalPose.DEFAULT, GP2_defaultBindings);

        GP2_defaultBindings.bindTask(GamepadButton.Y, new B_HangPre());
        GP2_defaultBindings.bindTask(GamepadButton.A, new B_HangLv2Both());

        //endregion

        //region Intake Sample Bindings

        /* ===================
        INTAKE SAMPLE BINDINGS
        ======================

        Inherits From: Default Bindings

        Active Modes:   INTAKE_SAMPLE_HOVER,
                        INTAKE_SAMPLE_STRAIGHT,
                        INTAKE_SAMPLE_CAMERA_SEARCH,
                        INTAKE_SAMPLE_GRAB

        */

        BindingSet GP2_intakeSampleBindings = new BindingSet(GP2_defaultBindings);
        GP2_intakeSampleBindings.bindTask(GamepadButton.RIGHT_BUMPER, new B_IntakeSampleHover());
        GP2_intakeSampleBindings.bindTask(GamepadButton.LEFT_BUMPER, new B_IntakeSampleGrab());
        GP2_intakeSampleBindings.bindTask(GamepadButton.RIGHT_TRIGGER, new B_StowSampleAfterIntake());

        bindingManager.setGamepad2Bindings(GlobalPose.INTAKE_SAMPLE_HOVER, GP2_intakeSampleBindings);
        bindingManager.setGamepad2Bindings(GlobalPose.INTAKE_SAMPLE_STRAIGHT, GP2_intakeSampleBindings);
        bindingManager.setGamepad2Bindings(GlobalPose.INTAKE_SAMPLE_CAMERA_SEARCH, GP2_intakeSampleBindings);
        bindingManager.setGamepad2Bindings(GlobalPose.INTAKE_SAMPLE_GRAB, GP2_intakeSampleBindings);

        //endregion

        //endregion

        follower.setStartingPose(new Pose(0, 0, 0));
        taskManager.runTask(new B_StowSample());
    }

    @Override
    public void loop() {
        super.loop();
        if (RobotState.getInstance().getMode() == GlobalPose.INTAKE_SAMPLE_HOVER) {
            double newAngle = wristTwistState.getAngle() + gamepad2.right_stick_x * wristRotateSpeed;
            newAngle = Math.min(Math.max(newAngle, WristTwistPose.MIN), WristTwistPose.MAX);
            A_SetWristTwistAngle setWristTwistAngle = new A_SetWristTwistAngle(newAngle);
            setWristTwistAngle.start();
            setWristTwistAngle.loop();
        }

    }

    @Override
    public void start() {
        super.start();
    }


}
