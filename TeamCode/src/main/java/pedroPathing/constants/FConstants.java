package pedroPathing.constants;

import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.localization.Localizers;
import com.pedropathing.util.KalmanFilterParameters;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class FConstants {
    static { // 13.7:1 gear ratio
        FollowerConstants.localizers = Localizers.PINPOINT;

        FollowerConstants.leftFrontMotorName = "leftFront";
        FollowerConstants.leftRearMotorName = "leftRear";
        FollowerConstants.rightFrontMotorName = "rightFront";
        FollowerConstants.rightRearMotorName = "rightRear";

        FollowerConstants.leftFrontMotorDirection = DcMotorSimple.Direction.REVERSE;
        FollowerConstants.leftRearMotorDirection = DcMotorSimple.Direction.REVERSE;
        FollowerConstants.rightFrontMotorDirection = DcMotorSimple.Direction.FORWARD;
        FollowerConstants.rightRearMotorDirection = DcMotorSimple.Direction.FORWARD;

        FollowerConstants.mass = 12.16;

        FollowerConstants.xMovement = 86.78114325227486;
        FollowerConstants.yMovement = 70.65324774;

        FollowerConstants.forwardZeroPowerAcceleration = -34.94781845965991;
        FollowerConstants.lateralZeroPowerAcceleration = -76.36552090173106;

        FollowerConstants.translationalPIDFCoefficients.setCoefficients(0.4, 0, 0.05, 0);
        FollowerConstants.useSecondaryTranslationalPID = false;
        FollowerConstants.secondaryTranslationalPIDFCoefficients.setCoefficients(0.1, 0, 0.01, 0); // Not being used, @see useSecondaryTranslationalPID

        FollowerConstants.headingPIDFCoefficients.setCoefficients(1.5, 0, 0.1, 0);
        FollowerConstants.useSecondaryHeadingPID = false;
        FollowerConstants.secondaryHeadingPIDFCoefficients.setCoefficients(2, 0, 0.1, 0); // Not being used, @see useSecondaryHeadingPID

        FollowerConstants.drivePIDFCoefficients.setCoefficients(0.01, 0, 0.001, 0.6, 0);
        FollowerConstants.useSecondaryDrivePID = false;
        FollowerConstants.drivePIDFSwitch = 38;
        FollowerConstants.secondaryDrivePIDFCoefficients.setCoefficients(0.01, 0, 0, 0.6, 0);

        FollowerConstants.zeroPowerAccelerationMultiplier = 3;
        FollowerConstants.centripetalScaling = 0.0005;

        FollowerConstants.driveKalmanFilterParameters = new KalmanFilterParameters(15, 1);

        FollowerConstants.pathEndTimeoutConstraint = 500;
        FollowerConstants.pathEndTValueConstraint = 0.995;
        FollowerConstants.pathEndVelocityConstraint = 0.1;
        FollowerConstants.pathEndTranslationalConstraint = 0.1;
        FollowerConstants.pathEndHeadingConstraint = 0.007;
        FollowerConstants.useBrakeModeInTeleOp = true;
        FollowerConstants.useVoltageCompensationInAuto = true;
    }
}
