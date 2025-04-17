package vcsc.teamcode.opmodes.base;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.List;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;
import vcsc.core.abstracts.power.PowerManager;
import vcsc.core.abstracts.state.StateRegistry;
import vcsc.core.abstracts.task.TaskManager;
import vcsc.core.util.GlobalTelemetry;
import vcsc.core.util.gamepad.BindingManager;
import vcsc.core.util.gamepad.BindingSet;
import vcsc.core.util.gamepad.GamepadWrapper;
import vcsc.teamcode.behavior.sample.B_StowSample;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionActuator;
import vcsc.teamcode.cmp.arm.extension.ArmExtensionState;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationActuator;
import vcsc.teamcode.cmp.arm.rotation.ArmRotationState;
import vcsc.teamcode.cmp.camera.Camera;
import vcsc.teamcode.cmp.claw.ClawActuator;
import vcsc.teamcode.cmp.claw.ClawState;
import vcsc.teamcode.cmp.elbow.ElbowActuator;
import vcsc.teamcode.cmp.elbow.ElbowState;
import vcsc.teamcode.cmp.robot.FollowerWrapper;
import vcsc.teamcode.cmp.robot.RobotState;
import vcsc.teamcode.cmp.wrist.hinge.WristHingeActuator;
import vcsc.teamcode.cmp.wrist.hinge.WristHingeState;
import vcsc.teamcode.cmp.wrist.twist.WristTwistActuator;
import vcsc.teamcode.cmp.wrist.twist.WristTwistState;
import vcsc.teamcode.config.GlobalConfig;
import vcsc.teamcode.config.GlobalPose;

public class BaseOpMode extends OpMode {
    protected MultipleTelemetry telem;
    protected TaskManager taskManager = TaskManager.getInstance();
    protected BindingManager bindingManager = BindingManager.getInstance();
    protected RobotState robotState = RobotState.getInstance();
    protected ElapsedTime matchTimer;
    protected ClawState clawState;
    protected ArmExtensionState armExtState;
    protected ArmRotationState armRotState;
    protected ElbowState elbowState;
    protected WristHingeState wristHingeState;
    protected WristTwistState wristTwistState;
    protected PowerManager powerManager;
    protected Follower follower;
    ClawActuator clawActuator;
    ArmExtensionActuator armExtActuator;
    ArmRotationActuator armRotActuator;
    ElbowActuator elbowActuator;
    WristHingeActuator wristHingeActuator;
    WristTwistActuator wristTwistActuator;
    boolean rumbledEndGame = false, rumbledMatchEnd = false;

    GamepadWrapper gw1, gw2;
    List<LynxModule> allHubs;

    @Override
    public void init() {
        GlobalTelemetry.init(telemetry);

        telem = GlobalTelemetry.getInstance();

        allHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        matchTimer = new ElapsedTime();


        StateRegistry reg = StateRegistry.getInstance();
        reg.clearStates();

        clawState = new ClawState();
        clawActuator = new ClawActuator(hardwareMap);
        clawState.registerActuator(clawActuator);

        armExtState = new ArmExtensionState();
        armExtActuator = new ArmExtensionActuator(hardwareMap, GlobalConfig.extensionCoeffs);
        armExtState.registerActuator(armExtActuator);

        armRotState = new ArmRotationState();
        armRotActuator = new ArmRotationActuator(hardwareMap, GlobalConfig.rotationCoeffs);
        armRotState.registerActuator(armRotActuator);

        elbowState = new ElbowState();
        elbowActuator = new ElbowActuator(hardwareMap);
        elbowState.registerActuator(elbowActuator);

        wristHingeState = new WristHingeState();
        wristHingeActuator = new WristHingeActuator(hardwareMap);
        wristHingeState.registerActuator(wristHingeActuator);

        wristTwistState = new WristTwistState();
        wristTwistActuator = new WristTwistActuator(hardwareMap);
        wristTwistState.registerActuator(wristTwistActuator);

        Camera camera = new Camera(hardwareMap);

//        powerManager = new PowerManager(hardwareMap);

        gw1 = new GamepadWrapper(gamepad1);
        gw2 = new GamepadWrapper(gamepad2);

        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        FollowerWrapper.setFollower(follower);

        bindingManager.setDefaultMode(GlobalPose.DEFAULT);
        bindingManager.setGamepad1Bindings(GlobalPose.DEFAULT, new BindingSet());
        bindingManager.setGamepad2Bindings(GlobalPose.DEFAULT, new BindingSet());

        // Register all states
        reg.registerStates(clawState, armExtState, armRotState, elbowState, wristHingeState, wristTwistState, camera);
    }

    @Override
    public void start() {
        super.start();
        matchTimer.reset();
        follower.startTeleopDrive();
    }

    @Override
    public void init_loop() {
        super.init_loop();
    }

    @Override
    public void loop() {
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }

        System.out.println("===================================");
        System.out.println("Overall States:");
        System.out.println("Robot Mode: " + robotState.getMode());
        System.out.println("Claw Pose: " + clawState.getPose());
        System.out.println("Arm Extension Pose: " + armExtState.getTargetPose());
        System.out.println("Ext actuator touch sensor: " + armExtActuator.isTouching());
        System.out.println("Arm Rotation Pose: " + armRotState.getTargetPose());
        System.out.println("Elbow Pose: " + elbowState.getPose());
        System.out.println("Wrist Hinge Pose: " + wristHingeState.getPose());
        System.out.println("Wrist Twist Pose: " + wristTwistState.getPose());
        System.out.println("---------- Task Manager -------------");
        taskManager.loop();
        System.out.println("----------- Bindings ------------");

        gw1.loop();
        gw2.loop();
        bindingManager.loop(gw1, gw2, robotState.getMode());

        System.out.println("----------- Actuators ------------");

        clawActuator.loop();
        armExtActuator.loop();
        armRotActuator.loop();
        elbowActuator.loop();
        wristHingeActuator.loop();
        wristTwistActuator.loop();

        System.out.println("---------- Misc -------------");

        if (robotState.getMode() != GlobalPose.INTAKE_SAMPLE_CAMERA_SEARCH) {
            follower.setTeleOpMovementVectors(
                    -gamepad1.left_stick_y * robotState.getDriveSpeed(),
                    -gamepad1.left_stick_x * robotState.getDriveSpeed(),
                    -gamepad1.right_stick_x * robotState.getTurnSpeed(),
                    true
            );
        }

        if (Thread.currentThread().isInterrupted()) {
            gamepad1.rumbleBlips(5);
            gamepad2.rumbleBlips(5);
            if (gamepad1.start) {
                taskManager.runTask(new B_StowSample());
                requestOpModeStop();
            }
        }

        /*  ================
            Both Controllers
            ================ */

        // ----- Rumbling -----
        if (matchTimer.seconds() >= 90 && !rumbledEndGame) { // End game rumble
            gamepad1.rumble(500);
            gamepad2.rumble(500);
            rumbledEndGame = true;
        } else if (matchTimer.seconds() >= 120 && !rumbledMatchEnd) { // Match end 3 rumbles
            gamepad1.rumbleBlips(3);
            gamepad2.rumbleBlips(3);
            rumbledMatchEnd = true;
        }

        follower.update();

        telem.update();
    }

    @Override
    public void stop() {
        super.stop();
        follower.setTeleOpMovementVectors(0, 0, 0);
        taskManager.clearTasks();
        armExtState.cancelAction();
        armRotState.cancelAction();
    }
}
