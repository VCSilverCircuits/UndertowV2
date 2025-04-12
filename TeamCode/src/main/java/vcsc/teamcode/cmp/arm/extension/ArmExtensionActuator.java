package vcsc.teamcode.cmp.arm.extension;


import static vcsc.teamcode.config.GlobalConfig.TPR;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import vcsc.core.abstracts.templates.poweredPIDF.PoweredPIDFActuator;
import vcsc.core.util.DcMotorGroup;
import vcsc.core.util.GlobalTelemetry;

public class ArmExtensionActuator extends PoweredPIDFActuator<ArmExtensionState, ArmExtensionPose> {
    // Three 5:1 ultraplanetary gearbox
    public static final double MOTOR_GEAR_RATIO = 5.2; // 3.61
    // Gear ratio of driven gears
    public static final double DRIVE_GEAR_RATIO = 1.0;

    public static final double PULLEY_DIAMETER = 26;
    public static final double CM_PER_TICK = PULLEY_DIAMETER * Math.PI / (10.0 * DRIVE_GEAR_RATIO * MOTOR_GEAR_RATIO * TPR);
    public static final double MAX_EXTENSION_POWER = 1.0;
    DcMotorGroup motors;
    TouchSensor touchSensor;

    public ArmExtensionActuator(HardwareMap hardwareMap, PIDFCoefficients coefficients) {
        super(coefficients);
        DcMotorEx extensionLeft = hardwareMap.get(DcMotorEx.class, "armExtensionLeft");
        DcMotorEx extensionCenter = hardwareMap.get(DcMotorEx.class, "armExtensionCenter");
        DcMotorEx extensionRight = hardwareMap.get(DcMotorEx.class, "armExtensionRight");
        touchSensor = hardwareMap.get(TouchSensor.class, "slideLimitSensor");
        extensionLeft.setDirection(DcMotorSimple.Direction.REVERSE);
//        extensionCenter.setDirection(DcMotorSimple.Direction.REVERSE);
//        extensionRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motors = new DcMotorGroup(extensionLeft, extensionCenter, extensionRight);
        motors.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motors.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motors.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {
        super.loop();
        MultipleTelemetry telem = GlobalTelemetry.getInstance();
//        if (getPosition() < 0) {
//            motors.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            motors.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        }
    }

    public boolean isTouching() {
        return touchSensor.isPressed();
    }

    @Override
    protected void loopPower() {
//        PowerManager powerManager = StateRegistry.getInstance().getState(PowerManager.class);
//        if (powerManager.isThrottled() && !RobotState.getInstance().isHanging()) {
//            motors.setPower(Math.min(power, 0.8));
//        } else {
//            motors.setPower(power);
//        }
        if (motors.getPower() != power) {
            motors.setPower(power);
        }

    }

    public void reset() {
        motors.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motors.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }


    @Override
    protected void loopPID() {
        MultipleTelemetry telemetry = GlobalTelemetry.getInstance();
        // NOTE: Encoders for extension run backwards
        double outputPower = controller.calculate(getPosition());
//        telemetry.addData("Run Position", controller.getSetPoint());
//        telemetry.addData("At Position", controller.atSetPoint());
//        telemetry.addData("Output Power", outputPower);
//        telemetry.addData("Current position", getPosition());
        double adjustedPower = Math.min(Math.abs(outputPower), MAX_EXTENSION_POWER) * Math.signum(outputPower);
        if (motors.getPower() != adjustedPower) {
            motors.setPower(adjustedPower);
        }
    }

    @Override
    public double getPosition() {
        return motors.getCurrentPosition();
    }

    @Override
    public double getCurrent() {
        return motors.getCurrent(CurrentUnit.MILLIAMPS);
    }
}
