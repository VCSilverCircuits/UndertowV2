package vcsc.teamcode.cmp.arm.rotation;

import static vcsc.teamcode.config.GlobalConfig.TPR;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import vcsc.core.abstracts.templates.poweredPIDF.PoweredPIDFActuator;
import vcsc.core.util.DcMotorGroup;
import vcsc.core.util.GlobalTelemetry;

public class ArmRotationActuator extends PoweredPIDFActuator<ArmRotationState, ArmRotationPose> {
    // Three 4:1 ultraplanetary gearboxes
    public static final double MOTOR_GEAR_RATIO = 3.61 * 3.61 * 3.61;
    // Gear ratio of driven gears
    public static final double DRIVE_GEAR_RATIO = 52.0 / 24.0;
    public static final double DEGREES_PER_TICK = 360.0 / (TPR * MOTOR_GEAR_RATIO * DRIVE_GEAR_RATIO);
    DcMotorGroup motors;
    private double maxSpeed = 0.75;

    public ArmRotationActuator(HardwareMap hardwareMap, PIDFCoefficients coefficients) {
        super(coefficients);
        DcMotorEx rotation = hardwareMap.get(DcMotorEx.class, "armRotation");
        motors = new DcMotorGroup(rotation);
        motors.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motors.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motors.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double spd) {
        maxSpeed = spd;
    }

    @Override
    public double getPosition() {
        return -motors.getCurrentPosition();
    }

    public void reset() {
        motors.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motors.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        controller.setSetPoint(0);
    }

    @Override
    public void loop() {
        super.loop();
//        if (getPosition() < 0) {
//            motors.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            motors.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        }
    }

    @Override
    public double getCurrent() {
        return motors.getCurrent(CurrentUnit.MILLIAMPS);
    }

    @Override
    protected void loopPower() {
        motors.setPower(power);
        controller.setSetPoint(getPosition());
    }

    @Override
    protected void loopPID() {
        MultipleTelemetry telemetry = GlobalTelemetry.getInstance();
        double outputPower = controller.calculate(getPosition());
//        telemetry.addData("Run Position", controller.getSetPoint());
//        telemetry.addData("At Position", controller.atSetPoint());
//        telemetry.addData("Output Power", outputPower);
//        telemetry.addData("Current position", getPosition());
        motors.setPower(Math.min(Math.abs(outputPower), maxSpeed) * Math.signum(outputPower));
    }
}
